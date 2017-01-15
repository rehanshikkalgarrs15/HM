package com.example.rehanr.hmcashew.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rehanr.hmcashew.Models.DealerPendingPayment;
import com.example.rehanr.hmcashew.Models.KernalRates;
import com.example.rehanr.hmcashew.R;

import java.util.List;

/**
 * Created by rehan r on 15-01-2017.
 */
public class AllDealerPendingPaymentAdapter extends RecyclerView.Adapter<AllDealerPendingPaymentAdapter.MyViewHolder>{


    private List<DealerPendingPayment> dealerPending;
    Context context;


    public AllDealerPendingPaymentAdapter(List<DealerPendingPayment> dealerPendingPayments, Context context) {
        this.dealerPending = dealerPendingPayments;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dealerName,pendingAmount;
        public MyViewHolder(View view) {
            super(view);
            dealerName = (TextView) view.findViewById(R.id.dealername);
            pendingAmount = (TextView)view.findViewById(R.id.pendingamount);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/DroidSans.ttf");
            dealerName.setTypeface(tf);
            pendingAmount.setTypeface(tf);
        }
    }

    @Override
    public AllDealerPendingPaymentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alldealerpendingpayments, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AllDealerPendingPaymentAdapter.MyViewHolder holder, int position) {
        DealerPendingPayment dealer = dealerPending.get(position);
        holder.dealerName.setText(dealer.getDealerName());
        holder.pendingAmount.setText(String.valueOf(dealer.getPendingAmount()));
    }

    @Override
    public int getItemCount() {
        return dealerPending.size();
    }
}
