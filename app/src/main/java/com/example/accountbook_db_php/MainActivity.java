package com.example.accountbook_db_php;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView dateText;
    DatePickerDialog datePickerDialog;
    RecyclerView recyclerView;
    ArrayList<ItemData> items = new ArrayList<>();
    ItemAdapter adapter;
    String tvDate = new Unit().today();
    String Result_date = new Unit().today().replace("/","");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("dddddd",tvDate);
        Log.d("dddddd",Result_date);
        dateText = findViewById(R.id.dateText);
        dateText.setText(tvDate);
        Button datePickerBtn = findViewById(R.id.dateButton);

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
                        String date = year +"/" + month +"/"+dayOfMonth;
                        dateText.setText(date);
                        Result_date = String.valueOf(year + month + dayOfMonth);

                    }
                }, pYear,pMonth,pDay);
                datePickerDialog.show();

            } //onClick
        });

    }

    public void getData(){
        System.out.println("서버 실행확인1");
        // 서버 주소
        String serverUrl = "http://222.104.195.229/PHP_connection.php";
        StringRequest request = new StringRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("서버 실행확인2");
                        Log.d("GSON 응답 ",response);
                        items.clear();
                        adapter.notifyDataSetChanged();
                        try{
                            Log.d("GSON 응답 ","실행확인222");
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
                params.put("date","20230723");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setShouldCache(false);
        requestQueue.add(request);

    }

    public void getsumData(){
        System.out.println("서버 실행확인1");
        // 서버 주소
        String serverUrl = "http://222.104.195.229/AccountBook_sum.php";

        StringRequest request = new StringRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("서버 실행확인2");
                        Log.d("GSON 응답 ",response);
                        try{
                            Log.d("GSON 응답 ","실행확인222");
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.d("GSON 응답 ",jsonObject.getString("sum"));
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
                params.put("date","20230723");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setShouldCache(false);
        requestQueue.add(request);

    }

}