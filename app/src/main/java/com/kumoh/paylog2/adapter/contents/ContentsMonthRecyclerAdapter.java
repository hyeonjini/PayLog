package com.kumoh.paylog2.adapter.contents;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.ContentsMonthItem;

import java.text.DecimalFormat;
import java.util.List;

public class ContentsMonthRecyclerAdapter extends RecyclerView.Adapter<ContentsMonthRecyclerAdapter.ContentsMonthRecyclerAdapterViewHolder> {
    private List<ContentsMonthItem> data;
    private DecimalFormat dc ;
    public ContentsMonthRecyclerAdapter(List<ContentsMonthItem> data){
        this.data = data;
        dc = new DecimalFormat("###,###,###,###,###");
    }
    @NonNull
    @Override
    public ContentsMonthRecyclerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contents_month, parent, false);
        return new ContentsMonthRecyclerAdapterViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ContentsMonthRecyclerAdapterViewHolder holder, int position){
        ContentsMonthItem item = data.get(position);
        String date = item.getDate();
        String split [] = date.split("-");
        String year = split[0];
        String month = split[1];
        holder.year.setText(year);
        holder.month.setText(month + "월");
        holder.income.setText(dc.format(item.getIncome()));
        holder.spending.setText(dc.format(item.getSpending()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ContentsMonthRecyclerAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView year, month, income, spending;
        public ContentsMonthRecyclerAdapterViewHolder(@NonNull View itemView){
            super(itemView);
            year = itemView.findViewById(R.id.item_contents_month_year);
            month = itemView.findViewById(R.id.item_contents_month_month);
            income = itemView.findViewById(R.id.item_contents_month_income);
            spending = itemView.findViewById(R.id.item_contents_month_spending);
        }
    }

    public ContentsMonthItem getItem(int position) {return data.get(position);}
    public void setData(List<ContentsMonthItem> newData){
        this.data = newData;
        notifyDataSetChanged();
    }
}
