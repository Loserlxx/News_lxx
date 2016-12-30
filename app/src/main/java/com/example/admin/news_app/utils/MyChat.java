package com.example.admin.news_app.utils;

/**
 * Created by admin on 2016-12-23.
 */

public class MyChat {
    private int type;//0:代表接收 1：代表发送
    private String chat;
    private String img;

    public MyChat(int type, String chat) {
        this.type = type;
        this.chat = chat;
    }

    public MyChat(int type, String chat, String img) {
        this.type = type;
        this.chat = chat;
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
