package com.android.samsung.codelab.guestbookdapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class intro extends AppCompatActivity {

    private BackPressCloseHandler backKeyClickHandler;

    ImageButton button1;
    ImageButton button2;
    ImageButton button3;
    ImageButton button4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        //버튼 객체 생성
        button1 = findViewById(R.id.btn_1);
        button2 = findViewById(R.id.btn_2);
        button3 = findViewById(R.id.btn_3);
        button4 = findViewById(R.id.btn_4);

        backKeyClickHandler = new BackPressCloseHandler(this);

        button1.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(), Information.class);
                        startActivity(intent);
                    }
                }
        );

        button2.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(), Report.class);
                        startActivity(intent);
                    }
                }
        );

        button3.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(), badguylistActivity.class);
                        startActivity(intent);
                    }
                }
        );
        button4.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(), FaqActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        backKeyClickHandler.onBackPressed();
    }

}
