package com.samsung.founders.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Information extends AppCompatActivity {

    TextView nameText;
    TextView dateText;
    TextView genText;
    TextView comText;
    TextView etcText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        nameText = findViewById(R.id.name);
        dateText = findViewById(R.id.date);
        genText = findViewById(R.id.gender);
        comText = findViewById(R.id.company);
        etcText = findViewById(R.id.etc);

        Intent intent = getIntent();

        String name = intent.getExtras().getString("NAME");
        String date = intent.getExtras().getString("DATE");
        String gender = intent.getExtras().getString("GEN");
        String company = intent.getExtras().getString("COM");
        String etc = intent.getExtras().getString("ETC");

        nameText.setText(name);
        dateText.setText(date);
        genText.setText(gender);
        comText.setText(company);
        etcText.setText(etc);
    }
}
