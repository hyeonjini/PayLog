package com.kumoh.paylog2.adapter.contents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.ListItemDto;

import java.util.ArrayList;
import java.util.List;

public class ContentsListRecyclerAdapter
        extends RecyclerView.Adapter {

    private List<ListItemDto> data;

    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_ITEM = 0;

    public ContentsListRecyclerAdapter(ArrayList<ListItemDto> data){
        this.data = data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = null;
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.item_contents_list_item,parent, false);
        return new ContentsListRecyclerAdapterItemViewHolder(view);

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ListItemDto item = data.get(position);
        ((ContentsListRecyclerAdapterItemViewHolder) holder).category.setText(item.getType());
        ((ContentsListRecyclerAdapterItemViewHolder) holder).subscribe.setText(item.getSubscribe());
        ((ContentsListRecyclerAdapterItemViewHolder) holder).amount.setText(Integer.toString(item.getAmount()));
        /*switch (holder.getItemViewType()) {
            case VIEW_TYPE_HEADER:
                ((ContentsListRecyclerAdapterHeaderViewHolder) holder).day.setText("");
                break;
            case VIEW_TYPE_ITEM:
                ((ContentsListRecyclerAdapterItemViewHolder) holder).category.setText("");
                break;
        }*/
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public static class ContentsListRecyclerAdapterItemViewHolder extends RecyclerView.ViewHolder{
        TextView category;
        TextView subscribe;
        TextView amount;
        public ContentsListRecyclerAdapterItemViewHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.item_contents_list_item_type);
            subscribe = itemView.findViewById(R.id.item_contents_list_item_subscribe);
            amount = itemView.findViewById(R.id.item_contents_list_item_amount);
        }
    }
    public static class ContentsListRecyclerAdapterHeaderViewHolder extends RecyclerView.ViewHolder{
        TextView day;
        public ContentsListRecyclerAdapterHeaderViewHolder(@NonNull View itemView){
            super(itemView);
            day = itemView.findViewById(R.id.item_contents_list_day);
        }
    }

    public void setData(List<ListItemDto> newData){
        this.data = newData;
        notifyDataSetChanged();
    }
}
