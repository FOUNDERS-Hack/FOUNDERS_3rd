package com.example.doorlockapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

public class ContractService {

    public Function createGetPostCountSmartContractCall() {
        return new Function("openDoor"
                , Collections.emptyList()
                , singletonList(new TypeReference<Bool>() {
        }));
    }

    public boolean openDoor(String address, String contractAddress, String data, String signTx) {
        try {
            ApiService apiService = new ApiService();
            Boolean issueTimeBool = apiService.execute().get();
            EthCallTask ethCallTask = new EthCallTask();
            Boolean contractBool = ethCallTask.execute(address, contractAddress, data).get();

            if (issueTimeBool && contractBool) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.i("Asd", "1");
            e.printStackTrace();
            Log.i("Asd", e.toString());
        }
        return false;
    }

    public class EthCallTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {

                Function func = createGetPostCountSmartContractCall();

                Transaction tx = Transaction.createEthCallTransaction(arg0[0], arg0[1], arg0[2]);

                EthCall result = RemoteManager.getInstance().sendEthCall(tx);

                if (result.hasError()) {
                    throw new Exception("Get Post eth call error" + result.getError().getMessage());
                }


                String value = result.getValue();
                List<TypeReference<Type>> outputParameters = func.getOutputParameters();
                List<Type> types = FunctionReturnDecoder.decode(value, outputParameters);
                Log.i("Asd", types.get(0).toString());
                return new Boolean(types.get(0).toString());
            } catch (MalformedURLException e) {
                //
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
