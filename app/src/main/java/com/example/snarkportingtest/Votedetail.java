package com.example.snarkportingtest;

import java.io.Serializable;

public class Votedetail implements Serializable {
    private int vote_id;
    private String title;
    private String start;
    private String end;
    private String type;
    private String note;
    private String created;

    public Votedetail() {

    }

    public Votedetail(int vote_id, String title, String start, String end, String type, String note, String created) {
        this.vote_id = vote_id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.type = type;
        this.note = note;
        this.created = created;
    }

    public int getVote_id() {
        return vote_id;
    }

    public void setVote_id(int vote_id) {
        this.vote_id = vote_id;
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
