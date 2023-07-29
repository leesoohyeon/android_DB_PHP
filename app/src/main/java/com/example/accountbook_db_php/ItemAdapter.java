package com.example.accountbook_db_php;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        }else if(item.getGubn().equals("2")){
            vh.tvGubn.setText("지출");
        }

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

        public VH(@NonNull View itemView) {
            super(itemView);
            tvMemo = itemView.findViewById(R.id.memo);
            tvGubn = itemView.findViewById(R.id.gubn);
            tvMoney = itemView.findViewById(R.id.money);
        }
    }

}
