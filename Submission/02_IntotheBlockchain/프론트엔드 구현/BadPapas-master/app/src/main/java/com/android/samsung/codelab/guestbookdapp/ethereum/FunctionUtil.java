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
    //TODO : 여기서 스마트컨트랙트 주소 바꿀 수 있떠
    public static final String CONTRACT_ADDRESS = "0xc6d3d14a1725967b9e0a0e62b9a6635fc1d4dd45";


    //TODO : 스마트컨트랙트에 트랜잭션 보내는 것.
    public static Function createReportSmartContractCall(String name, String date, String sex, String company, String feature, String privateKey) {
        return new Function("report"
                , Arrays.asList(
                new Utf8String(name)
                , new Utf8String(date)
                , new Utf8String(sex)
                , new Utf8String(company)
                , new Utf8String(feature))
                , Collections.emptyList());
    }
/*
    public static Function createGetPostCountSmartContractCall() {
        return new Function("etReports"
                , Collections.emptyList()
                , singletonList(new TypeReference<Uint>() {
        }));
    }
*/
    //TODO:스마트컨트랙트에서 데이터 받아오는 것
    public static Function createGetReportSmartContractCall(int index) {
        return new Function("getReports"
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

        ));
    }
}
