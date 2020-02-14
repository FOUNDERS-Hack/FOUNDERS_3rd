package com.example.blockchain_v1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class JaeActivity2 extends AppCompatActivity {
        LinearLayout baselayout;
        Button button1;
        Intent intent_get;
       TextView textViewTemp;
        private AppBarConfiguration mAppBarConfiguration;



  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jae_hyun_two);


    //jaeActivity에서 editText.getString하는 게 맞아
       // EditText editText = findViewById(R.id.editTextGet);


      String gettingText1;
      String gettingText2;
      textViewTemp = findViewById(R.id.textViewParentTop);


        intent_get=getIntent();
        gettingText1=intent_get.getStringExtra("te");
        gettingText2=intent_get.getStringExtra("tel");
        System.out.println("잘 받나"+gettingText1);
        textViewTemp.setText(gettingText1+gettingText2);

//      Log.e(TAG, "onCreate: ", );.e(editText.getText().toString();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinatio
      // ns.


    }

}
