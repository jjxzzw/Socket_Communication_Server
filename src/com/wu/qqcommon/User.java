package com.wu.qqcommon;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private String userId ;//用户名
    private String passwd;//密码
    private static final long serialVersionUID = 1L;

    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public User() {
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

}
