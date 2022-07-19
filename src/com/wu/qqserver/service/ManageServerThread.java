package com.wu.qqserver.service;

import com.wu.qqcommon.User;

import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * 管理服务器端的线程
 */
public class ManageServerThread {
    //userId为键值,socket线程对象为Value的HashMap集合
    private static HashMap<String,ServerConnectClientThread> hm = new HashMap<>();

    /**
     * 添加线程
     * @param userId 用户名(Key)
     * @param serverConnectClientThread socket线程对象(Value)
     */
    public static void addServerThread(String userId,ServerConnectClientThread serverConnectClientThread){
        hm.put(userId,serverConnectClientThread);
    }

    /**
     * 获取对应线程对象
     * @param userId 用户名(Key)
     * @return 线程对象
     */

    public static ServerConnectClientThread getServerThread(String userId){
        return hm.get(userId);
    }

    /**
     * 删除对应线程对象
     * @param userId 用户名(Key)
     */
    public static void removeServerThread(String userId){
        hm.remove(userId);
    }

    /**
     * 拼接当前在线用户的userId
     * @return userId字符串
     */
    public static String getOnlineUser(){
        StringBuilder userList = new StringBuilder();
        Set<String> set = hm.keySet();//keySet获得键值用户名
        for (String s :set) {
            userList.append(s).append(" ");
        }
        return userList.toString();
    }

}
