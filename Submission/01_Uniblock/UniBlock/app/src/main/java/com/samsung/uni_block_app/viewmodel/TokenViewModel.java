package com.samsung.uni_block_app.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.samsung.uni_block_app.services.TokenService;
import com.samsung.uni_block_app.util.Util;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.TransactionResult;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumTokenInfo;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TokenViewModel extends AndroidViewModel {

    private TokenService mTokenService = new TokenService();

    private MutableLiveData<EthereumTokenInfo> tokenInfo = new MutableLiveData<EthereumTokenInfo>();
    private MutableLiveData<String> tokenBalance = new MutableLiveData<String>();

    private MutableLiveData<Boolean> tokenInformationIsLoading = new MutableLiveData<Boolean>();
    private MutableLiveData<Integer> tokenInfoFetchedSuccessfullyFlag = new MutableLiveData<Integer>();
    private MutableLiveData<Integer> tokenBalanceFetchedSuccessfullyFlag = new MutableLiveData<Integer>();
    private MutableLiveData<Integer> tokenAddedSuccessfullyFlag = new MutableLiveData<Integer>();
    private MutableLiveData<Integer> tokenSentSuccessfullyFlag = new MutableLiveData<Integer>();

    private String transactionHash;
    private String errorMessage;

    public TokenViewModel(@NonNull Application application) {
        super(application);
        tokenInformationIsLoading.postValue(false);
    }

    public MutableLiveData<EthereumTokenInfo> getTokenInfo() {
        return tokenInfo;
    }

    public MutableLiveData<Boolean> getTokenInformationIsLoading() {
        return tokenInformationIsLoading;
    }

    public MutableLiveData<Integer> getTokenInfoFetchedSuccessfullyFlag() {
        return tokenInfoFetchedSuccessfullyFlag;
    }

    public MutableLiveData<Integer> getTokenAddedSuccessfullyFlag() {
        return tokenAddedSuccessfullyFlag;
    }

    public MutableLiveData<Integer> getTokenSentSuccessfullyFlag() {
        return tokenSentSuccessfullyFlag;
    }

    public MutableLiveData<String> getTokenBalance() {
        return tokenBalance;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void fetchTokenInformation(String contractAddress) {

        Log.i(Util.LOG_TAG, "Fetching Token Info for Address : " + contractAddress);

        ListenableFutureTask.Callback<EthereumTokenInfo> tokenInformationCallback =
                new ListenableFutureTask.Callback<EthereumTokenInfo>() {
                    @Override
                    public void onSuccess(EthereumTokenInfo tokenInformation) {
                        Log.i(Util.LOG_TAG, "Token Info Fetched Successfully: " + tokenInformation);
                        tokenInfo.postValue(tokenInformation);
                        tokenInfoFetchedSuccessfullyFlag.postValue(Util.POSITIVE_RESPONSE);
                    }

                    @Override
                    public void onFailure(@NotNull ExecutionException e) {
                        Log.e(Util.LOG_TAG, "Getting token info failed");
                        errorMessage = e.getMessage();
                        Util.printErrorMessage(e);
                        tokenInfoFetchedSuccessfullyFlag.postValue(Util.NEGATIVE_RESPONSE);
                    }

                    @Override
                    public void onCancelled(@NotNull InterruptedException e) {
                        Log.e(Util.LOG_TAG, "Getting token info cancelled");
                        errorMessage = e.getMessage();
                        Util.printErrorMessage(e);
                        tokenInfoFetchedSuccessfullyFlag.postValue(Util.NEGATIVE_RESPONSE);
                    }
                };

        mTokenService.getTokenInformation(contractAddress).setCallback(tokenInformationCallback);
    }

    public void getTokenBalance(EthereumAccount selectedAccount) {
        ListenableFutureTask.Callback<BigInteger> tokenBalanceCallback =
                new ListenableFutureTask.Callback<BigInteger>() {
                    @Override
                    public void onSuccess(BigInteger fetchedTokenBalanceRaw) {
                        Log.i(Util.LOG_TAG, "Token Balance is: " + fetchedTokenBalanceRaw);
                        BigDecimal convertedTokenBalance = new BigDecimal(fetchedTokenBalanceRaw).
                                divide(BigDecimal.TEN.pow(tokenInfo.getValue().getDecimals()));
                        tokenBalance.postValue(convertedTokenBalance.toString());
                        tokenBalanceFetchedSuccessfullyFlag.postValue(Util.POSITIVE_RESPONSE);
                        tokenInformationIsLoading.postValue(false);
                    }

                    @Override
                    public void onFailure(@NotNull ExecutionException e) {
                        Log.e(Util.LOG_TAG, "Could not fetch token balance.");
                        Util.printErrorMessage(e);
                        errorMessage = e.getMessage();
                        tokenBalanceFetchedSuccessfullyFlag.postValue(Util.NEGATIVE_RESPONSE);
                        tokenInformationIsLoading.postValue(false);
                    }

                    @Override
                    public void onCancelled(@NotNull InterruptedException e) {
                        Log.e(Util.LOG_TAG, "Fetch token balance cancelled.");
                        Util.printErrorMessage(e);
                        errorMessage = e.getMessage();
                        tokenBalanceFetchedSuccessfullyFlag.postValue(Util.NEGATIVE_RESPONSE);
                        tokenInformationIsLoading.postValue(false);
                    }
                };
        if (selectedAccount != null) {
            mTokenService.getTokenBalance(selectedAccount).setCallback(tokenBalanceCallback);
        } else {
            Log.e(Util.LOG_TAG, "No token account found. Please check if token has been added.");
        }

    }

    public void addTokenToEthereumAccount(EthereumAccount selectedAccount, String contractAddress) {
        ListenableFutureTask.Callback<EthereumAccount> tokenAdditionCallback = new ListenableFutureTask.Callback<EthereumAccount>() {
            @Override
            public void onSuccess(EthereumAccount ethereumAccount) {
                Log.i(Util.LOG_TAG, "Token added successfully!");
                tokenAddedSuccessfullyFlag.postValue(Util.POSITIVE_RESPONSE);
            }

            @Override
            public void onFailure(@NotNull ExecutionException e) {
                Log.e(Util.LOG_TAG, "Adding token failed");
                Util.printErrorMessage(e);
                errorMessage = e.getMessage();
                tokenAddedSuccessfullyFlag.postValue(Util.NEGATIVE_RESPONSE);
            }

            @Override
            public void onCancelled(@NotNull InterruptedException e) {
                Log.e(Util.LOG_TAG, "Adding token cancelled");
                Util.printErrorMessage(e);
            }
        };
        mTokenService.addEthereumToken(selectedAccount, contractAddress).setCallback(tokenAdditionCallback);
    }

    public void sendEthereumToken(EthereumAccount ethereumAccount, String toAddress, String tokenContractAddress, BigDecimal tokenAmount) {

        int tokenDecimals = tokenInfo.getValue().getDecimals();
        BigInteger convertedTokenAmount = tokenAmount.multiply(BigDecimal.TEN.pow(tokenDecimals)).toBigInteger();

        ListenableFutureTask.Callback<TransactionResult> tokenTransactionCallback = new ListenableFutureTask.Callback<TransactionResult>() {
            @Override
            public void onSuccess(TransactionResult transactionResult) {
                transactionHash = transactionResult.getHash();
                Log.i(Util.LOG_TAG, "Token sent successfully!");
                Log.i(Util.LOG_TAG, "Transaction Hash: " + transactionHash);
                tokenSentSuccessfullyFlag.postValue(Util.POSITIVE_RESPONSE);
            }

            @Override
            public void onFailure(@NotNull ExecutionException e) {
                Log.e(Util.LOG_TAG, "Sending token failed");
                Util.printErrorMessage(e);
                errorMessage = e.getMessage();
                tokenSentSuccessfullyFlag.postValue(Util.NEGATIVE_RESPONSE);
            }

            @Override
            public void onCancelled(@NotNull InterruptedException e) {
                Log.e(Util.LOG_TAG, "Sending token cancelled");
                Util.printErrorMessage(e);
                tokenSentSuccessfullyFlag.postValue(Util.NEGATIVE_RESPONSE);
            }
        };

        //Token Can be sent using ethereumService.sendTokenTransaction() or etherumService.sendSmartContractTransaction()

        ListenableFutureTask<TransactionResult> sendEthereumTokenTask =
                mTokenService.sendEthereumToken(ethereumAccount, toAddress, tokenContractAddress, convertedTokenAmount);

//        ListenableFutureTask<TransactionResult> sendEthereumTokenTask =
//                mTokenService.sendEthereumTokenUsingSendSmartContractTransaction(ethereumAccount, toAddress, tokenContractAddress, convertedTokenAmount);

        if (sendEthereumTokenTask != null) {
            sendEthereumTokenTask.setCallback(tokenTransactionCallback);
        } else {
            Log.e(Util.LOG_TAG, "Send Ethereum Token Task could not be build due to Availability Exception");
        }

    }

    public boolean isAddressValid(String tokenAddress) {
        return mTokenService.isAddressValid(tokenAddress);
    }

    public List<String> createTokenAddressList(String address, List<Account> accountList) {
        ArrayList<String> tokenAddressList = new ArrayList<>();
        for (Account currentAccount : accountList) {
            if (currentAccount.getAddress().equals(address)) {
                EthereumAccount ethereumAccount = (EthereumAccount) currentAccount;
                if (ethereumAccount.getTokenAddress() != null)
                    tokenAddressList.add(ethereumAccount.getTokenAddress());
            }
        }
        return tokenAddressList;
    }

}
