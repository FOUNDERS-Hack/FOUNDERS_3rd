package com.example.brain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.coinservice.CoinServiceFactory;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.ui.CucumberWebView;
import com.samsung.android.sdk.blockchain.ui.OnSendTransactionListener;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity{
    private SBlockchain sb; //블록체인 객체 전역 설정
    private HardwareWallet wallet;
    private Account generatedAccount;
    private List<Account> sbAccounts;
    private CucumberWebView webView;

    private String receiverAddress;
    private int sendEther;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MyApp","START THE APP");

        ////////////////////////////////////////////블록체인 객체
        sb = new SBlockchain();
        try {
            sb.initialize(this);
            Log.d("MyApp","blockchain initialize success");
        } catch (SsdkUnsupportedException e) {
            Log.d("MyApp","initialize failed");
            e.printStackTrace();
        }
        //////////////////////////////////////////객체 initialize
        connect();

        //////////////////기본적으로 앱이 실행되면 하드웨어 지갑에 연결을 하도록 함.
        ///사실 버튼을 눌러서 연결된다고 해도 상관은 없는데 그냥 편의서을 위해서 이렇게 하도록 함.
        //////////////////앱이 실행되었을때 acount가 존재하느지 여부를 확인하는 것임.


        webView = findViewById(R.id.cucumberWebView);
    }
    //////// 자신의 디바이스에 존재하고 있는 하드웨어 지갑에 연결을 시도함
    //////// 삼성의 경우에는 private key 가 keystore에 들어 있음.
    private void connect() {
        sb.getHardwareWalletManager()
                .connect(HardwareWalletType.SAMSUNG,true) //samsung의 private key를 사용한다.
                .setCallback(new ListenableFutureTask.Callback<HardwareWallet>() {
                    @Override
                    public void onSuccess(HardwareWallet hardwareWallet) {
                        Log.d("MyApp","connect success, getting account...");
                        wallet = hardwareWallet; //만약에 연결할 시에 그 지갑을 불러와서 wallet에 저장한다.
                        getAccount();
                    }
                    @Override
                    public void onFailure(@NotNull ExecutionException e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onCancelled(@NotNull InterruptedException e) { e.printStackTrace(); }
                });
    }
    //////////////////////////////////////////////////////////////
    ////////////이제 계정을 만들었는지 확인하기 위한 것으로 GET ACCOUNT라는 함수를 제작할 것임
    private void getAccount() {
        List<Account> accounts = sb.getAccountManager()
                .getAccounts(wallet.getWalletId(),
                        CoinType.ETH,
                        EthereumNetworkType.ROPSTEN //실제 이더리움을 사용하는게 아니니까 ROPSTEN 테스트 넷을 사용함.
                );
        sbAccounts = accounts; //전역 변수에 읽어들인 계정들을 저장한다.
        if(accounts.size() == 0) generate();
        else webViewInit();
        Log.d("MyApp", "Account list : " + Arrays.toString(new List[]{accounts}));

    }
    ////////////////////만약 계정이 없으면 자동으로 계정을 생성하도록 할지,
    private void generate() {
        Log.d("MyApp","account gernerating...");
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/731ebd76ce914e319973c97ede8b5a13"); //Node address
        sb.getAccountManager().generateNewAccount(wallet, coinNetworkInfo)
                .setCallback(new ListenableFutureTask.Callback<com.samsung.android.sdk.blockchain.account.Account>() {
                    @Override
                    public void onSuccess(com.samsung.android.sdk.blockchain.account.Account account) {
                        generatedAccount = account;
                        Log.d("MyApp","generated account : " + account.toString());
                        getAccount();
                    }
                    @Override
                    public void onFailure(@NotNull ExecutionException e) { e.printStackTrace(); }
                    @Override
                    public void onCancelled(@NotNull InterruptedException e) { e.printStackTrace(); }
                });
    }
    //////////////////////// WebView
    private void webViewInit() {
        Log.d("MyApp","initialize webview");
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/731ebd76ce914e319973c97ede8b5a13");
        sbAccounts = sb.getAccountManager().getAccounts(
                wallet.getWalletId(),
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN
        );
        EthereumService ethereumService = (EthereumService) CoinServiceFactory
                .getCoinService(this, coinNetworkInfo);
        MainActivity b = this;
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.init(ethereumService, sbAccounts.get(0), new OnSendTransactionListener() {
                    @Override
                    public void onSendTransaction(@NotNull String requestId, @NotNull EthereumAccount fromAccount, @NotNull String toAddress,
                                                  @Nullable BigInteger value, @Nullable String data, @Nullable BigInteger nonce) {
                        HardwareWallet connectedHardwareWallet =
                                sb.getHardwareWalletManager().getConnectedHardwareWallet();
                        Intent intent =
                                webView.createEthereumPaymentSheetActivityIntent(
                                        b,
                                        requestId,
                                        connectedHardwareWallet,
                                        toAddress,
                                        value,
                                        data,
                                        nonce
                                );
                        startActivityForResult(intent, 0);
                    }
                });
                webView.loadUrl("http://172.20.10.11:3000/"); //웹페이지 주소
            }
        });
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    ///////////////////////
}
