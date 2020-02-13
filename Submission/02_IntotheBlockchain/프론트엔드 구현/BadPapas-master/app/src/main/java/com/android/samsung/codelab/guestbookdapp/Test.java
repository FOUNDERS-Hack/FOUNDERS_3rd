package com.android.samsung.codelab.guestbookdapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.samsung.codelab.guestbookdapp.model.Report;

public class Test extends AppCompatActivity {

    Button Btn = (Button)findViewById(R.id.button);
    EditText editText = (EditText) findViewById(R.id.editText);
    EditText editText2 = (EditText) findViewById(R.id.editText2);
    EditText editText3 = (EditText) findViewById(R.id.editText3);
    EditText editText4 = (EditText) findViewById(R.id.editText4);
    EditText editText5 = (EditText) findViewById(R.id.editText5);




    Report report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);


        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                String date = editText2.getText().toString();
                String sex = editText3.getText().toString();
                String company = editText4.getText().toString();
                String feature = editText5.getText().toString();

                report.setName(name);
                report.setDate(date);
                report.setSex(sex);
                report.setCompany(company);
                report.setFeature(feature);


            }
        });

    }



}
