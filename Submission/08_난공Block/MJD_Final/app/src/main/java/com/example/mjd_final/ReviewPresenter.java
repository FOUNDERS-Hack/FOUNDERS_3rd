package com.example.mjd_final;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mjd_final.contract.ReviewContract;
import com.example.mjd_final.ethereum.FunctionUtil;
import com.example.mjd_final.model.Review;
import com.example.mjd_final.model.UserInfo;
import com.example.mjd_final.remote.RemoteManager;
import com.example.mjd_final.util.AppExecutors;
import com.samsung.android.sdk.coldwallet.ScwCoinType;
import com.samsung.android.sdk.coldwallet.ScwService;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.RawTransaction;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class ReviewPresenter {

    private static final String TAG = ReviewPresenter.class.getSimpleName();
    private ReviewContract.ViewContract contract;

    public ReviewPresenter(ReviewContract.ViewContract contract) {
        this.contract = contract;
    }

    public void giveAuth(String userId, String storeName) {

    }

    public void writeReview(String userId, String storeName, String comment){
        //contract.setLoadingProcess(true);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss", Locale.US);
        String date = dateFormat.format(System.currentTimeMillis());

        AppExecutors.getInstance().networkIO().execute(() -> {
            //BigInteger nonce = getNocnce();


        });
    }

    public void voteReview(String userId, int reviewId, boolean approve){

    }

    /*private RawTransaction createPostTransaction(BigInteger nonce) {
        Review review = UserInfo.getInstance().getReviewToWrite();
        // TODO : Make Web3j Function to call Post Smart contract call (Live code)
        // Encode function to HEX String

        Function func = new Function("post"
                , Arrays.asList(
                new Utf8String(feed.getName())
                , new Utf8String(feed.getComment())
                , new Utf8String(feed.getDate())
                , new Utf8String(feed.getEmoji()))
                , Collections.emptyList());

        String data = FunctionEncoder.encode(func);
        Log.d("ExampleCode", "encoded func: " + data);

        return RawTransaction.createTransaction(
                nonce
                , BigInteger.valueOf(3_000_000_000L)
                , BigInteger.valueOf(1_000_000L)
                , FunctionUtil.CONTRACT_ADDRESS
                , data);

    }*/

    private BigInteger getNonce() {
        String address = UserInfo.getInstance().getAddress();
        BigInteger nonce;
        try {
            nonce = RemoteManager.getInstance().getNonce(address);
        } catch (Exception e) {
            nonce = BigInteger.ZERO;
        }

        return nonce;

    }

    private void signTransaction(byte[] unsignedTx, SignTransactionListener listener) {

        // TODO : Sign the transaction with Samsung blockchain keystore
        // Success, Send Eth Transaction > sendSignedTransaction(signedTransaction) and Update listener
        // fail, update listener
        // Update Listener call > listener.transactionDidFinish(result, "");
        String hdPath = ScwService.getHdPath(ScwCoinType.ETH, 0);

        ScwService.getInstance().signEthTransaction(new ScwService.ScwSignEthTransactionCallback() {
            @Override
            public void onSuccess(byte[] bytes) {
                listener.transactionDidFinish(true, "");
                sendSignedTransaction(bytes);
            }

            @Override
            public void onFailure(int i, @Nullable String s) {
                listener.transactionDidFinish(false, "");
            }
        }, unsignedTx, hdPath);

    }

    private boolean sendSignedTransaction(byte[] bytes) {
        String hex = Numeric.toHexString(bytes);
        try {
            RemoteManager.getInstance().sendRawTransaction(hex);
            return true;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            return false;
        }

    }

    interface SignTransactionListener {
        void transactionDidFinish(boolean success, String message);
    }


}
