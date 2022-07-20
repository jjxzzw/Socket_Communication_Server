package com.wu.qqserver.service;

import com.wu.qqcommon.Message;
import com.wu.qqcommon.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 服务器端socket线程
 */

public class ServerConnectClientThread extends Thread {

    private Socket socket;
    private String userId;

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    //线程
    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("服务器端和客户端" + userId + "保持通信,读取信息");

                if(ManageOfflineMessage.getMessagehm().containsKey(userId)){
                    ManageOfflineMessage.getOfflineMes(userId);
                    ManageOfflineMessage.removeOffMes(userId);
                }

                //获取输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();


                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {//拉取在线用户列表
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());//输出流
                    System.out.println(message.getSender() + "请求的在线用户列表");
                    Message messageUserList = new Message();//创建一个数据报对象
                    String onlineUser = ManageServerThread.getOnlineUser();//在线用户的userId拼接的字符串
//                    messageUserList.setContent(onlineUser);
//                    messageUserList.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    messageUserList.setGetter(message.getSender());//设置接收者
                    oos.writeObject(MessageService.setReturnOnlineLstMes(messageUserList, onlineUser));//设置数据报并写入数据

                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {//私聊消息
                    //获取对应用户的socket线程
                    ServerConnectClientThread serverThread = ManageServerThread.getServerThread(message.getGetter());

                    if (serverThread != null) {//该用户在线
                        System.out.println(message.getSender() + "向" + message.getGetter() + "发送消息");
                        ObjectOutputStream oosRetrains = new ObjectOutputStream(serverThread.socket.getOutputStream());
                        oosRetrains.writeObject(message);
                    } else if (QQServer.getHashMap().containsKey(message.getGetter())) {//用户不在线
                        ManageOfflineMessage.addOfflineMes(message.getGetter(), message);
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        message.setMesType(MessageType.MESSAGE_NOT_ONLINE_USER);
                        oos.writeObject(message);
                    } else {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        message.setMesType(MessageType.MESSAGE_NOT_FOUND_USER);
                        oos.writeObject(message);
                    }

                } else if (message.getMesType().equals(MessageType.MESSAGE_GROUP_CHAT_MES)) {//群聊消息
                    System.out.println(message.getSender() + "向全体在线用户发消息");
                    String onlineUser = ManageServerThread.getOnlineUser();//在线用户的userId拼接的字符串
                    String[] users = onlineUser.split(" ");//得到在线用户userId的字符串数组
                    //message.setMesType(MessageType.MESSAGE_COMM_MES);
                    for (String s : users) {
                        if (!s.equals(message.getSender())) {//向除了自己以外的所有在线用户发送消息
                            ObjectOutputStream oos = new ObjectOutputStream(ManageServerThread.getServerThread(s).socket.getOutputStream());
                            oos.writeObject(message);
                        }
                    }

                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {//发送文件
                    ServerConnectClientThread serverThread = ManageServerThread.getServerThread(message.getGetter());
                    if (serverThread != null) {//该用户在线
                        System.out.println(message.getSender() + "向" + message.getGetter() + "发送文件");

                        ObjectOutputStream oosRetrains = new ObjectOutputStream(serverThread.socket.getOutputStream());
                        oosRetrains.writeObject(message);

                    } else if (QQServer.getHashMap().containsKey(message.getGetter())) {//用户不在线
                        ManageOfflineMessage.addOfflineMes(message.getGetter(), message);
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        message.setMesType(MessageType.MESSAGE_NOT_ONLINE_USER);
                        oos.writeObject(message);
                    } else {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        message.setMesType(MessageType.MESSAGE_NOT_FOUND_USER);
                        oos.writeObject(message);
                    }

                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {//退出系统
                    System.out.println(message.getSender() + "用户退出系统");
                    Message messageExit = new Message();
//                    messageExit.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
                    messageExit.setGetter(message.getSender());
                    //oos.writeObject(MessageService.setClientExitMes(messageExit));
                    ManageServerThread.removeServerThread(message.getSender());
                    ois.close();
                    socket.close();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }

        }
    }
}
