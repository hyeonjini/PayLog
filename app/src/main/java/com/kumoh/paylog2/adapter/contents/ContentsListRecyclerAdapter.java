package com.kumoh.paylog2.adapter.contents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.db.Spending;
import com.kumoh.paylog2.dto.ContentsListHeader;

import java.util.ArrayList;

public class ContentsListRecyclerAdapter
        extends RecyclerView.Adapter {

    private ArrayList<Spending> data;
    private ArrayList<ContentsListHeader> header;

    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_ITEM = 0;

    public ContentsListRecyclerAdapter(ArrayList<Spending> data, ArrayList<ContentsListHeader> header){
        this.data = data;
        this.header = header;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = null;
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == VIEW_TYPE_HEADER){
            view = inflater.inflate(R.layout.item_contents_header, parent, false);
            return new ContentsListRecyclerAdapterHeaderViewHolder(view);
        }else if (viewType == VIEW_TYPE_ITEM) {
            view = inflater.inflate(R.layout.item_contents_list_item, parent, false);
            return new ContentsListRecyclerAdapterItemViewHolder(view);
        } throw new RuntimeException("좆됨");
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_HEADER:
                ((ContentsListRecyclerAdapterHeaderViewHolder) holder).day.setText("");
                break;
            case VIEW_TYPE_ITEM:
                ((ContentsListRecyclerAdapterItemViewHolder) holder).category.setText("");
                break;
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public static class ContentsListRecyclerAdapterItemViewHolder extends RecyclerView.ViewHolder{
        TextView category;
        public ContentsListRecyclerAdapterItemViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.item_contents_list_item_category);
        }
    }
    public static class ContentsListRecyclerAdapterHeaderViewHolder extends RecyclerView.ViewHolder{
        TextView day;
        public ContentsListRecyclerAdapterHeaderViewHolder(@NonNull View itemView){
            super(itemView);
            day = itemView.findViewById(R.id.item_contents_list_day);
        }
    }
}
