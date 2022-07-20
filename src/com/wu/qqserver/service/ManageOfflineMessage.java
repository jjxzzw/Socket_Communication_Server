package com.wu.qqserver.service;

import com.wu.qqcommon.Message;
import com.wu.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ManageOfflineMessage {
    private static ConcurrentHashMap<String, ArrayList<Message>> messagehm = new ConcurrentHashMap<>();

    /**
     * 添加离线消息
     * @param getterId 离线用户
     * @param message 离线缓存消息
     */
    public static void addOfflineMes(String getterId,Message message){
        ArrayList<Message> tempMessages = messagehm.get(getterId);
        if (tempMessages != null){
            tempMessages.add(message);
        } else {
            ArrayList<Message> messages = new ArrayList<>();
            messages.add(message);
            messagehm.put(getterId,messages);
        }

    }


    /**
     * 获取缓存的离线消息
     * @param getterId 离线用户
     */
    public static void getOfflineMes(String getterId){
        ArrayList<Message> messages = messagehm.get(getterId);
        for (Message mes : messages) {

            try {
                mes.setMesType(MessageType.MESSAGE_OFFLINE_MES);
                ObjectOutputStream oos = new ObjectOutputStream(
                        ManageServerThread.getServerThread(getterId).getSocket().getOutputStream());
                oos.writeObject(mes);
                Thread.sleep(50);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 删除缓存的离线消息
     * @param getterId 离线用户
     */
    public static void removeOffMes(String getterId){
        ArrayList<Message> messages = messagehm.get(getterId);
        messagehm.remove(getterId,messages);
    }

    public static ConcurrentHashMap<String, ArrayList<Message>> getMessagehm() {
        return messagehm;
    }
}
