package com.example.blockchain_v1;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.blockchain_v1.sdk.Init;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

public class JaeActivity extends AppCompatActivity {
    LinearLayout baselayout;
    // Button button1;
    private Button submitBtn;

    private TextView text1;
    private TextView text2;
    private AppBarConfiguration mAppBarConfiguration;
    public EditText editText;
    public String forDebug;
    private String tempString;
    private boolean isSelected1;
    private boolean isSelected2;

    TextView temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.jae_hyun);

        Init init;

        submitBtn = findViewById(R.id.bottom_btn);

        temp = findViewById(R.id.textViewParentTop);
        editText = findViewById(R.id.editTextFilling);
        text1 = findViewById(R.id.textViewLeftChild);
        text2 = findViewById(R.id.textViewRightChild);

        text1.setOnClickListener(new View.OnClickListener() {
            //                editText.getTex t()
            @Override
            public void onClick(View view) {
                isSelected1 = true;
                isSelected2 = false;
//                tempString = temp.getText().toString()+text1.getText().toString() ;
//+ editText.getText().toString()

//                temp.setText(tempString);
//                text1.setText("");

//                editText.getText( ).toString();

                // editText.setText("");
            }
            //   tempString= temp.getText()+text1.getText().toString();

            //값을 저장할거냐 표시하고 끝낼 거냐
        });
        text2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                isSelected2 = true;
                isSelected1 = false;
//                tempString = temp.getText() + text2.getText().toString();
//                text2.setText("");
//                editText.setText("");
            }

        });



        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                String instanceId=Firebase
//                println("확인된 "

                //init = new Init();
//                init.writeAndDistribute();

                if(isSelected1) {
                    tempString = temp.getText().toString()+text1.getText().toString() ;
                } else {
                    tempString = temp.getText().toString()+text2.getText().toString() ;
                }

                isSelected1 = false;
                isSelected2 = false;


            }
        });
    }
}
