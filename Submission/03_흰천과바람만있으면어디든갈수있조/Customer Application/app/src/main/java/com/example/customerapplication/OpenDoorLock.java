package com.example.customerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.customerapplication.presenter.WriteFeedPresenter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.example.customerapplication.presenter.WriteFeedPresenter;
import com.samsung.android.sdk.coldwallet.ScwCoinType;
import com.samsung.android.sdk.coldwallet.ScwService;

import java.util.ArrayList;
import java.util.List;

public class OpenDoorLock extends AppCompatActivity {
    public static final String CONTRACT_ADDRESS = "0x5dFcDc09966250F5ec678d1D28695211CEd672bb";
    public static String address;
    private IntentIntegrator qrScan;
    private ImageView iv;
    private String text;
    TextView textView;

    // test
    private WriteFeedPresenter writeFeedPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_door_lock);


       // writeFeedPresenter = new WriteFeedPresenter();
        WriteFeedPresenter.getEthereumAddress();;
        text = WriteFeedPresenter.makeTrx(WriteFeedPresenter.address); //data 값을 받아서

        text += "**";
        text += CONTRACT_ADDRESS;  //컨트랙트어드레스
        text += "**";

       // Log.i("www",WriteFeedPresenter.address);
        WriteFeedPresenter.getEthereumAddress();
        text += WriteFeedPresenter.address ;
        iv = (ImageView)findViewById(R.id.qrcode);
        textView =findViewById(R.id.textView);

        textView.setText(text);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv.setImageBitmap(bitmap);
        }catch (Exception e){}


    }











}
