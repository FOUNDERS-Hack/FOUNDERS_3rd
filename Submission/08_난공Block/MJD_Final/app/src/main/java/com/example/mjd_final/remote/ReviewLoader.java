package com.example.mjd_final.remote;

import com.example.mjd_final.ethereum.FunctionUtil;
import com.example.mjd_final.model.Review;
import com.example.mjd_final.model.Vote;
import com.example.mjd_final.util.AppExecutors;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ReviewLoader {
    private String address;

    public ReviewLoader(String address, String storeName) {
        this.address = address;
    }

    //가게에 대한 리뷰 가져오기
    public void loadReviews(int numOfReview, String storeName, Listener listener) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            try {
                int storeReviewCount = getStoreReviewCount(storeName);
                int lastIndex = storeReviewCount - 1 - numOfReview;
                if (lastIndex < 0)
                    lastIndex = 0;
                ArrayList<Review> reviewList = new ArrayList<>();
                for (int i = storeReviewCount - 1; i >= lastIndex; i--) {
                    reviewList.add(getStoreReview(storeName, i));
                }
                listener.reviewDidLoaded(true, reviewList, "Success");
            } catch (Exception e) {
                listener.reviewDidLoaded(false, null, e.getMessage());
            }
        });
    }

    private int getStoreReviewCount(String storeName) throws Exception {
        Function functionGetStoreReviewCount = FunctionUtil.createGetStoreReviewCountSmartContractCall(storeName);
        String data = FunctionEncoder.encode(functionGetStoreReviewCount);
        Transaction tx = Transaction.createEthCallTransaction(address, FunctionUtil.CONTRACT_ADDRESS, data);

        EthCall result = RemoteManager.getInstance().sendEthCall(tx);
        if (result.hasError()) {
            throw new Exception("Get Store Review Count eth call error" + result.getError().getMessage());
        }

        String value = result.getValue();
        List<TypeReference<Type>> outputParameters = functionGetStoreReviewCount.getOutputParameters();
        List<Type> types = FunctionReturnDecoder.decode(value, outputParameters);
        Type type = types.get(0);
        BigInteger storeReviewCount = (BigInteger) type.getValue();
        return storeReviewCount.intValue();
    }

    private Review getStoreReview(String storeName, int index) throws Exception {
        Function functionGetStoreReview = FunctionUtil.createGetStoreReviewSmartContractCall(storeName, index);
        String data = FunctionEncoder.encode(functionGetStoreReview);
        Transaction tx = Transaction.createEthCallTransaction(address, FunctionUtil.CONTRACT_ADDRESS, data);

        EthCall result = RemoteManager.getInstance().sendEthCall(tx);
        if (result.hasError()) {
            throw new Exception("Get Store Review eth call error" + result.getError().getMessage());
        }

        String value = result.getValue();
        List<TypeReference<Type>> outputParameters = functionGetStoreReview.getOutputParameters();
        List<Type> types = FunctionReturnDecoder.decode(value, outputParameters);
        return new Review(
                (int) types.get(0).getValue()
                , (String) types.get(1).getValue()
                , (String) types.get(2).getValue()
                , (String) types.get(3).getValue()
                , (String) types.get(4).getValue()
                , new Vote((int) types.get(5).getValue(), (int) types.get(6).getValue(), (boolean) types.get(7).getValue())
        );
    }

    private int getMyReviewCount(String userId) throws Exception {
        Function functionGetMyReviewCount = FunctionUtil.createGetMyReviewCountSmartContractCall(userId);
        String data = FunctionEncoder.encode(functionGetMyReviewCount);
        Transaction tx = Transaction.createEthCallTransaction(address, FunctionUtil.CONTRACT_ADDRESS, data);

        EthCall result = RemoteManager.getInstance().sendEthCall(tx);
        if (result.hasError()) {
            throw new Exception("Get My Review Count eth call error" + result.getError().getMessage());
        }

        String value = result.getValue();
        List<TypeReference<Type>> outputParameters = functionGetMyReviewCount.getOutputParameters();
        List<Type> types = FunctionReturnDecoder.decode(value, outputParameters);
        Type type = types.get(0);
        BigInteger myReviewCount = (BigInteger) type.getValue();
        return myReviewCount.intValue();
    }

    private Review getMyReview(String userId, int index) throws Exception {
        Function functionGetMyReview = FunctionUtil.createGetMyReviewSmartContractCall(userId, index);
        String data = FunctionEncoder.encode(functionGetMyReview);
        Transaction tx = Transaction.createEthCallTransaction(address, FunctionUtil.CONTRACT_ADDRESS, data);

        EthCall result = RemoteManager.getInstance().sendEthCall(tx);
        if (result.hasError()) {
            throw new Exception("Get My Review eth call error" + result.getError().getMessage());
        }

        String value = result.getValue();
        List<TypeReference<Type>> outputParameters = functionGetMyReview.getOutputParameters();
        List<Type> types = FunctionReturnDecoder.decode(value, outputParameters);
        return new Review(
                (int) types.get(0).getValue()
                , (String) types.get(1).getValue()
                , (String) types.get(2).getValue()
                , (String) types.get(3).getValue()
                , (String) types.get(4).getValue()
                , new Vote((int) types.get(5).getValue(), (int) types.get(6).getValue(), (boolean) types.get(7).getValue())
        );
    }

    public interface Listener {
        void reviewDidLoaded(boolean success, List<Review> feedList, String message);
    }
}
