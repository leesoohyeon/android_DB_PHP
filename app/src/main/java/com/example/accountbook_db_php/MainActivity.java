package com.example.accountbook_db_php;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView dateText;
    DatePickerDialog datePickerDialog;
    RecyclerView recyclerView;
    ArrayList<ItemData> items = new ArrayList<>();
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateText = findViewById(R.id.dateText);
        Button datePickerBtn = findViewById(R.id.dateButton);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ItemAdapter(this, items);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

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
        getData();
        getsumData();
    }

    public void getData(){
        System.out.println("서버 실행확인1");
        // 서버 주소
        String serverUrl = "http://219.248.38.128/PHP_connection.php";


        // 결과를 JsonArray 받을 것이므로
        // StringRequest가 아니라
        // JsonArrayRequest를 이용할 것임

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, serverUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("서버 실행확인2");
                items.clear();
                adapter.notifyDataSetChanged();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String date = jsonObject.getString("in_date");
                        String gubn = jsonObject.getString("gubn");
                        String memo = jsonObject.getString("memo");
                        int money = Integer.parseInt( jsonObject.getString("money"));
                        int seq = Integer.parseInt(jsonObject.getString("seq"));
                        items.add(0,new ItemData(date,seq,gubn,memo,money));
                        adapter.notifyItemInserted(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
            }
        });

        // 실제 요청 작업을 수행해주는 요청큐 객체 생성
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        // 요청큐에 요청 객체 생성
        requestQueue.add(jsonArrayRequest);
    }

    public void getsumData(){
        System.out.println("서버 실행확인1");
        // 서버 주소
        String serverUrl = "http://219.248.38.128/AccountBook_sum.php";


        // 결과를 JsonArray 받을 것이므로
        // StringRequest가 아니라
        // JsonArrayRequest를 이용할 것임

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, serverUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("서버 실행확인2");
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String date = jsonObject.getString("in_date");
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
            }
        });

        // 실제 요청 작업을 수행해주는 요청큐 객체 생성
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        // 요청큐에 요청 객체 생성
        requestQueue.add(jsonArrayRequest);
    }

}