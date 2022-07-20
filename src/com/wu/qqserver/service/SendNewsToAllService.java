package com.wu.qqserver.service;

import com.wu.qqcommon.Message;
import com.wu.qqcommon.MessageType;
import com.wu.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class SendNewsToAllService implements Runnable{



    @Override
    public void run() {
        while (true) {

            System.out.println("请输入想要推送的消息(输入exit退出推送系统):");
            String news = Utility.readString(100);

            if (news.equals("exit")){
                break;
            }

            Message message = new Message();
            message.setSender("服务器");
            message.setContent(news);
            message.setSendTime(new Date().toString());
            message.setMesType(MessageType.MESSAGE_COMM_MES_TOALL);

            String onlineUser = ManageServerThread.getOnlineUser();
            String[] users = onlineUser.split(" ");

            for (String s : users) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(
                            ManageServerThread.getServerThread(s).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
