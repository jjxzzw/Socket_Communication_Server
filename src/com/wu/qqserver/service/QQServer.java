package com.wu.qqserver.service;

import com.wu.qqcommon.Message;
import com.wu.qqcommon.MessageType;
import com.wu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**n
 * 服务器监听9999端口
 */
public class QQServer {
    private static ConcurrentHashMap<String,User> hashMap = new ConcurrentHashMap<>();
    private ServerSocket serverSocket = null;
    static {
        hashMap.put("110",new User("110","123456"));
        hashMap.put("120",new User("120","123456"));
        hashMap.put("130",new User("130","123456"));
        hashMap.put("刹那",new User("刹那","123456"));
        hashMap.put("菲尔特",new User("菲尔特","123456"));
        hashMap.put("高达",new User("高达","123456"));
    }

    public boolean checkUser(String userId,String pwd){
        User user = hashMap.get(userId);
        if (user == null){
            return false;
        }
        return user.getPasswd().equals(pwd);
    }

    public QQServer() {
        //端口信息可以在配置文件中记录
        try {
            System.out.println("在9999端口监听");
            serverSocket = new ServerSocket(9999);

            while (true) {//在与某个客户端连接后,依然可以持续监听

                Socket socket = serverSocket.accept();
                //得到socket关联的对象输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //得到socket关联的对象输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                //读取对象
                User user = (User) ois.readObject();
                //创建message对象,准备进行回复
                Message message = new Message();
                //判断用户信息是否正确
                if (checkUser(user.getUserId(),user.getPasswd())) {//正确则回复对应报文,并开启一个与之联系的线程服务
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCED);
                    //回复报文
                    oos.writeObject(message);
                    //建立一个以userId为区分的线程服务对象
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, user.getUserId());
                    //开启线程
                    serverConnectClientThread.start();
                    //将线程键入一个线程集合中
                    ManageServerThread.addServerThread(user.getUserId(), serverConnectClientThread);
                } else {//错误则回复对应报文,并关闭socket
                    System.out.println("用户:"+user.getUserId()+"密码:"+user.getPasswd()+"服务器登录失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
//    private ServerSocket serverSocket = null;
//    private static User user = new User("110","123456");
//
//
//
//    @Override
//    public void run() {
//        try {
//            ServerSocket serverSocket = new ServerSocket(9999);
//            Socket socket = serverSocket.accept();
//            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//            User clientUser = (User) ois.readObject();
//            if (clientUser.equals(user)){
//                new MessageServerService().sendMessage(socket);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
