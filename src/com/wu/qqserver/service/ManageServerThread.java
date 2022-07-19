package com.wu.qqserver.service;

import com.wu.qqcommon.User;

import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class ManageServerThread {
    private static HashMap<String,ServerConnectClientThread> hm = new HashMap<>();

    public static void addServerThread(String userId,ServerConnectClientThread serverConnectClientThread){
        hm.put(userId,serverConnectClientThread);
    }

    public static ServerConnectClientThread getServerThread(String userId){
        return hm.get(userId);
    }

    public static void removeServerThread(String userId){
        hm.remove(userId);
    }

    public static String getOnlineUser(){
        StringBuffer userList = new StringBuffer();
        Set<String> set = hm.keySet();
        for (String s :set) {
            userList.append(s).append(" ");
        }
        return userList.toString();
    }

}
