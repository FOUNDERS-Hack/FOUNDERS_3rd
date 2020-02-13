package com.samsung.founders.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.VoidResponse;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button connectBtn;

    private ImageButton callSmartContractBtn;
    private ImageButton sendSmartContractBtn;
    private ImageButton reportBtn;
    private SBlockchain sBlockchain;
    private HardwareWallet wallet;
    private CoinNetworkInfo mCoinNetworkInfo = new CoinNetworkInfo(
            CoinType.ETH,
            EthereumNetworkType.ROPSTEN,
            "https://ropsten.infura.io/v3/70ddb1f89ca9421885b6268e847a459d"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        sBlockchain = new SBlockchain();
        try {
            sBlockchain.initialize(this);
        } catch (SsdkUnsupportedException e) {
            e.printStackTrace();
        }

        connectBtn = findViewById(R.id.connect);
        callSmartContractBtn = findViewById(R.id.call_smart_contract);
        sendSmartContractBtn = findViewById(R.id.send_smart_contract);
        reportBtn = findViewById(R.id.report);

        //this -> implements View.OnClickListener -> onClick
        connectBtn.setOnClickListener(this);
        callSmartContractBtn.setOnClickListener(this);
        sendSmartContractBtn.setOnClickListener(this);
        reportBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connect: {
                connect();
                break;
            }
            case R.id.report: {
                Intent intent = new Intent(getApplicationContext(), Input.class);
                startActivity(intent);
                break;
            }
            case R.id.call_smart_contract: {
                callSmartContract();
                break;
            }
            case R.id.send_smart_contract: {
                try {
                    Intent intent = getIntent();

                    String name = intent.getExtras().getString("NAME");
                    String date = intent.getExtras().getString("DATE");
                    String gen = intent.getExtras().getString("GEN");
                    String job = intent.getExtras().getString("JOB");
                    String etc = intent.getExtras().getString("ETC");

                    sendSmartContract(name, date, gen ,job, etc);
                } catch (AvailabilityException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void connect() {
        sBlockchain.getHardwareWalletManager()
                .connect(HardwareWalletType.SAMSUNG, false)
                .setCallback(new ListenableFutureTask.Callback<HardwareWallet>() {
                    @Override
                    public void onSuccess(final HardwareWallet hardwareWallet) {
                        wallet = hardwareWallet;
                        showToast(hardwareWallet.getWalletId() + " is connected.");
                    }

                    @Override
                    public void onFailure(ExecutionException e) {

                    }

                    @Override
                    public void onCancelled(InterruptedException e) {

                    }
                });
    }

    private void generateAccount() {
        sBlockchain.getAccountManager()
                .generateNewAccount(
                        wallet,
                        mCoinNetworkInfo
                )
                .setCallback(new ListenableFutureTask.Callback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        // for handle MainThread, `runOnUiThread` is used.
                        showToast(account.toString());
                    }

                    @Override
                    public void onFailure(ExecutionException e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onCancelled(InterruptedException e) {

                    }
                });
    }

    private void showToast(String msg) {
        runOnUiThread(
                () -> Toast.makeText(
                        MainActivity.this,
                        msg,
                        Toast.LENGTH_SHORT
                ).show()
        );
    }

    private void getAccounts() {
        List<Account> accounts = sBlockchain.getAccountManager()
                .getAccounts(
                        wallet.getWalletId(),
                        CoinType.ETH,
                        EthereumNetworkType.ROPSTEN
                );

        Toast.makeText(
                MainActivity.this,
                Arrays.toString(new List[]{accounts}),
                Toast.LENGTH_SHORT
        ).show();
    }

    private void paymentSheet() {
        List<Account> accounts = sBlockchain.getAccountManager()
                .getAccounts(
                        wallet.getWalletId(),
                        CoinType.ETH,
                        EthereumNetworkType.ROPSTEN
                );

        EthereumService service =
                (EthereumService) CoinServiceFactory.getCoinService(this, mCoinNetworkInfo);
        Intent intent = service.createEthereumPaymentSheetActivityIntent(
                this,
                wallet,
                (EthereumAccount) accounts.get(0), // from account
                "0xFd23eA99BB179c32e2a23545f3c99e1Cd6a3ea5e", //to address
                new BigInteger("1000000"),
                null,
                null
        );

        // result will back to onActivityResult.
        startActivityForResult(intent, 101);

    }

    //get value from SmartContract
    private void callSmartContract() {
        List<Account> accounts = sBlockchain.getAccountManager()
                .getAccounts(
                        wallet.getWalletId(),
                        CoinType.ETH,
                        EthereumNetworkType.ROPSTEN
                );


        List<TypeReference<?>> outputTypes =
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {},
                        new TypeReference<Utf8String>() {},new TypeReference<Utf8String>(){},new TypeReference<Utf8String>(){});

        Function function = new Function(
                "getReports",
                Collections.<Type>emptyList(),
                outputTypes
        );
        String encodedFunction = FunctionEncoder.encode(function);

        EthereumService service =
                (EthereumService) CoinServiceFactory.getCoinService(this, mCoinNetworkInfo);
        service.callSmartContractFunction(
                (EthereumAccount) accounts.get(0),
                "0x89d03452E1E12435F0688E826f6F651095E6C627", // saved value will be received.
                encodedFunction
        ).setCallback(new ListenableFutureTask.Callback<String>() {
            @Override
            public void onSuccess(final String result) {
                String name = FunctionReturnDecoder
                        .decode(result, function.getOutputParameters())
                        .get(0)
                        .getValue()
                        .toString();
                String date = FunctionReturnDecoder
                        .decode(result, function.getOutputParameters())
                        .get(1)
                        .getValue()
                        .toString();
                String sex = FunctionReturnDecoder
                        .decode(result, function.getOutputParameters())
                        .get(2)
                        .getValue()
                        .toString();
                String company = FunctionReturnDecoder
                        .decode(result, function.getOutputParameters())
                        .get(3)
                        .getValue()
                        .toString();
                String feature = FunctionReturnDecoder
                        .decode(result, function.getOutputParameters())
                        .get(4)
                        .getValue()
                        .toString();

                Intent intent = new Intent(getApplicationContext(), Information.class);

                intent.putExtra("NAME", name);
                intent.putExtra("DATE", date);
                intent.putExtra("GEN", sex);
                intent.putExtra("COM", company);
                intent.putExtra("ETC", feature);

                startActivity(intent);

                //showToast("result = " + name + date + sex + company + feature);
            }

            @Override
            public void onFailure(ExecutionException e) {

            }

            @Override
            public void onCancelled(InterruptedException e) {

            }
        });
    }

    //store '456' to Smart Contract
    private void sendSmartContract(String name, String date, String gen, String job, String etc) throws AvailabilityException {

        List<Account> accounts = sBlockchain.getAccountManager()
                .getAccounts(
                        wallet.getWalletId(),
                        CoinType.ETH,
                        EthereumNetworkType.ROPSTEN
                );



        Utf8String str1 = new Utf8String(name) ;
        Utf8String str2 = new Utf8String(date) ;
        Utf8String str3 = new Utf8String(gen) ;
        Utf8String str4 = new Utf8String(job) ;
        Utf8String str5 = new Utf8String(etc) ;

        List<Type> inputTypes =
                Arrays.<Type>asList(
                        str1,str2,str3,str4,str5
                );

        Function function = new Function(
                "report",
                inputTypes,
                Collections.emptyList()
        );

        String encodedFunction = FunctionEncoder.encode(function);

        EthereumService service =
                (EthereumService) CoinServiceFactory.getCoinService(this, mCoinNetworkInfo);

        service.sendSmartContractTransaction(
                wallet,
                (EthereumAccount) accounts.get(0),
                "0x89d03452E1E12435F0688E826f6F651095E6C627", // contract address
                EthereumUtils.convertGweiToWei(new BigDecimal("10")), // gas price
                new BigInteger("500000"), // gas limit
                encodedFunction, // '456' will be stored to contract
                null,
                null
        ).setCallback(new ListenableFutureTask.Callback<TransactionResult>() {
            @Override
            public void onSuccess(TransactionResult transactionResult) {
                showToast(transactionResult.getHash()); // you can search hash from etherscan.io
            }

            @Override
            public void onFailure(@NotNull ExecutionException e) {
                showToast(e.getMessage());
            }

            @Override
            public void onCancelled(@NotNull InterruptedException e) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Toast.makeText(
                            MainActivity.this,
                            "txid : " + data.getStringExtra("txid"),
                            Toast.LENGTH_SHORT
                    ).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(
                            MainActivity.this,
                            "user canceled",
                            Toast.LENGTH_SHORT
                    ).show();
                    break;
            }
        }
    }
}
