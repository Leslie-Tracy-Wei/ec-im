package ec.im.webServer.chat.server;

import ec.im.webServer.chat.handler.OnlineWebSocketHandler;
import ec.im.webServer.chat.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer {
    Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private final Integer port;

    public NettyServer(int port){
        this.port = port;
    }


    public void start() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            logger.info("accepct new connect");
                            //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                            pipeline.addLast(new HttpServerCodec());
                            //以块的方式来写的处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            pipeline.addLast(new OnlineWebSocketHandler());//添加在线客服聊天消息处理类
//                            pipeline.addLast(new WebSocketHandler());//添加测试的聊天消息处理类
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, 65536 * 10));
                        }
                    });

            // 服务器异步创建绑定
            ChannelFuture cf = b.bind().sync();
            logger.info(NettyServer.class + " 启动正在监听： " + cf.channel().localAddress());
            // 关闭服务器通道
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
