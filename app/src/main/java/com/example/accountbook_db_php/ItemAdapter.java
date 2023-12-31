package com.example.accountbook_db_php;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<ItemData> items;

    public ItemAdapter(Context context, ArrayList<ItemData> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.recycler_item,parent,false);

        VH holder = new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH) holder;
        ItemData item = items.get(position);
        vh.tvMoney.setText(String.valueOf(item.getMoney()));
        vh.tvMemo.setText(item.getMeno());
        if(item.getGubn().equals("1")){
            vh.tvGubn.setText("수입");
            vh.itembg.setBackgroundColor(Color.parseColor("#96FFFF"));
        }else if(item.getGubn().equals("2")){
            vh.tvGubn.setText("지출");
            vh.itembg.setBackgroundColor(Color.parseColor("#DC6089"));
        }
        vh.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("저장 서버실행 확인");
                String serverUrl = "http://222.104.195.229/AccountBook_delete.php";
                StringRequest request = new StringRequest(Request.Method.POST, serverUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                ((MainActivity)MainActivity.mContext).getsumData();
                                ((MainActivity)MainActivity.mContext).getData();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap();

                        params.put("date",item.getIn_date());
                        params.put("seq", String.valueOf(item.getSeq()));


                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                request.setShouldCache(false);
                requestQueue.add(request);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        TextView tvDate;
        TextView tvMemo;
        TextView tvGubn;
        TextView tvMoney;
        Button delBtn;
        ConstraintLayout itembg;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvMemo = itemView.findViewById(R.id.memo);
            tvGubn = itemView.findViewById(R.id.gubn);
            tvMoney = itemView.findViewById(R.id.money);
            delBtn = itemView.findViewById(R.id.delBtn);
            itembg = itemView.findViewById(R.id.itembg);
        }
    }

}
