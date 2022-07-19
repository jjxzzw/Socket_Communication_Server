package com.wu.qqserver.service;

import com.wu.qqcommon.Message;
import com.wu.qqcommon.MessageType;

public class MessageService {
    public static Message setReturnOnlineLstMes(Message message, String onlineUser){
        message.setContent(onlineUser);
        message.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
        message.setSender("服务器端口9999");
        message.setGetter(message.getSender());
        return message;
    }

    public static Message setClientExitMes(Message message){
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender("服务器端口9999");
        message.setGetter(message.getSender());
        return message;
    }
}
