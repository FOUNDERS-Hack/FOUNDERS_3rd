package com.samsung.uni_block_app.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.adapter.ProductAdapter;
import com.samsung.uni_block_app.model.ProductModel;
import com.samsung.uni_block_app.services.TokenService;
import com.samsung.uni_block_app.util.Util;
import com.samsung.uni_block_app.viewmodel.AccountInformationViewModel;
import com.samsung.uni_block_app.viewmodel.PaymentViewModel;
import com.samsung.uni_block_app.viewmodel.ProductViewModel;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;

import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MobiDAppMarketplaceFragment extends Fragment {

    private ProductViewModel mProductViewModel;
    private AccountInformationViewModel mAccountInformationViewModel;
    private PaymentViewModel mPaymentViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductViewModel = ViewModelProviders.of(getActivity()).get(ProductViewModel.class);
        mPaymentViewModel = ViewModelProviders.of(getActivity()).get(PaymentViewModel.class);
        mAccountInformationViewModel = ViewModelProviders.of(getActivity()).get(AccountInformationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mobi_marketplace, container, false);

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        Log.i(Util.LOG_TAG, "Getting product list from viewModel.");
        ArrayList<ProductModel> productList = mProductViewModel.getPlanetList(getDefaultAccountInfo(), "0x054fF5ce3aC2D2B3DC42a348a0fd48f8FB13b928", "car");

        RecyclerView productRecyclerView = getView().findViewById(R.id.product_recyclerview);

        // Create a LinearLayoutManager.
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        productRecyclerView.setLayoutManager(mLayoutManager);

        ProductAdapter.OnItemClickListener productClickListener = new ProductAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ProductModel productModel, View productView) {

                PlanetFragment fragment = new PlanetFragment();

                Bundle bundle = new Bundle();
                bundle.putString("name", productModel.getProductName());
                bundle.putInt("image", productModel.getProductImage());
                bundle.putString("detail", productModel.getProductDetails());
                bundle.putDouble("price", productModel.getProductPrice());
                fragment.setArguments(bundle);

                launchFragment(fragment);
//
//
//                String toAddress = productModel.getSellerAddress();
//                // Converting Ether to Wei.
//                BigInteger productPrice = Convert.toWei("" + productModel.getProductPrice(), Convert.Unit.ETHER).toBigInteger();
//
//                //checking network connection.
//                Log.d(Util.LOG_TAG, "Checking Network connection");
//                    Log.i(Util.LOG_TAG, "Calling payment sheet intent for Mobile Dapp.");
//                    if(getDefaultAccountInfo() == null){
//                        Log.e(Util.LOG_TAG, "Selected account value is null.");
//                        Toast.makeText(getContext(), "Loading Accounts.Please Wait...", Toast.LENGTH_SHORT).show();
//                    }else if(toAddress == null){
//                        Log.e(Util.LOG_TAG, "Receiver account value not found.");
//                    }else if(productPrice == null){
//                        Log.e(Util.LOG_TAG, "Product amount not found.");
//                    } else{
//                        Intent mobileDappPaymentSheetIntent = mPaymentViewModel.getDAppPaymentIntent(getDefaultAccountInfo(), toAddress, productPrice, null, null);
//                        //Launching payment sheet intent
//                        Log.i(Util.LOG_TAG, "Launching Payment Sheet Intent for Mobile Dapp.");
//                        startActivityForResult(mobileDappPaymentSheetIntent, 0);
//                    }
            }
        };

        RecyclerView.Adapter productRecyclerAdapter = new ProductAdapter(productList, productClickListener);

        // Setting Adapter
        productRecyclerView.setAdapter(productRecyclerAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    String transactionID = data.getStringExtra("txid");
                    Log.d(Util.LOG_TAG, "TransactionId : " + transactionID);
                    postDappTransactionSuccessFunctions(transactionID);
                    break;
                case Activity.RESULT_CANCELED:
                    Log.d(Util.LOG_TAG, "Transaction canceled by user.");
                    AlertUtil.showAlertDialog(getActivity(), "Transaction canceled.");
                    break;
            }
        }
    }

    private void postDappTransactionSuccessFunctions(String transactionID) {
        DialogInterface.OnClickListener positiveActionListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };
        DialogInterface.OnClickListener negativeActionListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Transaction Hash", transactionID);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(), "Transaction Hash Copied", Toast.LENGTH_SHORT).show();
            }
        };
        AlertUtil.showConfirmationAlertDialog(getActivity(), "Transaction Successful. Transaction Hash: " + transactionID, R.string.close, positiveActionListener, R.string.copy_to_clipboard, negativeActionListener);
    }

    private EthereumAccount getDefaultAccountInfo() {
        // getting default account for so that, account change while spinner account is changed.
        Log.i(Util.LOG_TAG, "Getting default account.");
        MutableLiveData<Account> accounts = mAccountInformationViewModel.getSelectedAccount();
        return (EthereumAccount) accounts.getValue();
    }

    private void launchFragment(Fragment fragmentToBeLaunched) {
        Log.i(Util.LOG_TAG, "Launching " + fragmentToBeLaunched.getClass().getSimpleName());
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentToBeLaunched).commit();
    }
}