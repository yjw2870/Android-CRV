package com.example.snarkportingtest;

public class User {
    //private String profile;
    private String id;
    private String pw;
    private String name;
    private int number;

    public User(){}

    public User(String id, String pw, String name, int number) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
