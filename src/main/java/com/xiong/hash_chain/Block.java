package com.xiong.hash_chain;

import com.xiong.hash_chain.util.GetHash;

import java.util.ArrayList;
import java.util.Date;

public class Block {
    /*Block structure:
     * String hash: 当前区块的哈希值，通过前一个区块的哈希值+当前区块的数据计算出来的
     * String previousHash： 前一区块的哈希值
     * String data: 表示该区块中存的内容
     * long timeStamp: 时间戳
     * int nonce: 用来挖矿的一个随机数，每次生成哈希值的时候，这个值都是不同的，从而使得生成的哈希值有变化
     */
    public String hash;
    public String previousHash;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    private String data;
    private long timeStamp;
    private int nonce;

    /* 构造函数
    * 使用这个函数，将需要写的内容data和前一个区块的哈希值一同传给这个构造函数，
    * 将区块链中的一个区块给构造出来。
    * */
    public Block (String data, String previousHash){
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    /* 计算哈希值
    * 需要根据上一个区块的哈希值+时间戳+当前区块的数据来计算出一个SHA256哈希值
    * */
    public String calculateHash() {
        String needHash = previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data + merkleRoot;
        String calculatedHash = GetHash.getHashSHA256(needHash);
        return calculatedHash;
    }

    /* 挖矿
    * 挖矿的含义就是提供一个挖矿的难度变量difficulty，表示生成的哈希值前面有多少位为0
    * 挖矿的方法就是不断的生成哈希值，因为SHA-256算法中有随机数，因此每次生成的哈希值都是不相同的，
    * 只有满足前difficulty位都是0，才象征着挖矿结束，得到一个合理的区块。
    * */
    public void mineBlock(int difficulty) {
        this.merkleRoot = Merkle.getMerkleRoot(transactions);
        String target = new String(new char[difficulty]).replace('\0', '0');
        while(!hash.substring(0,difficulty).equals(target)){
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined! : " + hash);
    }

    public boolean addTransaction(Transaction transaction) {
        if(transaction == null)return false;
        if(previousHash != "0") {
            if(!transaction.processTransaction()){
                System.out.println("Tx failed to process");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Tx added to Block successfully!");
        return true;
    }
}
