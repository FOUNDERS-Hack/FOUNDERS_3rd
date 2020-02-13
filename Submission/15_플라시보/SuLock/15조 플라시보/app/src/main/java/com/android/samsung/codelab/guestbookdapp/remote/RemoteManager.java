package com.android.samsung.codelab.guestbookdapp.remote;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;

public class RemoteManager {

    private final String BASE_URL = "https://ropsten.infura.io/v3/59e202cb9be04e5c95fa966a97cc3c49";

    private final Web3j web3j;

    public RemoteManager() {
        web3j = Web3j.build(new HttpService(BASE_URL));
    }

    public static RemoteManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public BigInteger getNonce(String address) throws IOException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
        return ethGetTransactionCount.getTransactionCount();
    }

    public BigInteger getBalance(String address) throws IOException {
        EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        return ethGetBalance.getBalance();
    }

    public void sendRawTransaction(String hex) throws Exception {
        EthSendTransaction trx = web3j.ethSendRawTransaction(hex).send();
        if (trx != null) {
            if (trx.hasError()) {
                throw new Exception(trx.getError().getMessage());
            }
        } else {
            throw new Exception("Cannot send transaction");
        }
    }

    public EthCall sendEthCall(Transaction tx) throws Exception {
        return web3j.ethCall(tx, DefaultBlockParameterName.LATEST).send();
    }

    private static class LazyHolder {
        private static final RemoteManager INSTANCE = new RemoteManager();
    }

}
