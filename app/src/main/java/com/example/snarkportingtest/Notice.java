package com.example.snarkportingtest;

public class Notice {
    //private String profile;
    private String noti_title;
    private String noti_body;
    private String data;

    public Notice(String noti_title, String noti_body, String data) {
        this.noti_title = noti_title;
        this.noti_body = noti_body;
        this.data = data;
    }

    public String getNoti_title() {
        return noti_title;
    }

    public void setNoti_title(String noti_title) {
        this.noti_title = noti_title;
    }

    public String getNoti_body() {
        return noti_body;
    }

    public void setNoti_body(String noti_body) {
        this.noti_body = noti_body;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
