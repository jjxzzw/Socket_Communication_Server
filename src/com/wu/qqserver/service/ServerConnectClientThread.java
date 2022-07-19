package com.wu.qqserver.service;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.wu.qqcommon.Message;
import com.wu.qqcommon.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnectClientThread extends Thread {

    private Socket socket;
    private String userId;

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("服务器端和客户端"+userId+"保持通信,读取信息");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    System.out.println(message.getSender()+"请求的在线用户列表");
                    Message messageUserList = new Message();
                    String onlineUser = ManageServerThread.getOnlineUser();
//                    messageUserList.setContent(onlineUser);
//                    messageUserList.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
//                    messageUserList.setGetter(message.getSender());
                    oos.writeObject(MessageService.setReturnOnlineLstMes(messageUserList,onlineUser));
                }else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    System.out.println(message.getSender()+"用户退出系统");
                    Message messageExit = new Message();
//                    messageExit.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
//                    messageExit.setGetter(message.getSender());
                    oos.writeObject(MessageService.setClientExitMes(messageExit));
                    ManageServerThread.removeServerThread(message.getSender());
                    ois.close();
                    oos.close();
                    socket.close();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
