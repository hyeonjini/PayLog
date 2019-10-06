package com.kumoh.paylog2.adapter.contents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.db.Spending;

import java.util.ArrayList;

public class ContentsListRecyclerAdapter extends RecyclerView.Adapter<ContentsListRecyclerAdapter.ContentsListRecyclerAdapterViewHolder> {

    private ArrayList<Spending> data;
    public ContentsListRecyclerAdapter(ArrayList<Spending> data){
        this.data = data;
    }
    @NonNull
    @Override
    public ContentsListRecyclerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contents_list, parent,false);
        return new ContentsListRecyclerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentsListRecyclerAdapterViewHolder holder, int position) {
        Spending spending = data.get(position);
        holder.day.setText(spending.getDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ContentsListRecyclerAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView day;
        public ContentsListRecyclerAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.item_contents_list_day);
        }
    }
}
