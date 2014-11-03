package com.mahadihasan.client;

/**
 *
 * @author Md Mahadi Hasan
 */

import java.io.Serializable;

public class UserInfo implements Serializable {   

    private String userName;
    private String message;

    public UserInfo() {
    }

    public void sendMessage(String sMessage) {
        message = sMessage;
    }

    public void setUser(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public String getUserName() {
        return userName;
    }
}
