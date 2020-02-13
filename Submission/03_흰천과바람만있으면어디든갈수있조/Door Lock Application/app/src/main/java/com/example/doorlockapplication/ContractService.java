package com.example.doorlockapplication;

import android.util.Log;

import androidx.annotation.Nullable;

import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumTransaction;
import com.samsung.android.sdk.coldwallet.ScwCoinType;
import com.samsung.android.sdk.coldwallet.ScwService;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java8.util.concurrent.CompletableFuture;

import static java.util.Collections.singletonList;

public class ContractService {
//    public static final String CONTRACT_ADDRESS = "0x5dFcDc09966250F5ec678d1D28695211CEd672bb";

    public static final String CONTRACT_ADDRESS = "0xcB5Ff765Be4CC90A74063e39a828D7fA3A353bD8";

    public static Function createGetPostCountSmartContractCall() {
        return new Function("openDoor"
                , Collections.emptyList()
                , singletonList(new TypeReference<Bool>() {
        }));
    }

    public static void openDoor(String address, String contractAddress, String data) {
        try {
            Function func = createGetPostCountSmartContractCall();

            Transaction tx = Transaction.createEthCallTransaction(address, contractAddress, data);

            EthCall result = RemoteManager.getInstance().sendEthCall(tx);

            if (result.hasError()) {
                throw new Exception("Get Post eth call error" + result.getError().getMessage());
            }


            String value = result.getValue();
            List<TypeReference<Type>> outputParameters = func.getOutputParameters();
            List<Type> types = FunctionReturnDecoder.decode(value, outputParameters);
//            return types.get(0);
            Log.i("Asd",types.get(0).toString());

        } catch (Exception e) {
            Log.i("Asd","1");
            e.printStackTrace();
            Log.i("Asd",e.toString());
        }
    }

//    public static void makeTrx() {
//        try {
//
//            String hdPath = ScwService.getHdPath(ScwCoinType.ETH, 0);
//
//            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//
//
//            RawTransaction unsignedTransaction = RawTransaction.createTransaction(new BigInteger("10"), Convert.toWei("400", Convert.Unit.GWEI).toBigInteger()
//                    , new BigInteger("41000"), CONTRACT_ADDRESS, Long.toString(timestamp.getTime()));
//
//
//            byte[] byteArr = TransactionEncoder.encode(unsignedTransaction);
//
//            ScwService.getInstance().signEthTransaction(new ScwService.ScwSignEthTransactionCallback() {
//                @Override
//                public void onSuccess(byte[] bytes) {
//                    String signedTxHex = Numeric.toHexString(bytes);
//                   ethereumService.getTransactionDetail(signedTxHex).setCallback(new);
//                    EthereumTransaction
//
//                }
//                @Override
//                public void onFailure(int i, @Nullable String s) {
//                    Log.e("signEth", s);
//                }
//            }, byteArr,hdPath);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
