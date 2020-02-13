package com.android.samsung.codelab.guestbookdapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Upload extends AppCompatActivity {

    ImageButton xBtn;
    Button uldB;

    Spinner drives;
    ArrayList<String> driveList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        xBtn = findViewById(R.id.xBtn);
        uldB = findViewById(R.id.uldB);
        drives = findViewById(R.id.drives);

        driveList = new ArrayList();

        driveList.add("Google Drive");
        driveList.add("DropBox");
        driveList.add("앨범");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                driveList);

        drives.setAdapter(arrayAdapter);


        xBtn.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(), intro.class);
                        startActivity(intent);
                    }
                }
        );

        uldB.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        show();
                    }
                }
        );

    }

    private void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView title = new TextView(this);
        title.setText("회원님의 번호는 01번 입니다.\n부여된 번호로 진행상황을\n확인할 수 있습니다.");
        title.setTextSize(25);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER_VERTICAL);
        title.setGravity(Gravity.LEFT);

        builder.setCustomTitle(title);

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), intro.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
