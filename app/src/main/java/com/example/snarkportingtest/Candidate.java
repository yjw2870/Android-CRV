package com.example.snarkportingtest;

import java.io.Serializable;

public class Candidate implements Serializable {
    private int candidate_id;
    private int vote_id;
    private String profile;
    private String name;
    private String group;
    private String note;

    public Candidate() {

    }

    public Candidate(int candidate_id, int vote_id, String profile, String name, String group, String note) {
        this.candidate_id = candidate_id;
        this.vote_id = vote_id;
        this.profile = profile;
        this.name = name;
        this.group = group;
        this.note = note;
    }
    // for create vote - add candidate
    public Candidate(String profile, String name, String group, String note) {
        this.profile = profile;
        this.name = name;
        this.group = group;
        this.note = note;
    }

    public int getCandidate_id() {
        return candidate_id;
    }

    public void setCandidate_id(int candidate_id) {
        this.candidate_id = candidate_id;
    }

    public int getVote_id() {
        return vote_id;
    }

    public void setVote_id(int vote_id) {
        this.vote_id = vote_id;
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
}
