package com.android.samsung.codelab.guestbookdapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class Information extends AppCompatActivity {

    InputMethodManager imm;

    LinearLayout layer;

    TextView nameBox;
    TextView birthBox;
    TextView genBox;
    TextView picBox;
    TextView noticeBox;

    EditText nameText;
    EditText birthText;
    EditText jobText;
    EditText bodyText;
    EditText etcText;

    RadioGroup genGroup;
    RadioButton checked;

    Button saveB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        layer = findViewById(R.id.layer);
        layer.setOnClickListener(myClickListener); //hideKeyboard 메소드

        nameBox = findViewById(R.id.nameBox);
        birthBox = findViewById(R.id.birthBox);
        genBox = findViewById(R.id.genBox);
        picBox = findViewById(R.id.picBox);
        noticeBox = findViewById(R.id.noticeBox);

        nameText = findViewById(R.id.nameText);
        birthText = findViewById(R.id.birthText);
        jobText = findViewById(R.id.jobText);
        bodyText = findViewById(R.id.bodyText);
        etcText = findViewById(R.id.etcText);

        genGroup = findViewById(R.id.rGroup);

        saveB = findViewById(R.id.saveB);

        //some word should be painted
        String Strs[] = new String[5];
        SpannableString spbStr[] = new SpannableString[5];
        int start[] = new int[5];
        int end[] = new int[5];

        Strs[0] = nameBox.getText().toString();
        Strs[1] = birthBox.getText().toString();
        Strs[2] = genBox.getText().toString();
        Strs[3] = picBox.getText().toString();
        Strs[4] = noticeBox.getText().toString();

        String word = "*";
        int len = word.length();

        for(int i=0; i<5; i++){
            spbStr[i] = new SpannableString(Strs[i]);
            start[i] = Strs[i].indexOf(word);
            end[i] = start[i] + len;
            spbStr[i].setSpan(new ForegroundColorSpan(Color.parseColor("#E91E1E")), start[i], end[i], Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        nameBox.setText(spbStr[0]);
        birthBox.setText(spbStr[1]);
        genBox.setText(spbStr[2]);
        picBox.setText(spbStr[3]);
        noticeBox.setText(spbStr[4]);

        saveB.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        checked = findViewById(genGroup.getCheckedRadioButtonId());

                        Intent intent = new Intent(getApplicationContext(), Profile.class);

                        try{
                            intent.putExtra("NAME_KEY", nameText.getText().toString());
                            intent.putExtra("BIRTH_KEY", birthText.getText().toString());
                            intent.putExtra("GEN_KEY", checked.getText().toString());
                            intent.putExtra("JOB_KEY", jobText.getText().toString());
                            intent.putExtra("BODY_KEY", bodyText.getText().toString());
                            intent.putExtra("ETC_KEY", etcText.getText().toString());

                            if(nameText.getText().length() == 0 || birthText.length() ==0)
                                throw new Exception();

                            startActivity(intent);
                        }catch(Exception e){ //값 미입력 시 예외처리
                            e.printStackTrace();
                            Toast toast = Toast.makeText(getApplicationContext(), "올바른 값이 입력되지 않았습니다.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }
        );
    }

    //뒤로 가기 하면 무조건 Intro 화면 (이전 액티비티로 안 감)
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), intro.class);
        startActivity(intent);
    }

    //키보드 숨기기 Listener
    View.OnClickListener myClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            hideKeyboard();
        }
    };

    //키보드 숨기기 메소드
    private void hideKeyboard() {
        imm.hideSoftInputFromWindow(nameText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(birthText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(jobText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(bodyText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etcText.getWindowToken(), 0);
    }
}
