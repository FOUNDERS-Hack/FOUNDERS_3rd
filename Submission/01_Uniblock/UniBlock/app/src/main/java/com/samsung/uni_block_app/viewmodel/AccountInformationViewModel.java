package com.samsung.uni_block_app.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.samsung.uni_block_app.services.AccountService;
import com.samsung.uni_block_app.services.TransactionService;
import com.samsung.uni_block_app.util.Util;
import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AccountInformationViewModel extends AndroidViewModel {

    private AccountService mAccountService = new AccountService();
    private TransactionService mTransactionService = new TransactionService();

    private MutableLiveData<List<Account>> accountList = new MutableLiveData<List<Account>>();
    private MutableLiveData<List<Account>> filteredAccountList = new MutableLiveData<List<Account>>();
    private MutableLiveData<Account> selectedAccount = new MutableLiveData<Account>();

    private MutableLiveData<Integer> newAccountAddedFlag = new MutableLiveData<Integer>();
    private MutableLiveData<Integer> accountRestoreFlag = new MutableLiveData<>();
    private Exception exception;

    public AccountInformationViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Account> getSelectedAccount() {
        return selectedAccount;
    }

    public MutableLiveData<Integer> getAccountRestoreFlag() {
        return accountRestoreFlag;
    }

    public void setSelectedAccount(Account selectedAccount) {
        this.selectedAccount.postValue(selectedAccount);
    }

    public MutableLiveData<List<Account>> getFilteredAccountList() {
        if (mAccountService.getAccounts().isEmpty()) {
            Log.i(Util.LOG_TAG, "No accounts found! Generating new accounts");
            restoreExistingAccounts();
        } else {
            postAccountLists();
        }
        return filteredAccountList;
    }

    public List<Account> getAllAccounts() {
        return mAccountService.getAccounts();
    }

    private void postAccountLists() {
        List<Account> allAccountsList = mAccountService.getAccounts();
        if (allAccountsList.size() > 0) {
            accountList.postValue(allAccountsList);
            filteredAccountList.postValue(filterAccounts(allAccountsList));
        } else {
            generateNewAccount();
        }

    }

    public MutableLiveData<Integer> getNewAccountAddedFlag() {
        return newAccountAddedFlag;
    }

    public Exception getException() {
        return exception;
    }

    public void restoreExistingAccounts() {
        ListenableFutureTask.Callback<Boolean> restoreExistingAccountCallback = new ListenableFutureTask.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean isRestoredSuccessfully) {
                if (isRestoredSuccessfully) {
                    Log.i(Util.LOG_TAG, "Restored completed successfully!");
                    postAccountLists();
                } else {
                    Log.i(Util.LOG_TAG, "Restoring ended unsuccessfully!");
                }
                accountRestoreFlag.postValue(Util.POSITIVE_RESPONSE);
            }

            @Override
            public void onFailure(@NotNull ExecutionException e) {
                Log.e(Util.LOG_TAG, "Restore Existing Account Failed");
                exception = e;
                Util.printErrorMessage(e);
                accountRestoreFlag.postValue(Util.NEGATIVE_RESPONSE);
            }

            @Override
            public void onCancelled(@NotNull InterruptedException e) {
                Log.e(Util.LOG_TAG, "Restore Existing Account Canceled");
                exception = e;
                Util.printErrorMessage(e);
                accountRestoreFlag.postValue(Util.NEGATIVE_RESPONSE);
            }
        };
        mAccountService.restoreAccounts().setCallback(restoreExistingAccountCallback);
    }

    public void generateNewAccount() {
        ListenableFutureTask.Callback<Account> getAccountListAfterGeneratingNewAccountCallback = new ListenableFutureTask.Callback<Account>() {
            @Override
            public void onSuccess(Account account) {
                Log.i(Util.LOG_TAG, "Generated new account @ path: " + account.getHdPath());
                postAccountLists();
                newAccountAddedFlag.postValue(Util.POSITIVE_RESPONSE);
            }

            @Override
            public void onFailure(@NotNull ExecutionException e) {
                Log.e(Util.LOG_TAG, "Creating new account failed.");
                Log.e(Util.LOG_TAG, "Error is of type: " + e.getClass().getSimpleName());
                exception = e;
                Util.printErrorMessage(e);
                newAccountAddedFlag.postValue(Util.NEGATIVE_RESPONSE);
            }

            @Override
            public void onCancelled(@NotNull InterruptedException e) {
                Log.e(Util.LOG_TAG, "Creating new account cancelled.");
                exception = e;
                Util.printErrorMessage(e);
                newAccountAddedFlag.postValue(Util.NEGATIVE_RESPONSE);
            }
        };
        mAccountService.generateNewAccount().setCallback(getAccountListAfterGeneratingNewAccountCallback);
    }

    private List<Account> filterAccounts(List<Account> unfilteredAccounts) {
        CoinType selectedCoinType = getCoinNetworkInfo().getCoinType();
        if (selectedCoinType.equals(CoinType.ETH)) {
            ArrayList<Account> filteredAccounts = new ArrayList<Account>();
            for (int i = 0; i < unfilteredAccounts.size(); i++) {
                EthereumAccount currentAccount = (EthereumAccount) unfilteredAccounts.get(i);
                if (currentAccount.getTokenAddress() == null) {
                    filteredAccounts.add(currentAccount);
                }
            }
            return filteredAccounts;
        }
        return unfilteredAccounts;
    }

    public CoinNetworkInfo getCoinNetworkInfo() {
        return mAccountService.getCoinNetworkInfo();
    }

    public void setupCoinService(Context applicationContext) {
        mTransactionService.setCoinService(applicationContext);
    }

    public EthereumAccount getTokenAccount(String ethereumAddress, String tokenContractAddress) {
        for (Account account : getAllAccounts()) {
            if (account.getAddress().equals(ethereumAddress)) {
                EthereumAccount ethereumAccount = (EthereumAccount) account;
                if (tokenContractAddress.equals(ethereumAccount.getTokenAddress())) {
                    return ethereumAccount;
                }
            }
        }
        return null;
    }
}
