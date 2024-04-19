package com.example.blockchainprac;

import android.util.Log;

public class Block {
    private int index;
    private long timestamp;
    private long initialTimestamp;
    private String data;
    private String previousHash;
    private String hash;
    private int nonce;

    public Block(int index, long timestamp, String data, String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.initialTimestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();
        this.nonce = 0;
    }

    // Calculate the hash of the block based on its properties
    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                        Long.toString(initialTimestamp) + // Use initial timestamp
                        Integer.toString(nonce) +
                        data
        );
    }

    // Mine the block by finding a hash that meets a certain difficulty level
    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        Log.d("BLOCK MINED", "mineBlock: "+hash);
    }

    // Getters for block properties
    public int getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getData() {
        return data;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }
}
