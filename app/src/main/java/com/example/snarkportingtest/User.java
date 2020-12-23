package com.example.snarkportingtest;

public class User {
    //private String profile;
    private String uid;
    private String votelist;

    public User(){}

    public User(String uid, String votelist) {
        this.uid = uid;
        this.votelist = votelist;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVotelist() {
        return votelist;
    }

    public void setVotelist(String votelist) {
        this.votelist = votelist;
    }
}
