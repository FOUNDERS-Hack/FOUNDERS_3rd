package com.samsung.uni_block_app.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.samsung.uni_block_app.services.TransactionService;
import com.samsung.uni_block_app.util.Util;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.CoinService;
import com.samsung.android.sdk.blockchain.coinservice.FeeInfo;
import com.samsung.android.sdk.blockchain.coinservice.TransactionResult;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.exception.AvailabilityException;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionService mTransactionService = new TransactionService();
    private String balance;
    private String transactionHash;
    private BigInteger estimatedGasLimit;
    private FeeInfo feeInformation;

    private MutableLiveData<Integer> transactionStatusFlag = new MutableLiveData<>();
    private MutableLiveData<Integer> balanceFetchFlag = new MutableLiveData<>();
    private MutableLiveData<Integer> gasLimitEstimationFlag = new MutableLiveData<>();
    private MutableLiveData<Integer> feeInfoFetchFlag = new MutableLiveData<>();


    public TransactionViewModel(@NonNull Application application) {
        super(application);
    }

    public boolean isAddressValid(String receiversAddress) {
        return mTransactionService.isAddressValid(receiversAddress);
    }

    public CoinService getCoinService() {
        return mTransactionService.getCoinService();
    }

    public EthereumService getEthereumService() {
        return mTransactionService.getEthereumService();
    }

    public String getBalance() {
        return balance;
    }

    public MutableLiveData<Integer> getBalanceFetchFlag() {
        return balanceFetchFlag;
    }

    public MutableLiveData<Integer> getTransactionStatusFlag() {
        return transactionStatusFlag;
    }

    public MutableLiveData<Integer> getGasLimitEstimationFlag() {
        return gasLimitEstimationFlag;
    }

    public MutableLiveData<Integer> getFeeInfoFetchFlag() {
        return feeInfoFetchFlag;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void fetchBalance(Account account) {
        ListenableFutureTask.Callback<BigInteger> balanceFetchCallback = new ListenableFutureTask.Callback<BigInteger>() {
            @Override
            public void onSuccess(BigInteger balanceInBaseUnit) {
                switch (mTransactionService.getCoinNetworkInfo().getCoinType()) {
                    case ETH:
                        BigDecimal balanceInEther = Util.convertToETH(balanceInBaseUnit);
                        //Update Balance
                        balance = balanceInEther.toString();
                        break;
                    default:
                        String msg = "AeroWallet do not support this CoinType";
                        Log.e(Util.LOG_TAG, msg);
                        break;
                }
                Log.d(Util.LOG_TAG, "Balance Fetched successfully.");
                Log.d(Util.LOG_TAG, "Balance is: " + balanceInBaseUnit.toString());
                balanceFetchFlag.postValue(Util.POSITIVE_RESPONSE);
            }

            @Override
            public void onFailure(@NotNull ExecutionException e) {
                Log.e(Util.LOG_TAG, "Balance Fetch Failed.");
                balanceFetchFlag.postValue(Util.NEGATIVE_RESPONSE);
                Util.printErrorMessage(e);
            }

            @Override
            public void onCancelled(@NotNull InterruptedException e) {
                Log.e(Util.LOG_TAG, "Balance Fetch cancelled.");
                balanceFetchFlag.postValue(Util.NEGATIVE_RESPONSE);
                Util.printErrorMessage(e);
            }
        };
        mTransactionService.getBalance(account).setCallback(balanceFetchCallback);
    }

    public void getFeeInformationFromSDK() {
        feeInfoFetchFlag.postValue(Util.NEUTRAL_RESPONSE);

        mTransactionService.getFeeInformation().setCallback(new ListenableFutureTask.Callback<FeeInfo>() {
            @Override
            public void onSuccess(FeeInfo feeInfo) {
                Log.d(Util.LOG_TAG, "Fast fee:" + feeInfo.getFast());
                Log.d(Util.LOG_TAG, "Normal fee:" + feeInfo.getNormal());
                Log.d(Util.LOG_TAG, "Slow fee:" + feeInfo.getSlow());
                feeInformation = feeInfo;

                feeInfoFetchFlag.postValue(Util.POSITIVE_RESPONSE);
            }

            @Override
            public void onFailure(@NotNull ExecutionException e) {
                Log.e(Util.LOG_TAG, "Fetching fee information Failure");
                Util.printErrorMessage(e);
                feeInfoFetchFlag.postValue(Util.NEGATIVE_RESPONSE);
            }

            @Override
            public void onCancelled(@NotNull InterruptedException e) {
                Log.e(Util.LOG_TAG, "Fetching fee information Canceled");
                Util.printErrorMessage(e);
                feeInfoFetchFlag.postValue(Util.NEGATIVE_RESPONSE);
            }
        });
    }

    public void estimateEthereumGasLimit(EthereumAccount account, String toAddress, BigDecimal value) {
        gasLimitEstimationFlag.postValue(Util.NEUTRAL_RESPONSE);
        BigInteger valueInWei = Util.convertToWEI(value);

        mTransactionService.estimateEthereumGasLimit(account, toAddress, valueInWei).setCallback(new ListenableFutureTask.Callback<BigInteger>() {
            @Override
            public void onSuccess(BigInteger estimatedGasLimitValue) {
                Log.d(Util.LOG_TAG, "Estimated gasLimit:" + estimatedGasLimitValue);
                estimatedGasLimit = estimatedGasLimitValue;
                gasLimitEstimationFlag.postValue(Util.POSITIVE_RESPONSE);
            }

            @Override
            public void onFailure(@NotNull ExecutionException e) {
                gasLimitEstimationFlag.postValue(Util.NEGATIVE_RESPONSE);
                Log.e(Util.LOG_TAG, "Gas limit estimation error :" + e.getMessage());
                Util.printErrorMessage(e);
            }

            @Override
            public void onCancelled(@NotNull InterruptedException e) {
                gasLimitEstimationFlag.postValue(Util.NEGATIVE_RESPONSE);
                Log.e(Util.LOG_TAG, "Gas limit estimation error :" + e.getMessage());
                Util.printErrorMessage(e);
            }
        });
    }

    public void sendEthereumTransaction(Account account, int transactionSpeed, String toAddress, BigDecimal amountInETH, String balanceString) {

        transactionStatusFlag.postValue(Util.NEUTRAL_RESPONSE);

        BigInteger gasPrice;
        switch (transactionSpeed) {
            case Util.TRANSACTION_SPEED_SLOW:
                gasPrice = feeInformation.getSlow();
                break;
            case Util.TRANSACTION_SPEED_NORMAL:
                gasPrice = feeInformation.getNormal();
                break;
            case Util.TRANSACTION_SPEED_FAST:
                gasPrice = feeInformation.getFast();
                break;
            default:
                gasPrice = feeInformation.getNormal();
                break;
        }

        BigInteger amountInWEI = Util.convertToWEI(amountInETH);

        // Logging fee info
        BigDecimal totalFeeInEth = Util.convertToETH(gasPrice.multiply(estimatedGasLimit));
        BigDecimal totalAmountInETH = amountInETH.add(totalFeeInEth);
        Log.d(Util.LOG_TAG, "Total Fee for transaction:" + totalFeeInEth);

        // Check if balance is sufficient
        BigDecimal balanceDecimal = new BigDecimal(balanceString);
        if (balanceDecimal.compareTo(totalAmountInETH) < 0) {
            // balance < transferAmount
            Log.e(Util.LOG_TAG, "Insufficient Balance.");
            transactionStatusFlag.postValue(Util.TRANSACTION_INSUFFICIENT_BALANCE);
            return;
        }

        try {
            mTransactionService.sendEthereumTransaction(account, gasPrice, estimatedGasLimit, toAddress, amountInWEI).setCallback(
                    new ListenableFutureTask.Callback<TransactionResult>() {
                        @Override
                        public void onSuccess(TransactionResult transactionResult) {
                            transactionHash = transactionResult.getHash();
                            Log.d(Util.LOG_TAG, "Transaction Hash: " + transactionResult.getHash());
                            if (transactionResult.getError().getCode() != 0) {
                                Log.e(Util.LOG_TAG, "Transaction Error: " + transactionResult.getError());
                            }
                            transactionStatusFlag.postValue(Util.POSITIVE_RESPONSE);
                        }

                        @Override
                        public void onFailure(@NotNull ExecutionException e) {
                            transactionStatusFlag.postValue(Util.NEGATIVE_RESPONSE);
                            Log.e(Util.LOG_TAG, "Transaction Failure");
                            Util.printErrorMessage(e);
                        }

                        @Override
                        public void onCancelled(@NotNull InterruptedException e) {
                            transactionStatusFlag.postValue(Util.NEGATIVE_RESPONSE);
                            Log.e(Util.LOG_TAG, "Transaction Canceled");
                            Util.printErrorMessage(e);
                        }
                    });
        } catch (AvailabilityException e) {
            e.printStackTrace();
        }
    }
}
