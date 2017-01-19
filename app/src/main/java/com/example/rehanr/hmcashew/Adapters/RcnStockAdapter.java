package com.example.rehanr.hmcashew.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rehanr.hmcashew.Models.RcnStock;
import com.example.rehanr.hmcashew.Models.TinStock;
import com.example.rehanr.hmcashew.R;

import java.util.List;

/**
 * Created by rehan r on 20-01-2017.
 */
public class RcnStockAdapter  extends RecyclerView.Adapter<RcnStockAdapter.MyViewHolder>  {



    private List<RcnStock> rcnStockList;
    Context context;

    @Override
    public RcnStockAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rcn_stock, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RcnStockAdapter.MyViewHolder holder, int position) {
        RcnStock rcnStock = rcnStockList.get(position);
        holder.tag.setText(rcnStock.getTag());
        holder.bag.setText(String.valueOf(rcnStock.getBag()));
        holder.quantity.setText(String.valueOf(rcnStock.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return rcnStockList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tag,bag,quantity;
        public MyViewHolder(View view) {
            super(view);
            tag = (TextView) view.findViewById(R.id.tag);
            bag = (TextView) view.findViewById(R.id.bags);
            quantity = (TextView)view.findViewById(R.id.qty);
        }
    }


    public RcnStockAdapter(List<RcnStock> rcnStocks, Context context) {
        this.rcnStockList = rcnStocks;
        this.context = context;
    }

}
