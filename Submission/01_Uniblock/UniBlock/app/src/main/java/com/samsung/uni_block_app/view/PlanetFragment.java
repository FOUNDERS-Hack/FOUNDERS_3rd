package com.samsung.uni_block_app.view;


import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.TransactionResult;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.exception.AvailabilityException;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.model.HardwareWalletTypeModel;
import com.samsung.uni_block_app.services.SBPManager;
import com.samsung.uni_block_app.util.Util;
import com.samsung.uni_block_app.viewmodel.AccountInformationViewModel;
import com.samsung.uni_block_app.viewmodel.SetupViewModel;
import com.samsung.uni_block_app.viewmodel.TransactionViewModel;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import org.jetbrains.annotations.NotNull;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlanetFragment extends Fragment {

    private AccountInformationViewModel mAccountInformationViewModel;
    private TransactionViewModel mTransactionViewModel;
    private SetupViewModel mSetupViewModel;

    private ProgressBar balanceProgressBar;
    String name;
    String detail;
    int image;
    double price;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            name = getArguments().getString("name");
            detail = getArguments().getString("detail");
            image = getArguments().getInt("image");
            price = getArguments().getDouble("price");
        }

        //Always pass activity instead of fragment since parent activity stays alive even if fragment is switched.
        mAccountInformationViewModel = ViewModelProviders.of(getActivity()).get(AccountInformationViewModel.class);
        mTransactionViewModel = ViewModelProviders.of(getActivity()).get(TransactionViewModel.class);
        mSetupViewModel = ViewModelProviders.of(getActivity()).get(SetupViewModel.class);
        return inflater.inflate(R.layout.planet_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView balanceTextView = view.findViewById(R.id.balance_textview);
        TextView balanceUnitTextView = view.findViewById(R.id.balance_unit);
        TextView hardwareWalletTextView = view.findViewById(R.id.hardware_wallet_textview);
        TextView inforTextView = view.findViewById(R.id.planetinfor);
        ImageView imageView = view.findViewById(R.id.planetimageView);
        EditText editText = view.findViewById(R.id.auction_edit_text);

//        TextView selectedNetworkTestView = view.findViewById(R.id.network_textview);
//
//        selectedNetworkTestView.setText(mSetupViewModel.getCoinNetworkInfo().getNetworkType().toString());

        ArrayList<HardwareWalletTypeModel> hardWalletTypeModel = mSetupViewModel.getWalletModelList();
        HardwareWalletType hardWalletType = mSetupViewModel.getHardwareWallet().getWalletType();

        if (hardWalletType == hardWalletTypeModel.get(0).getWalletType()) {
            hardwareWalletTextView.setText(hardWalletTypeModel.get(0).getName());
        } else if (hardWalletType == hardWalletTypeModel.get(1).getWalletType()) {
            hardwareWalletTextView.setText(hardWalletTypeModel.get(1).getName());
        } else {
            hardwareWalletTextView.setText(hardWalletTypeModel.get(2).getName());
        }

        balanceTextView.setText(name);
        inforTextView.setText(detail);
        imageView.setImageResource(image);

        balanceUnitTextView.setText(mAccountInformationViewModel.getCoinNetworkInfo().getCoinType().toString());
        balanceProgressBar = view.findViewById(R.id.balance_progressbar);

        Button receiveButton = view.findViewById(R.id.receive_button);
        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFragment(new ReceiveFragment());
            }
        });

        Button sendButton = view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EthereumService ethereumService = (EthereumService) SBPManager.getInstance().getCoinService();
                HardwareWallet hardwareWallet = SBPManager.getInstance().getHardwareWallet();
                MutableLiveData<Account> accounts = mAccountInformationViewModel.getSelectedAccount();
                EthereumAccount account = (EthereumAccount) accounts.getValue();

                List<Type> inputParameters = Arrays.asList(new Utf8String("car"), new Uint256(777));
                List outputParameters = Arrays.asList(
                        new TypeReference<Utf8String>() {
                        }
                );
                Function changePrice = new Function("changePrice", inputParameters, outputParameters);
                String encodedFunction = FunctionEncoder.encode(changePrice);

                try {
                    ethereumService.sendSmartContractTransaction(hardwareWallet, account, "0x43f62a7c82A8c3ca4f36Ef34314dA0E7aE8b18Fb"
                            , BigInteger.valueOf(20000000000L)
                            , BigInteger.valueOf(80000)
                            , encodedFunction
                            , BigInteger.valueOf(Long.parseLong(editText.getText().toString()) * (long) Math.pow(10, 18))
                            , null
                    ).setCallback(new ListenableFutureTask.Callback<TransactionResult>() {
                        @Override
                        public void onSuccess(TransactionResult transactionResult) {
                        }

                        @Override
                        public void onFailure(@NotNull ExecutionException e) {
                        }

                        @Override
                        public void onCancelled(@NotNull InterruptedException e) {
                        }
                    });
                } catch (AvailabilityException e) {
                }
            }
        });
    }


    private void launchFragment(Fragment fragmentToBeLaunched) {
        Log.i(Util.LOG_TAG, "Launching " + fragmentToBeLaunched.getClass().getSimpleName());
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentToBeLaunched).commit();
    }

    private void fetchBalanceRequest(Account account, ImageButton refreshBalanceButton) {
        if (account == null) {
            Log.e(Util.LOG_TAG, "fetchBalanceRequest with null account.");
            return;
        }

        //Refresh Balance
        Log.i(Util.LOG_TAG, "Request Balance Update.");
        UIUtil.toggleProgressBar(View.VISIBLE, balanceProgressBar, refreshBalanceButton);
        //isBalanceProgressBarVisible(true, refreshBalanceButton);
        mTransactionViewModel.fetchBalance(account);
    }
}
