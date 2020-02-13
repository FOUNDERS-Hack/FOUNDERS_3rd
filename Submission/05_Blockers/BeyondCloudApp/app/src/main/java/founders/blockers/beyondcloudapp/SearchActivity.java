package founders.blockers.beyondcloudapp;

import androidx.appcompat.app.AppCompatActivity;
import founders.blockers.beyondcloudapp.function.FunctionUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import org.jetbrains.annotations.NotNull;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {

    private SBlockchain sBlockchain;
    private HardwareWallet hardwareWallet;

    Button searchBtn;
    ListView searchResult;
    TextView textView;

    ArrayList<String> data;
    ArrayAdapter adapter;

    String dappSmartcontractAddress;
    String smartContractGetfunctionAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();

        searchBtn = findViewById(R.id.search_btn);
        textView = findViewById(R.id.text_bottom);

        sBlockchain = new SBlockchain();

        try {
            sBlockchain.initialize(this);
        } catch (SsdkUnsupportedException e) {
            e.printStackTrace();
        }

        search(); // connect to hardware-wallet through device


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchResult = findViewById(R.id.search_result);
                adapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        data);

                searchResult.setAdapter(adapter);
            }
        });

    }

    /*

    printAll()
    return every transaction data on 'BeyondCloud' dapp block-chain network

     */
    private void printAll() {

        //String infuraAddress = "70ddb1f89ca9421885b6268e847a459d";
        //dappSmartcontractAddress = "0x4af1b6125cca1b8cb15363aed2cc64c01937a5db";
        //smartContractGetfunctionAddress = "0x7355a424";

        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/70ddb1f89ca9421885b6268e847a459d"
        );

        List<Account> accountList = sBlockchain.getAccountManager()
                .getAccounts(hardwareWallet.getWalletId(), CoinType.ETH, EthereumNetworkType.ROPSTEN);

        EthereumService ethereumService = (EthereumService) CoinServiceFactory.getCoinService(this, coinNetworkInfo);


        ethereumService.callSmartContractFunction(
                (EthereumAccount) accountList.get(0),
                "0x4af1b6125cca1b8cb15363aed2cc64c01937a5db",
                "0x7355a424"
        ).setCallback(new ListenableFutureTask.Callback<String>() {
            @Override
            public void onSuccess(String result) { //return hex string

                data = new ArrayList<>();

                Function functionGetPost = FunctionUtils.countTx();

                List<TypeReference<Type>> outputParameters = functionGetPost.getOutputParameters();

                List<Type> types = FunctionReturnDecoder.decode(result, outputParameters);

                Type type = types.get(0);

                BigInteger post = (BigInteger) type.getValue();

                int length = post.intValue();


                for (int i = 0; i < length; i++) {
                    Function Intget = FunctionUtils.InttoHex(i);
                    String res = FunctionEncoder.encode(Intget);

                    ethereumService.callSmartContractFunction(
                            (EthereumAccount) accountList.get(0),
                            "0x4af1b6125cca1b8cb15363aed2cc64c01937a5db",
                            res
                    ).setCallback(new ListenableFutureTask.Callback<String>() {
                        @Override
                        public void onSuccess(String s) {
                            Log.d("SUCCESS TAG",  "account link success : "+s);

                            Function functionGetPost = FunctionUtils.callTx(length);

                            List<TypeReference<Type>> outputParameters = functionGetPost.getOutputParameters();

                            List<Type> types = FunctionReturnDecoder.decode(s, outputParameters);

                            Log.d("SUCCESS TAG", (String) types.get(0).getValue());
                            Log.d("SUCCESS TAG", (String) types.get(1).getValue());
                            Log.d("SUCCESS TAG", (String) types.get(2).getValue());

                            data.add((String) types.get(0).getValue() + "\n" + (String) types.get(1).getValue());

                            // == textBottom
                        }

                        @Override
                        public void onFailure(@NotNull ExecutionException e) {
                            Log.d("ERROR TAG #201", e.toString());
                        }

                        @Override
                        public void onCancelled(@NotNull InterruptedException e) {
                            Log.d("ERROR TAG #202", e.toString());
                        }
                    });
                }
                textView.setText("Loading Completed!");
            }

            @Override
            public void onFailure(@NotNull ExecutionException e) {
                Log.d("ERROR TAG #101", e.toString());
            }

            @Override
            public void onCancelled(@NotNull InterruptedException e) {
                Log.d("ERROR TAG #102", e.toString());
            }
        });


    }


    private void search() {
        sBlockchain.getHardwareWalletManager() //cold wallet 리턴
                .connect(HardwareWalletType.SAMSUNG, true)
                .setCallback(new ListenableFutureTask.Callback<HardwareWallet>() { //비동기 (oncreate 안의 함수들이 동작했는지 확인하기 위해! 개별적인 함수 connect, generate, .. 각각의 함수가 성공 후 성공했다고 리턴)
                    @Override
                    public void onSuccess(HardwareWallet w) {
                        hardwareWallet = w; // callback 에서 생성된 hard wallet 사용
                        Log.d("SUCCESS TAG", "connection success");
                        printAll();

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

}