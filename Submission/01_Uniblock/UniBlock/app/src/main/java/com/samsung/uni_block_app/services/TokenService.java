package com.samsung.uni_block_app.services;

import android.os.Message;
import android.telecom.Call;
import android.util.Log;

import com.samsung.uni_block_app.util.Util;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.TransactionResult;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumTokenInfo;
import com.samsung.android.sdk.blockchain.exception.AvailabilityException;

import org.jetbrains.annotations.NotNull;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;

public class TokenService {

    private SBPManager mSBPManager = SBPManager.getInstance();
    private EthereumService ethereumService = (EthereumService) SBPManager.getInstance().getCoinService();
    private BigInteger gasPrice = BigInteger.valueOf(20000000000L);
    private BigInteger gasLimit = BigInteger.valueOf(80000);

    public ListenableFutureTask<EthereumTokenInfo> getTokenInformation(String contractAddress) {
        return ethereumService.getTokenInfo(contractAddress);
    }

    public ListenableFutureTask<EthereumAccount> addEthereumToken(EthereumAccount selectedAccount, String contractAddress) {
        Log.i(Util.LOG_TAG, "Token Address to be added is: " + contractAddress);
        return ethereumService.addTokenAddress(selectedAccount, contractAddress);
    }

    public ListenableFutureTask<TransactionResult> sendEthereumToken
            (EthereumAccount ethereumAccount, String toAddress, String tokenContractAddress, BigInteger value) {
        try {
            return ethereumService.sendTokenTransaction
                    (mSBPManager.getHardwareWallet(), ethereumAccount,
                            toAddress, tokenContractAddress, gasPrice, gasLimit, value,
                            null);
        } catch (AvailabilityException e) {
            Log.e(Util.LOG_TAG, "Sending Ethereum Token Failed.");
            Util.printErrorMessage(e);
        }
        return null;
    }

    public ListenableFutureTask<TransactionResult> sendEthereumTokenUsingSendSmartContractTransaction
            (EthereumAccount ethereumAccount, String toAddress, String tokenContractAddress, BigInteger value) {
        List<Type> inputParameters = Arrays.asList(new Address(toAddress), new Uint(value));
        List outputParameters = Arrays.asList(
                new TypeReference<Bool>() {
                }
        );
        Function transferFunction = new Function("transfer", inputParameters, outputParameters);
        String encodedFunction = FunctionEncoder.encode(transferFunction);
        try {
            return ethereumService.sendSmartContractTransaction(mSBPManager.getHardwareWallet(), ethereumAccount, tokenContractAddress, gasPrice,
                    gasLimit, encodedFunction, value, null);
        } catch (AvailabilityException e) {
            Log.e(Util.LOG_TAG, "Sending Ethereum Token Using sendSmartContractTransaction Failed.");
            Util.printErrorMessage(e);
        }
        return null;
    }

    public String  getPrice
            (EthereumAccount ethereumAccount, String tokenContractAddress, String name) {
        List<Type> inputParameters = Arrays.asList(new Utf8String(name));
        List outputParameters = Arrays.asList(
                new TypeReference<Uint256>() {
                }
        );
        Function getPrice = new Function("getPrice", inputParameters, outputParameters);
        String encodedFunction = FunctionEncoder.encode(getPrice);
        final String[] value = new String[1];

        try {
            ethereumService.callSmartContractFunction(ethereumAccount, tokenContractAddress, encodedFunction).setCallback(new ListenableFutureTask.Callback<String>() {
                @Override
                public void onSuccess(String s) {
                    value[0] = s;
                }

                @Override
                public void onFailure(@NotNull ExecutionException e) {
                }

                @Override
                public void onCancelled(@NotNull InterruptedException e) {
                }
            });
            Thread.sleep(2000);
        } finally {
            List<Type> list = FunctionReturnDecoder.decode(value[0], outputParameters);
            return list.get(0).getValue().toString();
        }
    }

    public String  getPlanetList
            (EthereumAccount ethereumAccount, String tokenContractAddress) {
        List<Type> inputParameters = Arrays.asList();
        List outputParameters = Arrays.asList(
                new TypeReference<Utf8String>() {
                }
        );
        Function getPrice = new Function("getPrice", inputParameters, outputParameters);
        String encodedFunction = FunctionEncoder.encode(getPrice);
        final String[] value = new String[1];

        try {
            ethereumService.callSmartContractFunction(ethereumAccount, tokenContractAddress, encodedFunction).setCallback(new ListenableFutureTask.Callback<String>() {
                @Override
                public void onSuccess(String s) {
                    value[0] = s;
                }

                @Override
                public void onFailure(@NotNull ExecutionException e) {
                }

                @Override
                public void onCancelled(@NotNull InterruptedException e) {
                }
            });
            Thread.sleep(2000);
        } finally {
            List<Type> list = FunctionReturnDecoder.decode(value[0], outputParameters);
            return list.get(0).getValue().toString();
        }
    }

    public boolean isAddressValid(String tokenAddress) {
        if (tokenAddress == null) return false;
        else return ethereumService.isValidAddress(tokenAddress);
    }

    public ListenableFutureTask<BigInteger> getTokenBalance(EthereumAccount ethereumAccount) {
        return ethereumService.getTokenBalance(ethereumAccount);
    }

}
