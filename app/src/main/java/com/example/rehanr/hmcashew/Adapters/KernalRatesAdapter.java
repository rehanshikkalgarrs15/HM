package com.example.rehanr.hmcashew.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rehanr.hmcashew.Fragments.KernalRatesFragment;
import com.example.rehanr.hmcashew.Models.KernalRates;
import com.example.rehanr.hmcashew.Models.TinStock;
import com.example.rehanr.hmcashew.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rehan r on 12-01-2017.
 */
public class KernalRatesAdapter extends RecyclerView.Adapter<KernalRatesAdapter.MyViewHolder> {



    private List<KernalRates> kernalRatesList;
    Context context;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kernalrates, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        KernalRates kernalRate = kernalRatesList.get(position);
        holder.gradeName.setText(kernalRate.getGradeName());

        String[] date1Array = kernalRate.getDate1().split(" ");
        String[] date2Array = kernalRate.getDate2().split(" ");

        //change date format from  yyyy-mm-dd to dd-mmm-yyyy in java
        String Date1 = changeDateFormat(date1Array[0]);
        String Date2 = changeDateFormat(date2Array[0]);
        holder.date1.setText(Date1);
        holder.date2.setText(Date2);
        holder.rate1.setText("Rs. " + String.valueOf(kernalRate.getRate1()));
        holder.rate2.setText("Rs. " + String.valueOf(kernalRate.getRate2()));
    }

    private String changeDateFormat(String changeDate) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-DD");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMMM-yyyy");
        Date date = null;
        try {
            date = format1.parse(changeDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String dateString = format2.format(date);
        dateString = dateString.replace("-", " ");
        return dateString;

    }


    @Override
    public int getItemCount() {
        return kernalRatesList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView gradeName,date1,date2,rate1,rate2;
        public MyViewHolder(View view) {
            super(view);
            gradeName = (TextView) view.findViewById(R.id.gradename);
            date1 = (TextView)view.findViewById(R.id.date1tag);
            date2 = (TextView)view.findViewById(R.id.date2tag);
            rate1 = (TextView)view.findViewById(R.id.rate1);
            rate2 = (TextView)view.findViewById(R.id.rate2);
        }
    }


    public KernalRatesAdapter(List<KernalRates> kernalRates, Context context) {
        this.kernalRatesList = kernalRates;
        this.context = context;
    }

}
