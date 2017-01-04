package com.example.rehanr.hmcashew.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rehanr.hmcashew.Models.TinStock;
import com.example.rehanr.hmcashew.R;

import java.util.List;

/**
 * Created by rehan r on 03-01-2017.
 */
public class KernalStockAdapter extends RecyclerView.Adapter<KernalStockAdapter.MyViewHolder> {


    private List<TinStock> tinstockList;
    Context context;

    @Override
    public KernalStockAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kernalstock, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(KernalStockAdapter.MyViewHolder holder, int position) {
        TinStock tinStock = tinstockList.get(position);
        holder.gradeName.setText(tinStock.getGradeName());
        holder.tins.setText(tinStock.getTins()+" Tins");
    }

    @Override
    public int getItemCount() {
        return tinstockList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView gradeName,lotNo,gradetype,tins;
        public MyViewHolder(View view) {
            super(view);
            gradeName = (TextView) view.findViewById(R.id.grade);
            tins = (TextView) view.findViewById(R.id.totaltins);
        }
    }


    public KernalStockAdapter(List<TinStock> tinStockList, Context context) {
        this.tinstockList = tinStockList;
        this.context = context;
    }
}
