package com.example.blockchainprac;

public class lastData {
    private String hash;
    private String data;
    private int index;
    private String timestamp;

    public lastData() {
    }

    public lastData(String hash, String data, int index, String timestamp) {
        this.hash = hash;
        this.data = data;
        this.index = index;
        this.timestamp = timestamp;
    }

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}