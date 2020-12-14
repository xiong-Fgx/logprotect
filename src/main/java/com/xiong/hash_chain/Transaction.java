package com.xiong.hash_chain;



import com.xiong.hash_chain.util.GetHash;
import com.xiong.hash_chain.util.Key2String;
import com.xiong.hash_chain.util.Sig;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
    public String transactionID;
    public PublicKey sender;
    public PublicKey receiver;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0;

    /*构造函数
    * */
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs){
        this.sender = from;
        this.receiver = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash(){
        sequence++;//sequence是为了避免两个不同的tx中包含相同的内容
        String needHash = Key2String.getStringFromKey(sender)
                + Key2String.getStringFromKey(receiver)
                + Float.toString(value)
                + sequence;
        return GetHash.getHashSHA256(needHash);
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = Key2String.getStringFromKey(sender) + Key2String.getStringFromKey(receiver) + Float.toString(value);
        signature = Sig.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = Key2String.getStringFromKey(sender) + Key2String.getStringFromKey(receiver) + Float.toString(value);
        return Sig.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction() {
        if(verifySignature() == false) {
            System.out.println("signature verified failed");
            return false;
        }

        for(TransactionInput i : inputs) {
            i.UTXO = MyBlockchain.UTXOs.get(i.transactionOutputId);
        }

        if(getInputValue() < MyBlockchain.minimumTX) {
            System.out.println("input too small, and the minimum is " + MyBlockchain.minimumTX);
            return false;
        }

        float leftOver = getInputValue() - value;
        if(leftOver < 0) {
            System.out.println("sender do not have enough money!");
            return false;
        }

        transactionID = calculateHash();
        outputs.add(new TransactionOutput(this.receiver, value, transactionID));
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionID));

        for(TransactionOutput o : outputs) {
            MyBlockchain.UTXOs.put(o.id, o);
        }

        for(TransactionInput i : inputs) {
            if(i.UTXO != null) MyBlockchain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    public float getInputValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            if(i.UTXO != null)
                total += i.UTXO.value;
        }
        return total;
    }

    public float getOutputValue() {
        float total = 0;
        for(TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }

}


