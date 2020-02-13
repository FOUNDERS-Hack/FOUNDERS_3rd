package com.example.doorlockapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    //qr code scanner object
    private IntentIntegrator qrScan;
    TextView address;
    Button button;

    public static boolean open =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qrScan = new IntentIntegrator(this);
        address = findViewById(R.id.address);
        button = findViewById(R.id.button);

        if(open == false){

        }
        else {
            Intent in = new Intent(MainActivity.this, success.class);
            startActivity(in);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scan option
                qrScan.setPrompt("Scanning...");
                //qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
            }
        });

    }


    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "Cancel!", Toast.LENGTH_SHORT).show();
            } else {
                //qrcode 결과가 있으면
                Toast.makeText(MainActivity.this, "Scan Success", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject obj = new JSONObject(result.getContents());

                    String total =  result.getContents();
                    String adddress3,contractAddress3,data3,hex3;
                    int fisrted = 0 , seconded =0, thired = 0;

                    for(int i=0;i<total.length();i++){
                        if(total.charAt(i)=='*'&& total.charAt(i)=='*' && fisrted == 0){
                            fisrted = i;
                        }
                        else if(total.charAt(i)=='*'&& total.charAt(i)=='*' && seconded == 0){
                            seconded = i;
                        }
                        else if(total.charAt(i)=='*'&& total.charAt(i)=='*' && seconded == 0) {
                            thired = i;
                        }
                    }
                    data3 = total.substring(0,fisrted);
                    contractAddress3 = total.substring(fisrted+2,seconded);
                    adddress3 = total.substring(seconded+2,thired);
                    hex3 = total.substring(thired+2);
                    Log.i("good",data3);
                    Log.i("good",contractAddress3);
                    Log.i("good",adddress3);
                    Log.i("good",hex3);
                    ContractService contractService = new ContractService();
                    if (contractService.openDoor(adddress3,contractAddress3,data3,hex3)) {
                        Intent in = new Intent(MainActivity.this, success.class);
                        startActivity(in);
                    } else {
                        Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                    //address.setText(result.getContents());

                    String total =  result.getContents();
                    String adddress3,contractAddress3,data3,hex3;
                    int fisrted = 0 , seconded =0, thired = 0;

                    for(int i=0;i<total.length();i++){
                        if(total.charAt(i)=='*'&& total.charAt(i+1)=='*' && fisrted == 0){
                            fisrted = i;
                        }
                        else if(total.charAt(i)=='*'&& total.charAt(i+1)=='*' && seconded == 0){
                            seconded = i;
                        }
                        else if(total.charAt(i)=='*'&& total.charAt(i+1)=='*' && thired == 0) {
                            thired = i;
                        }
                    }
                    data3 = total.substring(0,fisrted);
                    contractAddress3 = total.substring(fisrted+2,seconded);
                    adddress3 = total.substring(seconded+2,thired);
                    hex3 = total.substring(thired+2);

                    Log.i("good",data3);
                    Log.i("good",contractAddress3);
                    Log.i("good",adddress3);
                    Log.i("good",hex3);
                    ContractService contractService = new ContractService();
                    if (contractService.openDoor(adddress3,contractAddress3,data3,hex3)) {
                        Intent in = new Intent(MainActivity.this, success.class);
                        startActivity(in);
                    } else {
                        Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_LONG).show();
                    }
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



}