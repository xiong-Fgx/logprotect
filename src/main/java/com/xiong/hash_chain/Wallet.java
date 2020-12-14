package com.xiong.hash_chain;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* 生成公私钥对
* 该类的作用是生成一个公私钥对
* */
public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

    public HashMap<String, TransactionOutput> UTXOS = new HashMap<String, TransactionOutput>();

    public Wallet() {
        generateKeys();
    }

    public void generateKeys() {
        try{
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public float getBalance() {
        float total = 0;
        for(Map.Entry<String, TransactionOutput> item : MyBlockchain.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) {
                UTXOS.put(UTXO.id, UTXO);
                total += UTXO.value;
            }
        }
        return total;
    }

    public Transaction sendFunds(PublicKey _recipient, float value) {
        if(getBalance() < value) {
            System.out.println("not enough fund");
            return null;
        }
        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

        float total = 0;
        for(Map.Entry<String, TransactionOutput> item : UTXOS.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);

        newTransaction.generateSignature(privateKey);

        for(TransactionInput input : inputs) {
            UTXOS.remove(input.transactionOutputId);
        }

        return newTransaction;
    }
}
