package com.android.samsung.codelab.guestbookdapp.presenter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.android.samsung.codelab.guestbookdapp.contract.WriteFeedContract;
import com.android.samsung.codelab.guestbookdapp.ethereum.FunctionUtil;
import com.android.samsung.codelab.guestbookdapp.model.Feed;
import com.android.samsung.codelab.guestbookdapp.model.UserInfo;
import com.android.samsung.codelab.guestbookdapp.remote.RemoteManager;
import com.android.samsung.codelab.guestbookdapp.util.AppExecutors;
import com.samsung.android.sdk.coldwallet.ScwCoinType;
import com.samsung.android.sdk.coldwallet.ScwService;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class WriteFeedPresenter implements WriteFeedContract.PresenterContract {

    private static final String TAG = WriteFeedPresenter.class.getSimpleName();
    private WriteFeedContract.ViewContract contract;

    public WriteFeedPresenter(WriteFeedContract.ViewContract contract) {
        this.contract = contract;
    }

    //수정**********************************
    @Override
    public void actionSend() {
        Feed feed = UserInfo.getInstance().getFeedToWrite();
        if (TextUtils.isEmpty(feed.getArea())
                || TextUtils.isEmpty(feed.getBname())
                || TextUtils.isEmpty(feed.getPh())
                || TextUtils.isEmpty(feed.getBod())
                || TextUtils.isEmpty(feed.getCod())
                || TextUtils.isEmpty(feed.getToc())) {
            contract.toastMessage("Please fill in all fields.");
            return;
        }
        contract.setLoadingProgress(true);
        //수정**********************************
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss", Locale.US);
//        String date = dateFormat.format(System.currentTimeMillis());
//        UserInfo.getInstance().getFeedToWrite().setDate(date);

        AppExecutors.getInstance().networkIO().execute(() -> {

            BigInteger nonce = getNonce();


            // TODO : Make post comment Raw Transaction (Live code)
            // make unsigned tx by Web3j TransactionEncoder
//
              RawTransaction tx = createPostTransaction(nonce);
              byte[] unsignedTx = TransactionEncoder.encode(tx);
            signTransaction(unsignedTx, (success, message) -> {
                if (success) {
                    contract.toastMessage("Success to post your comment");
                } else {
                    contract.toastMessage("Fail to post your comment.");
                }
                contract.setLoadingProgress(false);
                contract.finishActivity();
            });

        });

    }
    //수정**********************************
//    public void changeEmoji() {
//        contract.setEmojiBottomSheet();
//    }

    private RawTransaction createPostTransaction(BigInteger nonce) {
        Feed feed = UserInfo.getInstance().getFeedToWrite();
        // TODO : Make Web3j Function to call Post Smart contract call (Live code)
        // Encode function to HEX String

        //수정***************************
        Function func = new Function("post"
                , Arrays.asList(
                new Utf8String(feed.getArea())
                , new Utf8String(feed.getBname())
                , new Utf8String(feed.getPh())
                , new Utf8String(feed.getBod())
                , new Utf8String(feed.getCod())
                , new Utf8String(feed.getToc()))
                , Collections.emptyList());  //나는 return을 받지 않겠다.
        //솔리디티의 변수들을 받기 위해 이런 객체를 만들어 놓는다 aslist에 솔리디티의 변수 4개를 넣을 공간을 설정한다
        String data = FunctionEncoder.encode(func);
        Log.d("ExampleCode", "encoded func : " + data);
        return RawTransaction.createTransaction(
                nonce
                , BigInteger.valueOf(3_000_000_000L)   //임의의 가스 limit
                , BigInteger.valueOf(1_000_000L)     //임의의 가스 limit
                , FunctionUtil.CONTRACT_ADDRESS
                , data);
        //여기까지 하면 post하는 기능을 가지게 된다.
    }

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
        //Update Listener call > listener.transactionDidFinish(result, "");
        String hdPath = ScwService.getHdPath(ScwCoinType.ETH,0);
        ScwService.getInstance().signEthTransaction(new ScwService.ScwSignEthTransactionCallback() {
            @Override
            public void onSuccess(byte[] bytes) {
                listener.transactionDidFinish(true,"");
                sendSignedTransaction(bytes);
            }

            @Override
            public void onFailure(int i, @Nullable String s) {
                listener.transactionDidFinish(false,"");
            }
        },unsignedTx, hdPath);
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
