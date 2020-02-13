package com.example.mjd_final.model;

public class Vote {
    private int voteCount, approve;
    private boolean isVoting;

    public Vote(int voteCount, int approve, boolean isVoting) {
        this.voteCount = voteCount;
        this.approve = approve;
        this.isVoting = isVoting;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getApprove() {
        return approve;
    }

    public void setApprove(int approve) {
        this.approve = approve;
    }

    public boolean isVoting() {
        return isVoting;
    }

    public void setVoting(boolean voting) {
        isVoting = voting;
    }
}
