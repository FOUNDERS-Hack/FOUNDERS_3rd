package com.android.samsung.codelab.guestbookdapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Report extends AppCompatActivity {

    ImageButton closeB;
    TextView confirmBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        closeB = findViewById(R.id.closeB);
        confirmBox = findViewById(R.id.confirmBox);

        show();


        //닫기 버튼, intro로 이동
        closeB.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(), intro.class);
                        startActivity(intent);
                    }
                }
        );

    }


    private void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);

        builder.setTitle("서류 제출 완료 후 부여 받은\n번호를 입력해주세요");
        builder.setView(input);

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmBox.setTextColor(Color.parseColor("#33CA22"));
                        confirmBox.setText("정보 확인됨");
                    }
                });

        builder.setNegativeButton("처음이에요",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), Upload.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
