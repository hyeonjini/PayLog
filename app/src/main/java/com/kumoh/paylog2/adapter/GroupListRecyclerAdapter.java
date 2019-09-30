package com.kumoh.paylog2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.db.PurchaseGroup;

import java.security.acl.Group;
import java.util.List;

public class GroupListRecyclerAdapter extends RecyclerView.Adapter<GroupListRecyclerAdapter.GroupListViewHolder>{

    List<PurchaseGroup> data;
    public GroupListRecyclerAdapter(List<PurchaseGroup> data){
        this.data = data;
    }
    @NonNull
    @Override
    public GroupListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_list, parent, false);
        return new GroupListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListViewHolder holder, int position) {
        PurchaseGroup group = data.get(position);
        holder.groupName.setText(group.getGroupName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class GroupListViewHolder extends RecyclerView.ViewHolder{

        TextView groupName;

        public GroupListViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.list_item_text);
        }
    }

    //데이터가 새로 들어옴
    public void setData(List<PurchaseGroup> groups){
       data = groups;
        notifyDataSetChanged();
    }
}
