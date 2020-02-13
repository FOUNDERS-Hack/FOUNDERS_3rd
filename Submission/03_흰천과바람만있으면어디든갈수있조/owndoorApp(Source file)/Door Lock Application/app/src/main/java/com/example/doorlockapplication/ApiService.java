package com.example.doorlockapplication;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.web3j.abi.datatypes.Bool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;

public class ApiService  extends AsyncTask<String, Void, Boolean> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {

            JSONObject json = new JSONObject();
            json.put("tx", params[0]);

            String body = json.toString();
            System.out.println(body);
            URL postUrl = new URL("http://52.79.241.140:2000/decode");       // URL 설정
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
            connection.setDoOutput(true); 				// xml내용을 전달하기 위해서 출력 스트림을 사용
            connection.setInstanceFollowRedirects(false);  //Redirect처리 하지 않음
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStream os= connection.getOutputStream();
            os.write(body.getBytes());
            os.flush();
            System.out.println("Location: " + connection.getHeaderField("Location"));

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));

            String output;
            StringBuilder builder = new StringBuilder();
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                builder.append(output);                     // View에 표시하기 위해 라인 구분자 추가
            }
            String myResult = builder.toString();
            connection.disconnect();
            String issueTimeStamp = myResult.substring(3);
            issueTimeStamp = issueTimeStamp.substring(0, issueTimeStamp.length()-1);
            long i = 0;
            try {
                issueTimeStamp = issueTimeStamp.replaceAll("\"", "");
                i = Long.parseLong(issueTimeStamp);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (timestamp.getTime() > i + 300000) {
                return new Boolean(false);
            } else {
                return new Boolean(true);
            }
        } catch (MalformedURLException e) {
            //
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
