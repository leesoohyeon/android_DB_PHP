package com.example.accountbook_db_php;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Unit {
    public String today(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String result_date = formatter.format(date);
        return result_date;
    }
}
