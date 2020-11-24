package com.example.snarkportingtest;

import java.io.Serializable;

public class Votedetail implements Serializable {
    private String title;
    private String start;
    private String end;
    private String function;
    private String type;
    private String note;

    public Votedetail() {

    }

    public Votedetail(String title, String start, String end, String function, String type, String note) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.function = function;
        this.type = type;
        this.note = note;
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

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
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

}
