package com.example.snarkportingtest;

import java.io.Serializable;

public class Candidate implements Serializable {
    private String profile;
    private String name;
    private String group;
    private String note;
    private int votes;

    public Candidate() {

    }

    public Candidate(String profile, String name, String group, String note, int votes) {
        this.profile = profile;
        this.name = name;
        this.group = group;
        this.note = note;
        this.votes = votes;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
