package com.android.samsung.codelab.guestbookdapp.ethereum;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.singletonList;
public class FunctionUtil {

    public static final String CONTRACT_ADDRESS = "0x79B2a9ad0Cd37BC329e94b9003B1166C7d0d3AC2";

    //수정
    public static Function createPostSmartContractCall(String area, String bname, String ph, String bod, String cod, String toc) {
        return new Function("post"
                , Arrays.asList(
                new Utf8String(area)
                , new Utf8String(bname)
                , new Utf8String(ph)
                , new Utf8String(bod)
                , new Utf8String(cod)
                , new Utf8String(toc))
                , Collections.emptyList());
    }

    public static Function createGetPostCountSmartContractCall() {
        return new Function("getPostCount"
                , Collections.emptyList()
                , singletonList(new TypeReference<Uint>() {
        }));
    }

    //수정
    public static Function createGetPostSmartContractCall(int index) {
        return new Function("getPost"
                , singletonList(new Uint(BigInteger.valueOf(index)))
                , Arrays.asList(
                new TypeReference<Utf8String>() {
                }
                , new TypeReference<Utf8String>() {
                }
                , new TypeReference<Utf8String>() {
                }
                , new TypeReference<Utf8String>() {
                }
                , new TypeReference<Utf8String>() {
                }
                , new TypeReference<Utf8String>() {
                }
        ));
    }
}
