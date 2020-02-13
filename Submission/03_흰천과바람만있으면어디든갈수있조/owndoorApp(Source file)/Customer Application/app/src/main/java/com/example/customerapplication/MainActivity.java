package com.example.customerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void started(View view){
        Toast.makeText(getApplicationContext(),"버튼이 눌러졌습니다.",Toast.LENGTH_SHORT).show();
        Intent in = new Intent(MainActivity.this, MainMenu.class);
        startActivity(in);

    }
}
