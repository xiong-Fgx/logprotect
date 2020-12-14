package com.xiong.hash_chain;
import com.xiong.hash_chain.util.GetHash;

import java.util.ArrayList;

public class Merkle {
    public static String getMerkleRoot(ArrayList<Transaction> transacitons) {
        int count = transacitons.size();
        ArrayList<String> previousTreeLayer = new ArrayList<String>();
        for(Transaction transaction : transacitons) {
            previousTreeLayer.add(transaction.transactionID);
        }

        ArrayList<String> treeLayer = previousTreeLayer;
        while(count > 1) {
            treeLayer = new ArrayList<String>();
            for(int i = 1; i < previousTreeLayer.size(); i++) {
                treeLayer.add(GetHash.getHashSHA256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }
        String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
        return merkleRoot;
    }
}
