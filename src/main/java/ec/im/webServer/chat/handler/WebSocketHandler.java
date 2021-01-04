package ec.im.webServer.chat.handler;

import ec.im.webServer.base.utils.JsonUtils;
import ec.im.webServer.base.utils.SpringUtil;
import ec.im.webServer.chat.model.Message;
import ec.im.webServer.chat.repository.UserGroupRepository;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求处理的handler
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    // 定义一个channel组 ，管理所有的channel
    // GlobalEventExecutor.INSTANCE 全局的事件执行器 单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 存储用户id和用户的channelId绑定
     */
    public static ConcurrentHashMap<Long, ChannelId> userMap = new ConcurrentHashMap<>();
    /**
     * 用于存储群聊房间号和群聊成员的channel信息
     */
    public static ConcurrentHashMap<Integer, ChannelGroup> groupMap = new ConcurrentHashMap<>();

    /**
     * 获取用户拥有的群聊id号
     */
    UserGroupRepository userGroupRepositor = SpringUtil.getBean(UserGroupRepository.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //首次连接是FullHttpRequest，把用户id和对应的channel对象存储起来
        if (msg != null && msg instanceof FullHttpRequest){
            FullHttpRequest request = (FullHttpRequest)msg;
            String uri = request.uri();
            // 对应的userId
            Long userId = getUrlParams(uri);
            // 绑定userId 与channelId
            userMap.put(userId, ctx.channel().id());
            logger.info("登录的用户id是：{} ，channelId : {}", userId,ctx.channel().id());
            //第1次登录,需要查询下当前用户是否加入过群,加入过群,则放入对应的群聊里
            List<Integer> groupIds = userGroupRepositor.findGroupIdByUserId(userId);
            ChannelGroup cGroup = null;
            //查询用户拥有的组是否已经创建了
            for (Integer groupId : groupIds) {
                cGroup = groupMap.get(groupId);
                //如果群聊管理对象没有创建
                if (cGroup == null) {
                    //构建一个channelGroup群聊管理对象然后放入groupMap中
                    cGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
                    groupMap.put(groupId, cGroup);
                }
                //把用户放到群聊管理对象里去
                cGroup.add(ctx.channel());
            }


            //如果url包含参数，需要处理
            if (uri.contains("?")) {
                String newUri = uri.substring(0, uri.indexOf("?"));
                request.setUri(newUri);
            }


        } else if (msg instanceof TextWebSocketFrame){
            // 传输的聊天信息
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;

            logger.info("客户端收到服务器数据：{}", frame.text());

            Message message = JsonUtils.serializable(frame.text(), Message.class);

            // 对于群聊 以及 单独聊天不同处理 1-群聊 2-单独聊
            if (Objects.equals(message.getMessageType(),"group")){
                //推送群聊信息
                groupMap.get(message.getChatId()).writeAndFlush(new TextWebSocketFrame(JsonUtils.deserializer(message)));
            } else if (Objects.equals(message.getMessageType(),"chat")){
                //处理私聊的任务，如果对方也在线,则推送消息
                // todo 离线消息todo
                ChannelId channelId = userMap.get(Long.parseLong(message.getChatId().toString()));
                if (channelId != null) {
                    Channel ct = channelGroup.find(channelId);
                    if (ct != null) {
                        ct.writeAndFlush(new TextWebSocketFrame(JsonUtils.deserializer(message)));
                    }
                }
            }
        }
        super.channelRead(ctx,msg);
    }

    /**
     * 断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("与客户端连接断开，通道关闭");
        channelGroup.remove(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("与客户端建立连接，通道开启！");
        //添加到channelGroup通道组
        channelGroup.add(ctx.channel());
        logger.info("ctx id is " + ctx.channel().id());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 该方法会将所有的channel遍历并发送 无须在遍历
        System.out.println("[客户端]" + ctx.channel().remoteAddress() + "加入聊天\n");
        channelGroup.writeAndFlush("[客户端]" + ctx.channel().remoteAddress() + "加入聊天\n");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().id().asLongText());
        channelGroup.writeAndFlush("客户端" + ctx.channel().remoteAddress() + "离开了\n");
    }


    /**
     * 获取url中的userId
     * @param url
     * @return
     */
    private static Long getUrlParams(String url) {
        if (!url.contains("=")) {
            return null;
        }
        String userId = url.substring(url.indexOf("=") + 1);
        return Long.parseLong(userId);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("error cause " + cause);
        cause.printStackTrace();
        ctx.close();
    }

}
