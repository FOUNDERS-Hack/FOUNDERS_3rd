package com.samsung.uni_block_app.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.coinservice.CoinServiceFactory;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;
import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.adapter.AccountAdapter;
import com.samsung.uni_block_app.services.SBPManager;
import com.samsung.uni_block_app.services.SharedPreferenceManager;
import com.samsung.uni_block_app.services.TokenService;
import com.samsung.uni_block_app.util.Util;
import com.samsung.uni_block_app.viewmodel.AccountInformationViewModel;
import com.samsung.uni_block_app.viewmodel.SetupViewModel;
import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.network.NetworkType;

import org.jetbrains.annotations.NotNull;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class NavigationActivity extends AppCompatActivity {

    Spinner accountChooserSpinner;
    BottomNavigationView navView;
    private AccountInformationViewModel mAccountInformationViewModel;
    private SetupViewModel mSetupViewModel;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_marketplace:
                    launchFragment(new MobiDAppMarketplaceFragment());
                    return true;
                case R.id.navigation_dapp:
                    launchFragment(new MobiDAppMarketplaceFragment());
                    return true;
                case R.id.navigation_transfer:
                    launchFragment(new TransactionFragment());
                    return true;
                case R.id.navigation_token:
                    launchFragment(new SendTokenFragment());
                    return true;
                default:
                    launchFragment(new DashboardFragment());
                    return true;
            }
        }
    };

    //Code for hide spinner Dialog
    private static void closeSpinnerDialog(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            Util.printErrorMessage(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountInformationViewModel = ViewModelProviders.of(this).get(AccountInformationViewModel.class);
        mSetupViewModel = ViewModelProviders.of(this).get(SetupViewModel.class);

        setContentView(R.layout.activity_navigation);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Unpack CoinType and NetworkType from bundle
        //Set up coin network info object UI->VM->Service->SBP Manager
        Bundle coinAndNetworkInfoBundle = getIntent().getExtras();
        CoinType selectedCoinType = (CoinType) coinAndNetworkInfoBundle.getSerializable(Util.COIN_TYPE);
        NetworkType selectedNetworkType = (NetworkType) coinAndNetworkInfoBundle.getSerializable(Util.NETWORK_TYPE);

        String rpcString = Util.getRPCString(selectedNetworkType);
        mSetupViewModel.setCoinNetworkInfo(selectedCoinType, selectedNetworkType, rpcString);

        mAccountInformationViewModel.setupCoinService(getApplicationContext());

        //On activity create launch default fragment
        launchFragment(new DashboardFragment());

        //Setup Spinner
        accountChooserSpinner = findViewById(R.id.accountChooserSpinner);

        //mAccountInformationViewModel.getAccountList() returns MutableLiveData<List<Account>>
        mAccountInformationViewModel.getFilteredAccountList().observe(this, filteredAccounts -> {
            Log.i(Util.LOG_TAG, "Received account list. Size: " + filteredAccounts.size());
            hideAccountLoadingProgressBar();
            hideGenerateNewAccountLoadingProgressBar();
            initializeSpinner(accountChooserSpinner, filteredAccounts);
            //Send Alert message new account added
        });

        //Generate New Account Error Flag Observer
        mAccountInformationViewModel.getNewAccountAddedFlag().observe(this, responseValue -> {
            if (responseValue == Util.NEGATIVE_RESPONSE) {
                hideGenerateNewAccountLoadingProgressBar();
                Exception exceptionOccurred = mAccountInformationViewModel.getException();
                if (exceptionOccurred.getMessage().contains("AccountException")) {
                    AlertUtil.showAlertDialog(NavigationActivity.this,
                            Util.removeSDKSignatureFromErrorMessage(exceptionOccurred.getMessage()));
                } else {
                    AlertUtil.showActivityCloseAlertDialog(NavigationActivity.this, Util.CONNECTION_ERROR, true);
                }
            }
            Util.neutralizeConsumedFlag(mAccountInformationViewModel.getNewAccountAddedFlag());
        });

        //Handle Account Restore Result
        mAccountInformationViewModel.getAccountRestoreFlag().observe(this, responseValue -> {
            if (responseValue == Util.NEGATIVE_RESPONSE) {
                hideGenerateNewAccountLoadingProgressBar();
                Exception exceptionOccurred = mAccountInformationViewModel.getException();
                if (exceptionOccurred.getMessage().contains("AccountException")) {
                    AlertUtil.showAlertDialog(NavigationActivity.this,
                            Util.removeSDKSignatureFromErrorMessage(exceptionOccurred.getMessage()));
                } else if (exceptionOccurred.getMessage().contains("ERROR_WALLET_NOT_CREATED")) {
                    SharedPreferenceManager.setValid(false);
                    mSetupViewModel.disconnectHardwareWallet();
                    AlertUtil.showActivityCloseAlertDialog(NavigationActivity.this, Util.WALLET_NOT_SET, true);
                } else {
                    AlertUtil.showActivityCloseAlertDialog(NavigationActivity.this, Util.CONNECTION_ERROR, true);
                }
            }
            Util.neutralizeConsumedFlag(mAccountInformationViewModel.getAccountRestoreFlag());
        });
    }

    private EthereumAccount getDefaultAccountInfo() {
        // getting default account for so that, account change while spinner account is changed.
        Log.i(Util.LOG_TAG, "Getting default account.");
        MutableLiveData<Account> accounts = mAccountInformationViewModel.getSelectedAccount();
        return (EthereumAccount) accounts.getValue();
    }

    private void launchFragment(Fragment fragmentToLaunch) {
        Log.i(Util.LOG_TAG, "Launching " + fragmentToLaunch.getClass().getSimpleName());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentToLaunch).commit();
    }

    private void hideAccountLoadingProgressBar() {
        findViewById(R.id.accountListProgressBar).setVisibility(View.GONE);
    }

    private void initializeSpinner(Spinner accountChooserSpinner, List<Account> accountList) {
        Log.i(Util.LOG_TAG, "Initializing Spinner");

        //Setup spinner for displaying account list
        AccountAdapter spinnerAdapter = new AccountAdapter(this, accountList);
        accountChooserSpinner.setAdapter(spinnerAdapter);

        //Always set the latest created account as the default selected account
        accountChooserSpinner.setSelection(accountList.size() - 1);

        //Account listener for on account selected
        AdapterView.OnItemSelectedListener accountSelectionListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Account selectedAccount = (Account) parent.getItemAtPosition(position);
                Log.i(Util.LOG_TAG, "Selected account at position: " + id);
                Log.i(Util.LOG_TAG, "Selected account with HD Path: " + selectedAccount.getHdPath());

                mAccountInformationViewModel.setSelectedAccount(selectedAccount);
                //refresh list: move selected item to top
                accountList.remove(position);
                //sort remaining accounts based on hdpath index
                accountList.sort(
                        (firstAccount, secondAccount) ->
                                firstAccount.getHdPath().compareTo(secondAccount.getHdPath())
                );
                // set selected Account @ top, shift remaining accounts
                accountList.add(0, selectedAccount);
                spinnerAdapter.notifyDataSetChanged();

                //This code is necessary since "onItemSelected" is only called newly selected position is different from the previously selected position
                accountChooserSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.i(Util.LOG_TAG, "On nothing selected is called");
            }
        };
        accountChooserSpinner.setOnItemSelectedListener(accountSelectionListener);
    }

    //Code related to add new account progressbar UI
    private void hideGenerateNewAccountLoadingProgressBar() {
        findViewById(R.id.generate_new_account_progress_bar).setVisibility(View.GONE);
        findViewById(R.id.generate_new_account_progressbar_parent).setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void showGenerateNewAccountLoadingProgressBar() {
        findViewById(R.id.generate_new_account_progress_bar).setVisibility(View.VISIBLE);
        findViewById(R.id.generate_new_account_progressbar_parent).setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    //Handle Add New account button press
    public void onClickAddNewAccount(View view) {
        // Account Generation: View/Activity > ViewModel > Model/Service > Samsung Blockchain Platform (SBP) SDK
        mAccountInformationViewModel.generateNewAccount();

        //Handling UI while generating account
        try {
            closeSpinnerDialog(accountChooserSpinner);
        } catch (Exception e) {
            Log.e(Util.LOG_TAG, "Could not close spinner dialog");
            Util.printErrorMessage(e);
        }
        showGenerateNewAccountLoadingProgressBar();
    }

    //Code for handling Options Menu Item Selection
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.wallet_setup) {
            DialogInterface.OnClickListener positiveActionListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Wallet Reset, Flagging SharedPreference not valid anymore
                    SharedPreferenceManager.setValid(false);
                    mSetupViewModel.disconnectHardwareWallet();
                    launchWalletSetupActivity();
                }
            };
            DialogInterface.OnClickListener negativeActionListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            };
            AlertUtil.showConfirmationAlertDialog(this, "Do you want ot reset wallet?", R.string.yes, positiveActionListener, R.string.no, negativeActionListener);
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchWalletSetupActivity() {
        Intent launchSelectionActivityIntent = new Intent(this, WalletSelectionActivity.class);
        launchSelectionActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(launchSelectionActivityIntent);
    }

    //Code for launching Default fragment or exiting app on back pressed.
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment.getClass().equals(DashboardFragment.class)) {
            mSetupViewModel.disconnectHardwareWallet();
            super.onBackPressed();
        } else if (currentFragment.getClass().equals(AddTokenFragment.class)) {
            launchFragment(new SendTokenFragment());
            navView.getMenu().getItem(4).setChecked(true);
        } else {
            //Or else on back pressed return to default fragment
            launchFragment(new DashboardFragment());
            navView.getMenu().getItem(0).setChecked(true);
        }
    }

}
