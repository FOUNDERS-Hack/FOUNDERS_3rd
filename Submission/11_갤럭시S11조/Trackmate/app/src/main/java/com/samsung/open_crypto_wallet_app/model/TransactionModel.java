package com.samsung.open_crypto_wallet_app.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.samsung.open_crypto_wallet_app.BR;

import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;

import java.math.BigInteger;

public class TransactionModel extends BaseObservable {

    private BigInteger nonce;
    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private String toAddress;
    private BigInteger amount;
    private BigInteger racetime;
    private byte[] unsignedTransaction;
    private byte[] signedTransaction;
    private boolean signed;
    private String transactionHash;
    private RawTransaction rawTransaction;

    public byte[] generateUnsignedTransaction() {
        //Encoding Raw transaction to byte[] as per communication protocol set by SBK
        RawTransaction unsignedTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toAddress, amount);

        setUnsignedRawTransaction(unsignedTransaction);

        return TransactionEncoder.encode(unsignedTransaction);
    }

    private void setUnsignedRawTransaction(RawTransaction rawTrx) {
        rawTransaction = rawTrx;
    }
    public RawTransaction getUnsignedRawTransaction() {
        return rawTransaction;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public void setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }

    public void setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public void setRacetime(BigInteger racetime) {this.racetime = racetime; }


    public byte[] getUnsignedTransaction() {
        return unsignedTransaction;
    }

    public void setUnsignedTransaction(byte[] unsignedTransaction) {
        this.unsignedTransaction = unsignedTransaction;
    }

    public byte[] getSignedTransaction() {
        return signedTransaction;
    }

    public void setSignedTransaction(byte[] signedTransaction) {
        this.signedTransaction = signedTransaction;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    @Bindable
    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
        notifyPropertyChanged(BR.signed);
    }
}
