package com.xiong.hash_chain;
import com.xiong.hash_chain.util.Key2String;
import com.xiong.hash_chain.util.GetHash;

import java.security.PublicKey;

public class TransactionOutput {
    public String id;
    public PublicKey reciepient;
    public float value;
    public String parentTransactionId;

    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId){
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = GetHash.getHashSHA256(Key2String.getStringFromKey(reciepient)
                                            + Float.toString(value)
                                            + parentTransactionId);
    }

    public boolean isMine(PublicKey publicKey) {
        return (publicKey == reciepient);
    }
}
