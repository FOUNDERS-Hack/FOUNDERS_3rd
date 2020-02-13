package com.android.samsung.codelab.guestbookdapp.remote;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.samsung.codelab.guestbookdapp.contract.IntroContract;
import com.android.samsung.codelab.guestbookdapp.ethereum.FunctionUtil;
import com.android.samsung.codelab.guestbookdapp.model.Feed;
import com.android.samsung.codelab.guestbookdapp.util.AppExecutors;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedLoader {


    private String address;

    public FeedLoader(String address) {
        this.address = address;
    }

    public void loadFeeds(int numOfFeed, Listener listener) {
        AppExecutors.getInstance().networkIO().execute(() -> {
            try {
                int postCount = getPostCount();
                int lastIndex = postCount - 1 - numOfFeed;
                if (lastIndex < 0)
                    lastIndex = 0;

                ArrayList<Feed> feedList = new ArrayList<>();
                for (int index = postCount - 1; index >= lastIndex; index--) {
                    feedList.add(getPost(index));
                }
                listener.feedDidLoaded(true, feedList, "Success");
            } catch (Exception e) {
                listener.feedDidLoaded(false, null, e.getMessage());
            }
        });

    }

    private int getPostCount() throws Exception {
        Function functionGetPostCount = FunctionUtil.createGetPostCountSmartContractCall();
        String data = FunctionEncoder.encode(functionGetPostCount);
        Transaction tx = Transaction.createEthCallTransaction(address, FunctionUtil.CONTRACT_ADDRESS, data);

        EthCall result = RemoteManager.getInstance().sendEthCall(tx);
        if (result.hasError()) {
            throw new Exception("Get Post count eth call error" + result.getError().getMessage());
        }

        String value = result.getValue();
        List<TypeReference<Type>> outputParameters = functionGetPostCount.getOutputParameters();
        List<Type> types = FunctionReturnDecoder.decode(value, outputParameters);
        Type type = types.get(0);
        BigInteger postCount = (BigInteger) type.getValue();

        return postCount.intValue();
    }

//    public class ListItem{
//        public String value;
//        public boolean hasBackground = false;
//
//        public ListItem(String value, boolean background){
//            this.value = value;
//            this.hasBackground = background;
//        }
//    }
//
//    private ListItem getDefaultItem(String value) {
//        return new ListItem(value, false);
//    }
//
//    private void updateCommonElements() {
//        for (int j = 0; j < selectedGlobalInstruments.size(); j++) {
//            for (int i = 0; i < forexInstruments.size(); i++) {
//                String instrumentList = "'" + forexInstruments.get(i).value + "'";
//                if (selectedGlobalInstruments.get(j).equals(instrumentList)) {
//                    forexInstruments.get(i).hasBackground = true;
//                }
//            }
//        }
//    }

    private Feed getPost(int index) throws Exception {

        // TODO : Make Get Post Smart Contract call(Live code)
        // encode function with FunctionEncoder
        // Make Eth Call Transaction
        // send ETH Call


        Function functionGetPost = FunctionUtil.createGetPostSmartContractCall(index);
        String data = FunctionEncoder.encode(functionGetPost);
        Transaction tx = Transaction.createEthCallTransaction(address, FunctionUtil.CONTRACT_ADDRESS, data);

        EthCall result = RemoteManager.getInstance().sendEthCall(tx);  //조회를 하기 때문에 서명의 과정이 불필요하다(Ether call transaction)
        if (result.hasError()) {
            throw new Exception("Get Post eth call error" + result.getError().getMessage());
        }



        //수정
        String value = result.getValue();
        Log.d("ExampleCode", "Return value : " + value);
        List<TypeReference<Type>> outputParameters = functionGetPost.getOutputParameters();
        List<Type> types = FunctionReturnDecoder.decode(value, outputParameters);
        Log.d("ExampleCode", "type length : " + types.size());
        Log.d("ExampleCode", "value 1(area) : " + types.get(0).getValue());
        Log.d("ExampleCode", "value 2(bname) : " + types.get(1).getValue());
        Log.d("ExampleCode", "value 3(ph) : " + types.get(2).getValue());
        Log.d("ExampleCode", "value 4(bod) : " + types.get(3).getValue());
        Log.d("ExampleCode", "value 5(cod) : " + types.get(4).getValue());
        Log.d("ExampleCode", "value 6(toc) : " + types.get(5).getValue());


//        ListView LV = null;
//        LV = (ListView) findViewById


        return new Feed(
                (String) types.get(0).getValue()
                , (String) types.get(1).getValue()
                , (String) types.get(2).getValue()
                , (String) types.get(3).getValue()
                , (String) types.get(4).getValue()
                , (String) types.get(5).getValue()
        );

    }





    public interface Listener {
        void feedDidLoaded(boolean success, List<Feed> feedList, String message);
    }
}
// 기록할 때는 코드를 보면 가스를 요구하는데 이 떄문에 서명을 요구한다. 하지만 여기에서 확인하는 과정에서는 서명을 요구하지 않기 때문에 별다른 코드를 요구하지 않는다.