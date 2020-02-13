package com.samsung.founders.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Input extends AppCompatActivity {

    Button sendBtn;

    EditText name;
    EditText date;
    EditText gen;
    EditText job;
    EditText etc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        sendBtn = findViewById(R.id.sendBtn);

        name = findViewById(R.id.name);
        date = findViewById(R.id.date);
        gen = findViewById(R.id.gen);
        job = findViewById(R.id.job);
        etc = findViewById(R.id.etc);

        sendBtn.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        intent.putExtra("NAME", name.getText().toString());
                        intent.putExtra("DATE", date.getText().toString());
                        intent.putExtra("GEN", gen.getText().toString());
                        intent.putExtra("JOB", job.getText().toString());
                        intent.putExtra("ETC", etc.getText().toString());

                        startActivity(intent);
                        }
                    }
        );

    }
}
