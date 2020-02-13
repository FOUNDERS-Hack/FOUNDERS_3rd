package com.example.customerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Arrays;
import java.util.Calendar;
import java.util.FormatterClosedException;

public class
ReservateHotel extends AppCompatActivity {


    Button button;
    //시간관련
    TimePicker timePicker;
    CalendarView calendarView;


    //날짜를 저장하는 곳
    int year,month,dayofmonth;
    int hour, minuted;
    String totaldate;
    String totaltime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservate_hotel);

        button =findViewById(R.id.button5);
        timePicker = findViewById(R.id.timePicker);
        calendarView = findViewById(R.id.calendarView);

        //시간 받아오기
        totaltime= Integer.toString(timePicker.getHour()) + ":" + Integer.toString(timePicker.getMinute());

        //날짜받아오기

        Calendar cal =Calendar.getInstance();
        year = cal.get(cal.YEAR);
        month = cal.get(cal.MONTH);
        dayofmonth = cal.get(cal.DATE);
        totaldate = Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(dayofmonth);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(); //인텐트 생성
                intent.putExtra("INPUT_TEXT",totaldate + " " + totaltime); //name을 INPUT_TEXT로 날짜값들을 전달 합니다.
                setResult(RESULT_OK,intent); //요청코드를 RESULT_OK로 설정
                finish();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() { //캘린더뷰가 클릭되면 해당 리스너의
            public void onSelectedDayChange(CalendarView view, int year1, int month1, int dayOfMonth1) {
                year = year1;   //설정된 날짜의 값을  year,month, dayofMonth에 저장해주는 것입니다.
                month = month1;
                dayofmonth = dayOfMonth1;
                totaldate = Integer.toString(year)+"-"+Integer.toString(month + 1)+"-"+Integer.toString(dayofmonth);
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                minuted = minute;
                totaltime= ( Integer.toString(hour )+ ":" + Integer.toString(minuted));
            }
        });

    }

    public void canceled(View view){
        finish();
    }


}








