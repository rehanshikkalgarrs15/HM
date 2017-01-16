package com.example.rehanr.hmcashew.Adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rehanr.hmcashew.Models.DealerPendingPayment;
import com.example.rehanr.hmcashew.Models.TinStock;
import com.example.rehanr.hmcashew.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rehan r on 15-01-2017.
 */
public class DealerPendingPaymentAdapter extends RecyclerView.Adapter<DealerPendingPaymentAdapter.MyViewHolder>  {


    private List<DealerPendingPayment> pendingPayments;
    Context context;


    @Override
    public DealerPendingPaymentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dealerpendingpayment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DealerPendingPaymentAdapter.MyViewHolder holder, int position) {
        DealerPendingPayment dealer = pendingPayments.get(position);
        holder.billno.setText(String.valueOf(dealer.getBillNo()));
        holder.billdate.setText(changeDateFormat(dealer.getBilldate()));
        holder.quantity.setText(String.valueOf(dealer.getTotalQuantity()));
        holder.totalamount.setText( String.valueOf(dealer.getBillamount()));
        holder.totalpaid.setText(String.valueOf(dealer.getPaymentMade()));
        holder.totalpending.setText( String.valueOf(dealer.getPendingAmount()));
    }

    @Override
    public int getItemCount() {
        return pendingPayments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dealername,billno,billdate,quantity,ppdate,totalamount,totalpaid,totalpending,
                        billnotag,billdatetag,quantitytag,ppdatetag,totalamounttag,totalpaidtag,totalpendingtag;
        public MyViewHolder(View view) {
            super(view);
            billno = (TextView)view.findViewById(R.id.billno);
            billdate = (TextView)view.findViewById(R.id.billdate);
            quantity = (TextView)view.findViewById(R.id.totalquantity);
            totalamount = (TextView)view.findViewById(R.id.totalamount);
            totalpaid = (TextView)view.findViewById(R.id.totalpaid);
            totalpending = (TextView)view.findViewById(R.id.totalpending);

            setFontforItem(context,view.findViewById(R.id.itemlayout));

        }
    }


    //set custom font to all textviews in the recycler item
    private void setFontforItem(Context context, View root) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    setFontforItem(context, viewGroup.getChildAt(i));
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), context.getString(R.string.font)));
        } catch (Exception e) {
            Log.e("ProjectName", String.format("Error occured when trying to apply %s font for %s view", "DroidSans", root));
            e.printStackTrace();
        }
    }


    public DealerPendingPaymentAdapter(List<DealerPendingPayment> dealerPendingPaymentList, Context context) {
        this.pendingPayments = dealerPendingPaymentList;
        this.context = context;
    }

    private String changeDateFormat(String selectedDATE) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-DD");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = format1.parse(selectedDATE);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String dateString = format2.format(date);
        dateString = dateString.replace("-", " ");
        return dateString;
    }

}
