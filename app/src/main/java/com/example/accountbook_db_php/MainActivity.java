package com.example.accountbook_db_php;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView dateText;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateText = findViewById(R.id.dateText);
        Button datePickerBtn = findViewById(R.id.dateButton);

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int pYear = calendar.get(Calendar.YEAR); //년
                int pMonth = calendar.get(Calendar.MONTH); //월
                int pDay = calendar.get(Calendar.DAY_OF_MONTH); //일
                datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // 1월은 0부터 시작하기 때문에 +1을 해준다.
                        month = month +1;
                        String date = year +"/" + month +"/"+dayOfMonth;
                        dateText.setText(date);

                    }
                }, pYear,pMonth,pDay);
                datePickerDialog.show();
            } //onClick
        });

    }
}