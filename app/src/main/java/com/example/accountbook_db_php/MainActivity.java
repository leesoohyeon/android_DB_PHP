package com.example.accountbook_db_php;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static Context mContext;
    TextView dateText;
    DatePickerDialog datePickerDialog;
    RecyclerView recyclerView;
    ArrayList<ItemData> items = new ArrayList<>();
    ItemAdapter adapter;
    String tvDate = new Unit().today();
    String Result_date = new Unit().today().replace("/","");
    String gubn = "";
    String rMemo = "";
    String rMoney = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateText = findViewById(R.id.dateText);
        dateText.setText(tvDate);
        Button datePickerBtn = findViewById(R.id.dateButton);
        Button gubnBtn = findViewById(R.id.Btngubn);
        Button insertBtn = findViewById(R.id.insertBtn);
        mContext = this;
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gubn.equals("선택")){
                    Toast.makeText(MainActivity.this,"지출구분을 선택하여주세요.",Toast.LENGTH_SHORT);
                    return;
                }else{
                    EditText etMemo = findViewById(R.id.etMemo);
                    EditText etMoney = findViewById(R.id.etMoney);
                    rMemo = String.valueOf(etMemo.getText());
                    rMoney = String.valueOf(etMoney.getText());
                    insertData();
                }
            }
        });

        gubnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
                getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.menu_min){
                            gubn = "2";
                            gubnBtn.setText("지출");
                        }else if(item.getItemId() == R.id.menu_plus){
                            gubn = "1";
                            gubnBtn.setText("수입");
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ItemAdapter(this, items);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        getData();
        getsumData();

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
                        String rMonth = "";
                        if(month<10){
                            rMonth = "0"+month;
                        }else{
                            rMonth = String.valueOf(month);
                        }
                        String date = year +"/" + rMonth +"/"+dayOfMonth;
                        dateText.setText(date);
                        Result_date = String.valueOf(year) + rMonth + dayOfMonth;
                        getData();
                        getsumData();
                    }
                }, pYear,pMonth,pDay);
                datePickerDialog.show();
            } //onClick

        });

    }


    public void insertData(){
        System.out.println("저장 서버실행 확인");
        String serverUrl = "http://222.104.195.229/AccountBook_insert.php";
        StringRequest request = new StringRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,"입력완료",Toast.LENGTH_SHORT).show();
                        getData();
                        getsumData();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap();

                params.put("date",Result_date);
                params.put("memo",rMemo);
                params.put("gubn",gubn);
                params.put("money",rMoney);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    public void getData(){
        System.out.println("서버 실행확인1");
        // 서버 주소
        String serverUrl = "http://222.104.195.229/AccountBook_select.php";
        StringRequest request = new StringRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        items.clear();
                        adapter.notifyDataSetChanged();
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String date = jsonObject.getString("in_date");
                                String gubn = jsonObject.getString("gubn");
                                String memo = jsonObject.getString("memo");
                                int money = Integer.parseInt( jsonObject.getString("money"));
                                int seq = Integer.parseInt(jsonObject.getString("seq"));
                                items.add(0,new ItemData(date,seq,gubn,memo,money));
                                adapter.notifyItemInserted(0);
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("date",Result_date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setShouldCache(false);
        requestQueue.add(request);

    }

    public void getsumData(){
        // 서버 주소
        String serverUrl = "http://222.104.195.229/AccountBook_sum.php";

        StringRequest request = new StringRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String sum = jsonObject.getString("sum");
                                String min = jsonObject.getString("min");
                                String plus = jsonObject.getString("plus");
                                TextView tvsum = (TextView) findViewById(R.id.money_sum);
                                TextView tvmin = (TextView) findViewById(R.id.money_min);
                                TextView tvplus = (TextView) findViewById(R.id.money_plus);
                                tvsum.setText(sum);
                                tvmin.setText(min);
                                tvplus.setText(plus);
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("date",Result_date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setShouldCache(false);
        requestQueue.add(request);

    }

}