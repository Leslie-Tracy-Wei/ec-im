package ec.im.webServer.chat.model;


/**
 * 通讯实体类
 */
public class Message {
    /**
     * 消息类型
     */
    private String messageType;
    /**
     * 消息发送者id
     */
    private Integer userId;
    /**
     * 消息接受者id或群聊id
     */
    private Integer chatId;
    /**
     * 消息内容
     */
    private String message;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
}
