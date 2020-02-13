package com.example.mjd_final.ethereum;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.singletonList;

public class FunctionUtil {

    public static String CONTRACT_ADDRESS = "0x2146fec6907be71b2bac4d8025eeff1f3054c168";

    public static Function createGiveAuthSmartContractCall(String userId, String storeName) {
        return new Function("giveAuth"
                , Arrays.asList(
                new Address(userId)
                , new Utf8String(storeName))
                , Collections.emptyList());
    }

    public static Function createGetStoreReviewCountSmartContractCall(String storeName) {
        return new Function("getStoreReviewCount"
                , singletonList(new Utf8String(storeName))
                , singletonList(new TypeReference<Uint>(){
        }));
    }

    public static Function createGetStoreReviewSmartContractCall(String storeName, int index) {
        return new Function("getStoreReview"
                , Arrays.asList(
                new Utf8String(storeName)
                , new Uint(BigInteger.valueOf(index)))
                , Arrays.asList(
                new TypeReference<Uint>() {
                }
                , new TypeReference<Address>() {
                }
                , new TypeReference<Utf8String>() {
                }
                , new TypeReference<Utf8String>() {
                }
                , new TypeReference<Utf8String>() {
                }
                , new TypeReference<Bool>() {
                }
                , new TypeReference<Uint>() {
                }
                , new TypeReference<Uint>() {
                }
                , new TypeReference<Bool>() {
                }
        ));
    }

    public static Function createGetMyReviewCountSmartContractCall(String userId) {
        return new Function("getMyReviewCount"
                , singletonList(new Address(userId))
                , singletonList(new TypeReference<Uint>() {
        }));
    }

    public static Function createGetMyReviewSmartContractCall(String userId, int index) {
        return new Function("getMyReview"
                , Arrays.asList(
                new Address(userId)
                , new Uint(BigInteger.valueOf(index)))
                , Arrays.asList(
                new TypeReference<Uint>() {
                }
                , new TypeReference<Address>() {
                }
                , new TypeReference<Utf8String>() {
                }
                , new TypeReference<Utf8String>() {
                }
                , new TypeReference<Utf8String>() {
                }
                , new TypeReference<Bool>() {
                }
                , new TypeReference<Uint>() {
                }
                , new TypeReference<Uint>() {
                }
                , new TypeReference<Bool>() {
                }
        ));
    }

    public static Function createPostReviewSmartContractCall(String userId, String storeName, String comment, String date) {
        return new Function("postReview"
                , Arrays.asList(
                new Address(userId)
                , new Utf8String(storeName)
                , new Utf8String(comment)
                , new Utf8String(date))
                , singletonList(new TypeReference<Bool>() {
        }));
    }

    public static Function createPostVoteSmartContractCall(String userId, int reviewId, boolean approve) {
        return new Function("postVote"
                , Arrays.asList(
                new Address(userId)
                , new Uint(BigInteger.valueOf(reviewId))
                , new Bool(approve))
                , singletonList(new TypeReference<Bool>() {
        }));
    }
}