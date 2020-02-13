package com.android.samsung.codelab.guestbookdapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class copy_key extends AppCompatActivity {

    Button closeB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.copy_key);

        closeB = findViewById(R.id.closeB);

        closeB.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(), intro.class);
                        startActivity(intent);
                    }
                }
        );

        //버튼 객체 생성
       // FButton copyKey = (FButton) findViewById(R.id.copy);

        //버튼 효과
        /*copyKey.setButtonColor(getResources().getColor(R.color.fbutton_color_concrete));
        copyKey.setShadowColor(getResources().getColor(R.color.fbutton_color_asbestos));
        copyKey.setShadowEnabled(true);
        copyKey.setShadowHeight(5);
        copyKey.setCornerRadius(5);
*/
       /* button1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(intro.this, );
                startActivity(intent);
            }
        });*/


    }


}
