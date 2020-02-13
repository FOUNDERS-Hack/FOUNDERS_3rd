package founders.blockers.beyondcloudapp.function;
/*

 createTx() : link to web3 transaction-creation function

 callBack() : get transaction data

 countTx() : count account-related block

 */

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.singletonList;

public class FunctionUtils {
//    public static final String CONTRACT_ADDRESS = "0x1425cbbbfb6d3c309c81fc8193e2e38f94c333b7";

    public static Function createTx(String name, String date, String content){
        return new Function(
                "set",
                Arrays.asList(
                        new Utf8String(name),
                        new Utf8String(date),
                        new Utf8String(content)
                ),
                Collections.emptyList());
    }

    public static Function callTx(int key){
        return new Function(
                "get",
                singletonList(new Uint(BigInteger.valueOf(key))),
                Arrays.asList(
                        new TypeReference<Utf8String>() {
                        },
                        new TypeReference<Utf8String>() {
                        },
                        new TypeReference<Utf8String>() {
                        }
                )
        );
    }

    public static Function countTx(){
        return new Function(
                "getdataCount",
                Collections.emptyList(),
                singletonList(new TypeReference<Uint>() {
                })
        );
    }

    public static Function callBlockByName(String key) {
        return new Function("get"
                ,Arrays.asList(
                new Utf8String(key))
                , Arrays.asList(
                new TypeReference<Utf8String>() {
                }
                , new TypeReference<Utf8String>() {
                }
                , new TypeReference<Utf8String>() {
                }


        ));
    }

    public static Function InttoHex(int key){
        return new Function("get"
                ,   singletonList(new Uint(BigInteger.valueOf(key)))
                ,  Collections.emptyList());

    }


    public static Function StringtoHex(String result) {
        return new Function("nope"
                ,Arrays.asList(
                new Utf8String(result))
                ,  Collections.emptyList());

    }
}
