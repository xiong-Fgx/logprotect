package com.xiong.hash_chain;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class MyBlockchain {
    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
    public static int difficulty = 6;
    public static Wallet walletA;
    public static Wallet walletB;
    public static float minimumTX = 0.01f;
    public static Transaction genesisTransaction;

    /*用于判断当前的区块链是否有异常
    * 使用从头到尾的遍历，依次判断每个节点是否合法
    * */
    public static boolean IsValid() {
        Block previousBlock;
        Block currentBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>();
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
        for(int i = 1; i < blockchain.size(); i++) {
            previousBlock = blockchain.get(i-1);
            currentBlock = blockchain.get(i);

            //当前区块的哈希值与当前区块计算出来的哈希值不相等，表示异常
            if(!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("error! current hash is wrong or data has been modified");
                return false;
            }
            //前一区块的哈希值和当前区块所记录的上衣区块哈希值不相等，表示异常
            if(!previousBlock.hash.equals(currentBlock.previousHash)){
                System.out.println("error! current block records different hash with previous");
                return false;
            }
            //判断是否满足挖矿难度的要求
            if(!currentBlock.hash.substring(0,difficulty).equals(hashTarget)){
                System.out.println("error! current block has not been mined");
                return false;
            }

            TransactionOutput tempOutput;
            for(int t = 0; t < currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on TX " + t + "is invalid");
                    return false;
                }

                if(currentTransaction.getInputValue() != currentTransaction.getOutputValue()) {
                    System.out.println("#Inputs are not equal to outputs on TX " + t);
                    return false;
                }

                for(TransactionInput input : currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if(tempOutput == null) {
                        System.out.println("#Referenced input on TX " + t + " is missing!");
                        return false;
                    }

                    if(input.UTXO.value != tempOutput.value) {
                        System.out.println("#Referenced input on TX " + t + " value is invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }
                for(TransactionOutput output : currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if(currentTransaction.outputs.get(0).reciepient != currentTransaction.receiver) {
                    System.out.println("#Transaction " + t + " reciepient is not who it should be");
                    return false;
                }

                if(currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
                    System.out.println("Transaction " + t + "output change is not sender");
                    return false;
                }
            }
        }
        System.out.println("everything is OK!");
        return true;
    }

    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinbase = new Wallet();

        genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        genesisTransaction.generateSignature(coinbase.privateKey);
        genesisTransaction.transactionID = "0";
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.receiver, genesisTransaction.value, genesisTransaction.transactionID));
        UTXOs.put(genesisTransaction.outputs.get(0).id,genesisTransaction.outputs.get(0));

        System.out.println("creating and mining genesis block...");
        Block genesis = new Block("This is the original block!", "0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        Block block1 = new Block("This is the first block!", genesis.hash);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA send 40 to WalletB");
        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        addBlock(block1);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance() + " WalletB's balance is" + walletB.getBalance());

        Block block2 = new Block("This is the second block!", block1.hash);
        System.out.println("\nWalletA is sending 1000 more than it has");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
        addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance() + " WalletB's balance is" + walletB.getBalance());

        Block block3 = new Block("This is the third block!", block2.hash);
        System.out.println("\nWalletB send 20 to WalletA");
        block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20f));
        System.out.println("\nWalletA's balance is: " + walletA.getBalance() + " WalletB's balance is" + walletB.getBalance());

        IsValid();
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}


