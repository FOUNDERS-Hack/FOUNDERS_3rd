package founders.blockers.beyondcloudapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.ui.OnSendTransactionListener;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity{

    Boolean connectBool = false;
    String connectionMsg;
    String signupMsg;

    Button connectBtn;
    Button loginBtn;
    Button signUpBtn;

    private SBlockchain sBlockchain;
    private HardwareWallet hardwareWallet;
    private Account generatedAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sBlockchain = new SBlockchain();

        try{
            sBlockchain.initialize(this); //블록체인 초기화
        }catch (SsdkUnsupportedException e){
            e.printStackTrace();
        }

        connectBtn = findViewById(R.id.connect_btn);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = connectAndGetAccount();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

        loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if(!connectBool){
                    String msg = "wallet connection failure";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    return ;
                }

                Intent intent = new Intent(getApplicationContext(),MainActivity.class );
                startActivity(intent);
            }
        });

        signUpBtn = findViewById(R.id.signup_btn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectAndSignup();
            }
        });

    }



    private String connectAndGetAccount(){
        sBlockchain.getHardwareWalletManager() //cold wallet 리턴
                .connect(HardwareWalletType.SAMSUNG, true)
                .setCallback(new ListenableFutureTask.Callback<HardwareWallet>() { //비동기 (oncreate 안의 함수들이 동작했는지 확인하기 위해! 개별적인 함수 connect, generate, .. 각각의 함수가 성공 후 성공했다고 리턴)
                    @Override
                    public void onSuccess(HardwareWallet w) {
                        hardwareWallet = w; // callback 에서 생성된 hard wallet 사용
                        Log.d("SUCCESS TAG", "connection success");
                        connectionMsg = "connection success";

                        List<Account> accountList = sBlockchain.getAccountManager()
                                .getAccounts(hardwareWallet.getWalletId(), CoinType.ETH, EthereumNetworkType.ROPSTEN);// parameter String, CoinType, NetworkType 3 중 1개만 입력해도 가능


                        Log.d("SUCCESS TAG MyApp", Arrays.toString(new List[]{accountList}));

                        connectBool = true;
                    }

                    @Override
                    public void onFailure(ExecutionException e) {
                        Log.d("ERROR TAG", e.toString());
                        connectionMsg = "connection failure";
                    }

                    @Override
                    public void onCancelled(InterruptedException e) {
                        Log.d("ERROR TAG", e.toString());
                        connectionMsg = "error";
                    }
                });

        return connectionMsg;

    }

    private void connectAndSignup(){
        sBlockchain.getHardwareWalletManager()
                .connect(HardwareWalletType.SAMSUNG, true)
                .setCallback(new ListenableFutureTask.Callback<HardwareWallet>() {
                    @Override
                    public void onSuccess(HardwareWallet w) {
                        hardwareWallet = w;
                        Log.d("SUCCESS TAG", "connection success");
                        generate();
                    }

                    @Override
                    public void onFailure(ExecutionException e) {
                        Log.d("ERROR TAG", e.toString());
                    }

                    @Override
                    public void onCancelled(InterruptedException e) {
                        Log.d("ERROR TAG", e.toString());
                    }
                });
    }


    private String generate(){

        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/70ddb1f89ca9421885b6268e847a459d"//rpcAddress : 블록체인 상 public node 사용
        );

        sBlockchain.getAccountManager()
                .generateNewAccount(hardwareWallet, coinNetworkInfo)
                .setCallback(new ListenableFutureTask.Callback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        generatedAccount = account;
                        Log.d("SUCCESS TAG MyApp", account.toString());
                        signupMsg = "signup success";
                    }

                    @Override
                    public void onFailure(@NotNull ExecutionException e) {
                        Log.d("ERROR TAG", e.getMessage());
                        signupMsg = "Already generated!";
                    }

                    @Override
                    public void onCancelled(@NotNull InterruptedException e) {
                        signupMsg = "error";
                    }

                });

        return signupMsg;
    }



}
