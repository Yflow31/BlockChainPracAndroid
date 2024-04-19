package com.example.blockchainprac;

public class ShowData {
    private String hash;
    private String data;

    public ShowData() {
    }

    public ShowData(String hash, String data) {
        this.hash = hash;
        this.data = data;
    }

    // Getters and setters
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
