package com.example.blockchainprac.utils;

public class VoteUser {
    String ID;
    Boolean voted;
    String votingTimestamp;
    String email;

    public VoteUser() {
    }

    public VoteUser(String ID, Boolean voted, String votingTimestamp, String email) {
        this.ID = ID;
        this.voted = voted;
        this.votingTimestamp = votingTimestamp;
        this.email = email;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Boolean getVoted() {
        return voted;
    }

    public void setVoted(Boolean voted) {
        this.voted = voted;
    }

    public String getVotingTimestamp() {
        return votingTimestamp;
    }

    public void setVotingTimestamp(String votingTimestamp) {
        this.votingTimestamp = votingTimestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
