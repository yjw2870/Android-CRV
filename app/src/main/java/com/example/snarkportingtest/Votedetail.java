package com.example.snarkportingtest;

import java.io.Serializable;

public class Votedetail implements Serializable {
    private String title;
    private String start;
    private String end;
    private String type;
    private String note;
    private String group;
    private String voted;
    private String created;

    public Votedetail() {

    }

    public Votedetail(String title, String start, String end, String type, String note, String group, String voted, String created) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.type = type;
        this.note = note;
        this.group = group;
        this.voted = voted;
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVoted() {
        return voted;
    }

    public void setVoted(String voted) {
        this.voted = voted;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
