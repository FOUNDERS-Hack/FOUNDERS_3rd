package founders.blockers.beyondcloudapp.ui.memories;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import founders.blockers.beyondcloudapp.R;
import founders.blockers.beyondcloudapp.function.FunctionUtils;

public class MemoriesFragment extends Fragment {

    Button searchBtn;
    EditText result;
    TextView _name;
    TextView _birth;
    TextView _will;
    ImageView image;
    String ahn;
    String jackson;
    String kobe;
    String editResult;

    private SBlockchain sBlockchain;
    private HardwareWallet hardwareWallet;

    private String to;

    private MemoriesViewModel memoriesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        memoriesViewModel =
                ViewModelProviders.of(this).get(MemoriesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_memories, container, false);


        sBlockchain = new SBlockchain();
        image=root.findViewById(R.id.imageInputData);
        ahn="안중근";
        jackson="마이클잭슨";
        kobe="코비 브라이언트";

        try {
            sBlockchain.initialize(getContext());
        } catch (SsdkUnsupportedException e) {
            e.printStackTrace();
        }
        image.setImageResource(R.drawable.qrcode);

        _name=root.findViewById(R.id.name_field);
        _birth=root.findViewById(R.id.birth_field);
        _will=root.findViewById(R.id.will_field);
        searchBtn=root.findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result=root.findViewById(R.id.nameGet);
                editResult=result.getText().toString();

                printOne(editResult);
                if (editResult.equals(ahn)){
                    image.setImageResource(R.drawable.ahn);
                }else if(editResult.equals(jackson)){
                    Log.d("same","same");
                    image.setImageResource(R.drawable.jackson);
                }else if(editResult.equals(kobe)){
                    image.setImageResource(R.drawable.kobe);
                }
            }
        });


        return root;
    }


    private void printOne(String r){

        sBlockchain.getHardwareWalletManager()
                .connect(HardwareWalletType.SAMSUNG, true)
                .setCallback(new ListenableFutureTask.Callback<HardwareWallet>() {
                    @Override
                    public void onSuccess(HardwareWallet w) {
                        hardwareWallet = w;
                        Log.d("SUCCESS TAG", "connection success" );

                        CoinNetworkInfo coinNetworkInfo;
                        coinNetworkInfo = new CoinNetworkInfo(
                                CoinType.ETH,
                                EthereumNetworkType.ROPSTEN,
                                "https://ropsten.infura.io/v3/70ddb1f89ca9421885b6268e847a459d"
                        );

                        List<Account> accountList = sBlockchain.getAccountManager()
                                .getAccounts(hardwareWallet.getWalletId(),
                                        CoinType.ETH,
                                        EthereumNetworkType.ROPSTEN);

                        EthereumService ethereumService = (EthereumService) CoinServiceFactory.getCoinService(getContext(), coinNetworkInfo);
                        Function functionGetPost = FunctionUtils.StringtoHex(r);
                        String data = FunctionEncoder.encode(functionGetPost);
                        data=data.substring(10);

                        ethereumService.callSmartContractFunction(
                                (EthereumAccount) accountList.get(0),
                                "0x4af1b6125cca1b8cb15363aed2cc64c01937a5db",
                                "0xb336ad83"+data
                        ).setCallback(new ListenableFutureTask.Callback<String>() {
                            @Override
                            public void onSuccess(String s) {
                                Log.d("SUCCESS TAG", "get data : "+s );
                                Function functionGetPost = FunctionUtils.callBlockByName(s);
                                List<TypeReference<Type>> outputParameters = functionGetPost.getOutputParameters();
                                List<Type> types = FunctionReturnDecoder.decode(s, outputParameters);

                                Log.d("hi",(String)types.get(0).getValue());
                                Log.d("hi",(String)types.get(1).getValue());
                                Log.d("hi",(String)types.get(2).getValue());

                                //확인차 log값
                                _name.setText((String)types.get(0).getValue());
                                _birth.setText((String)types.get(1).getValue());
                                _will.setText((String)types.get(2).getValue());

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

                    @Override
                    public void onFailure(@NotNull ExecutionException e) {
                        Log.d("ERROR TAG #301", e.toString());
                    }

                    @Override
                    public void onCancelled(@NotNull InterruptedException e) {
                        Log.d("ERROR TAG #302", e.toString());
                    }
                });
    }
}