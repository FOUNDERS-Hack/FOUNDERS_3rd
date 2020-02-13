package com.samsung.open_crypto_wallet_app;

import android.content.Context;
import android.util.Log;

import com.samsung.open_crypto_wallet_app.view_model.AccountViewModel;
import com.samsung.open_crypto_wallet_app.view_model.TransactionViewModel;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;

import java8.util.concurrent.CompletableFuture;

public class NodeConnector {

    public static final String ROPSTEN = "ropsten";
    public static final String KOVAN = "kovan";
    public static final String MAINNET = "mainnet";

    private static NodeConnector nodeConnector;
    private Web3j web3jNode;

    private Context mContext;

    private NodeConnector(String defaultNetwork) {
        String infura_endpoint_url = "https://" + defaultNetwork + ".infura.io/v3/20793494ad6241b183e4e59bca0efb37"; // HardCoded, As reaching Application resources requires Context
        web3jNode = Web3j.build(new HttpService(infura_endpoint_url));
        Log.i(Util.LOG_TAG, "Web3j node object created.");

        //Code for checking whether connection has been established.
        CompletableFuture<Web3ClientVersion> web3clientCompletableFuture = web3jNode.web3ClientVersion().sendAsync();
        web3clientCompletableFuture.thenApply(web3ClientVersion -> {
            Log.i(Util.LOG_TAG, "Node Connection Established, Web Client Version: " + web3ClientVersion.getWeb3ClientVersion());
            return web3ClientVersion;
        });
    }

    public static void reCreateNodeConnector(Context context) {
        nodeConnector = new NodeConnector(SharedPreferenceManager.getDefaultNetwork(context));
    }

    private void setContext(Context context) {
        mContext = context;
    }

    public static NodeConnector getInstance(Context context) {
        if (nodeConnector == null) {
            reCreateNodeConnector(context);
        } else {
            nodeConnector.setContext(context);
        }
        return nodeConnector;
    }

    public void getBalance(String publicAddress) {
        // TODO : Get Balance with Web3j (Live code)

        CompletableFuture<EthGetBalance> ethGetBalanceCompletableFuture;
        ethGetBalanceCompletableFuture = web3jNode.ethGetBalance(publicAddress, DefaultBlockParameterName.LATEST).sendAsync();
        //sync call이 들어오면 thenApply에 있는 func이 call back된다

        ethGetBalanceCompletableFuture.thenApply((ethGetBalance) -> {
            // Balance will be set once the data is fetched
            // Wei X 10^18 = Eth
            BigDecimal balanceInWei = new BigDecimal(ethGetBalance.getBalance());
            BigDecimal balanceInEther = balanceInWei.divide(new BigDecimal(BigInteger.TEN.pow(18)));
            String fetchedBalance = balanceInEther.toString();
            Log.i(Util.LOG_TAG, "Fetched Balance: " + fetchedBalance);
            AccountViewModel.setBalance(fetchedBalance);
            return ethGetBalance;       //dummy return
        });

        /*
        CompletableFuture<EthGetBalance> ethGetBalanceCompletableFuture;

        ethGetBalanceCompletableFuture = web3jNode.ethGetBalance(publicAddress, DefaultBlockParameterName.LATEST).sendAsync();

        ethGetBalanceCompletableFuture.thenApply(ethGetBalance -> {
            // Balance will be set once the data is fetched
            BigDecimal balanceInWei = new BigDecimal(ethGetBalance.getBalance());
            BigDecimal balanceInEther = balanceInWei.divide(new BigDecimal(BigInteger.TEN.pow(18)));
            String fetchedBalance = balanceInEther.toString();
            Log.i(Util.LOG_TAG, "Fetched Balance: " + fetchedBalance);
            AccountViewModel.setBalance(fetchedBalance);
            return ethGetBalance;       //dummy return
        });
         */
    }

    //Call shutdown to free resource
    public void shutDown() {
        Log.i(Util.LOG_TAG, "Shutting down Etherum Node Connection");
        web3jNode.shutdown();
    }

    public CompletableFuture<EthGetTransactionCount> getNonceRequest(String address) {
        CompletableFuture<EthGetTransactionCount> nonceRequest;
        nonceRequest = web3jNode.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).sendAsync();
        return nonceRequest;
    }

    public void sendTransaction(byte[] signedTransaction) {
        // TODO : Send Transaction with Web3j(Live code)

        String transactionToSend = Numeric.toHexString(signedTransaction);
        Log.d("KeyStoreManager", "Singed tx to send: " + transactionToSend);

        CompletableFuture<EthSendTransaction> transactionRequest = web3jNode.ethSendRawTransaction(transactionToSend).sendAsync();
        transactionRequest.thenApply(ethSendTransaction -> {
            if (ethSendTransaction.hasError()) {
                Log.e(Util.LOG_TAG, "Sending Transaction Failed with error code: " + ethSendTransaction.getError().getCode());
                Log.e(Util.LOG_TAG, "Sending Transaction Failed with error: " + ethSendTransaction.getError().getMessage());
            } else {
                Log.i(Util.LOG_TAG, "Hash: " + ethSendTransaction.getTransactionHash());
                Log.d("KeyStoreManager", "txHash : " + ethSendTransaction.getTransactionHash());
            }
            TransactionViewModel.setTransactionHash(ethSendTransaction.getTransactionHash());
            return signedTransaction;           //dummy return
        });
    }

}
