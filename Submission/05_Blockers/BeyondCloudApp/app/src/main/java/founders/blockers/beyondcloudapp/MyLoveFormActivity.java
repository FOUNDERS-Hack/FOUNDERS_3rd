package founders.blockers.beyondcloudapp;

import androidx.appcompat.app.AppCompatActivity;
import founders.blockers.beyondcloudapp.function.FunctionUtils;
import founders.blockers.beyondcloudapp.ui.addBlock.AddBlockFragment;

import android.os.Bundle;
import android.util.Log;
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
import com.samsung.android.sdk.blockchain.coinservice.CoinServiceFactory;
import com.samsung.android.sdk.blockchain.coinservice.TransactionResult;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumUtils;
import com.samsung.android.sdk.blockchain.exception.AvailabilityException;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import org.jetbrains.annotations.NotNull;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyLoveFormActivity extends AppCompatActivity {

    Button saveBtn;

    EditText name;
    EditText lifespan;
    EditText symbol;
    EditText photo;
    EditText content;

    private SBlockchain sBlockchain;
    private HardwareWallet hardwareWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylove_form);

        sBlockchain = new SBlockchain();

        try {
            sBlockchain.initialize(this);
        } catch (SsdkUnsupportedException e) {
            e.printStackTrace();
        }

        connect();

        saveBtn = findViewById(R.id.save_btn);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });


    }

    private void save() {
        name = findViewById(R.id.row_data_name);
        lifespan = findViewById(R.id.row_data_lifespan);
        symbol = findViewById(R.id.row_data_symbol);
        photo = findViewById(R.id.row_data_photo);
        content = findViewById(R.id.row_data_content);

        Function functionGetPostCount = FunctionUtils.createTx(name.getText().toString(),lifespan.getText().toString(),content.getText().toString());
        String savedData = FunctionEncoder.encode(functionGetPostCount);

        Log.d("INPUT SUCCESS", "input : "+savedData);

        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/70ddb1f89ca9421885b6268e847a459d"
        );

        List<Account> accountList = sBlockchain.getAccountManager()
                .getAccounts(hardwareWallet.getWalletId(), CoinType.ETH, EthereumNetworkType.ROPSTEN);

        EthereumService ethereumService = (EthereumService) CoinServiceFactory.getCoinService(this, coinNetworkInfo);

        HardwareWallet connectedHardwareWallet =
                sBlockchain.getHardwareWalletManager().getConnectedHardwareWallet();

        try{
            ethereumService.sendSmartContractTransaction(
                    connectedHardwareWallet,
                    (EthereumAccount) accountList.get(0),
                    "0x4af1b6125cca1b8cb15363aed2cc64c01937a5db",
                    EthereumUtils.convertGweiToWei(new BigDecimal("100")),
                    new BigInteger("500000"),
                    savedData,
                    null,
                    null)
                    .setCallback(new ListenableFutureTask.Callback<TransactionResult>() {
                        @Override
                        public void onSuccess(TransactionResult transactionResult) {
                            Log.d("SUCCESS TAG",  "saved data : "+savedData);

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
        }catch(AvailabilityException e){
            return ;
        }

    }


    private void connect() {
        sBlockchain.getHardwareWalletManager() //cold wallet 리턴
                .connect(HardwareWalletType.SAMSUNG, true)
                .setCallback(new ListenableFutureTask.Callback<HardwareWallet>() { //비동기 (oncreate 안의 함수들이 동작했는지 확인하기 위해! 개별적인 함수 connect, generate, .. 각각의 함수가 성공 후 성공했다고 리턴)
                    @Override
                    public void onSuccess(HardwareWallet w) {
                        hardwareWallet = w; // callback 에서 생성된 hard wallet 사용
                        Log.d("SUCCESS TAG", "connection success");
                        String msg="connection success";

                        // get account from hardware-wallet
                        List<Account> accountList = sBlockchain.getAccountManager()
                                .getAccounts(
                                        hardwareWallet.getWalletId(),
                                        CoinType.ETH,
                                        EthereumNetworkType.ROPSTEN);

                        Log.d("SUCCESS TAG", "account link success");
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
