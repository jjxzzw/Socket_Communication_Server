package com.wu.qqserver.service;

import com.wu.qqcommon.Message;
import com.wu.qqcommon.MessageType;

/**
 * 数据报加工模块
 */
public class MessageService {
    //在线用户列表数据报(服务器端)
    public static Message setReturnOnlineLstMes(Message message, String onlineUser){
        message.setContent(onlineUser);
        message.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
        message.setSender("服务器端口9999");
        return message;
    }

    //退出系统数据报(服务器端)
    public static Message setClientExitMes(Message message){
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender("服务器端口9999");
        return message;
    }

}
