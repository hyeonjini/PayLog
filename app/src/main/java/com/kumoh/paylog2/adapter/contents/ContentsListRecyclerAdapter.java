package com.kumoh.paylog2.adapter.contents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.ContentsListBody;
import com.kumoh.paylog2.dto.ContentsListItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ContentsListRecyclerAdapter
        extends RecyclerView.Adapter {

    private List<ContentsListItem> data;
    private DecimalFormat dc = new DecimalFormat("###,###,###,###,###");
    private static final int VIEW_TYPE_HEADER = 0;
    final static int SPENDING_VIEW = -1;
    final static int INCOME_VIEW = 1;

    public ContentsListRecyclerAdapter(ArrayList<ContentsListItem> data){

        this.data = data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contents_header, parent, false);
            return new ContentsListRecyclerAdapterHeaderViewHolder(v);
        } else if (viewType == SPENDING_VIEW){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contents_list_spending , parent, false);

            return new ContentsListRecyclerAdapterSpendingViewHolder(v);
        }else if(viewType == INCOME_VIEW){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contents_list_income , parent, false);
            return new ContentsListRecyclerAdapterIncomeViewHolder(v);
        }
        return null;
    }
    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getViewType() == 0) {
            return VIEW_TYPE_HEADER;
        } else if (data.get(position).getViewType() == 1){
            return INCOME_VIEW;
        } else if(data.get(position).getViewType() == -1){
            return SPENDING_VIEW;
        }
        return 0;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContentsListItem item = data.get(position);
        if (holder instanceof ContentsListRecyclerAdapterHeaderViewHolder){
            ((ContentsListRecyclerAdapterHeaderViewHolder) holder).date.setText(item.getDate());
            //((ContentsListRecyclerAdapterHeaderViewHolder) holder).income.setText(Integer.toString(item.getIncome()));
            ((ContentsListRecyclerAdapterHeaderViewHolder) holder).income.setText(dc.format(item.getIncome()));
            //((ContentsListRecyclerAdapterHeaderViewHolder) holder).spending.setText(Integer.toString(item.getSepnding()));
            ((ContentsListRecyclerAdapterHeaderViewHolder) holder).spending.setText(dc.format(item.getSepnding()));
        }
        else if (holder instanceof ContentsListRecyclerAdapterSpendingViewHolder){

            //((ContentsListRecyclerAdapterSpendingViewHolder) holder).category.setText(Integer.toString(((ContentsListBody) item).getCategoryId()));
            ((ContentsListRecyclerAdapterSpendingViewHolder) holder).category.setText("지출");
            //((ContentsListRecyclerAdapterSpendingViewHolder) holder).spending.setText(Integer.toString(((ContentsListBody) item).getAmount()));
            ((ContentsListRecyclerAdapterSpendingViewHolder) holder).spending.setText(dc.format(((ContentsListBody) item).getAmount()));
            ((ContentsListRecyclerAdapterSpendingViewHolder) holder).description.setText(((ContentsListBody) item).getDescription());
        }
        else if (holder instanceof ContentsListRecyclerAdapterIncomeViewHolder){
            //((ContentsListRecyclerAdapterIncomeViewHolder) holder).category.setText(Integer.toString(((ContentsListBody) item).getCategoryId()));
            ((ContentsListRecyclerAdapterIncomeViewHolder) holder).category.setText("수입");
            //((ContentsListRecyclerAdapterIncomeViewHolder) holder).income.setText(Integer.toString(((ContentsListBody) item).getAmount()));
            ((ContentsListRecyclerAdapterIncomeViewHolder) holder).income.setText(dc.format(((ContentsListBody) item).getAmount()));
            ((ContentsListRecyclerAdapterIncomeViewHolder) holder).description.setText(((ContentsListBody) item).getDescription());
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ContentsListRecyclerAdapterHeaderViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView income;
        TextView spending;
        public ContentsListRecyclerAdapterHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.item_contents_list_date);
            income =itemView.findViewById(R.id.item_contents_list_income);
            spending =itemView.findViewById(R.id.item_contents_list_spending);
        }
    }
    public static class ContentsListRecyclerAdapterSpendingViewHolder extends RecyclerView.ViewHolder{
        TextView category, description, spending;
        public ContentsListRecyclerAdapterSpendingViewHolder(@NonNull View itemView){
            super(itemView);
            category = itemView.findViewById(R.id.item_contents_list_item_category);
            description = itemView.findViewById(R.id.item_contents_list_item_description);
            spending = itemView.findViewById(R.id.item_contents_list_item_spending);
        }
    }
    public static class ContentsListRecyclerAdapterIncomeViewHolder extends RecyclerView.ViewHolder{
        TextView category, description, income;
        public ContentsListRecyclerAdapterIncomeViewHolder(@NonNull View itemView){
            super(itemView);
            category = itemView.findViewById(R.id.item_contents_list_income_category);
            description = itemView.findViewById(R.id.item_contents_list_income_description);
            income = itemView.findViewById(R.id.item_contents_list_income_income);
        }
    }
    public void setData(List<ContentsListItem> newData){
        this.data = newData;
        notifyDataSetChanged();
    }
}
