package com.kumoh.paylog2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.db.PurchaseGroup;

import java.util.List;

public class GroupListRecyclerAdapter extends RecyclerView.Adapter<GroupListRecyclerAdapter.GroupListViewHolder>{

    private GroupListRecyclerOnClickListener clickListener;
    private GroupListRecyclerLongClickListener longClickListener;
    private List<PurchaseGroup> data;
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


        //클릭 이벤트 (짧게터치)
        if(clickListener != null){
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(pos);
                }
            });
        }

        //클릭 이벤트 (길게터치)
        if(longClickListener != null){
            final int pos = position;
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onItemLongClicked(pos);
                    return false;
                }
            });
        }
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
    public PurchaseGroup getItem(int position){
        return data.get(position);
    }
    //데이터가 새로 들어옴
    public void setData(List<PurchaseGroup> groups){
       data = groups;
        notifyDataSetChanged();
    }

    //리스너 인터페이스 (짧게 터치)
    public interface GroupListRecyclerOnClickListener{
        void onItemClicked(int position);
    }
    //리스너 셋
    public void setOnClickListener(GroupListRecyclerOnClickListener clickListener){
        this.clickListener = clickListener;
    }

    //리스너 인터페이스 (길게 터치)
    public interface GroupListRecyclerLongClickListener{
        void onItemLongClicked(int position);
    }
    public void setLongClickListener(GroupListRecyclerLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }
}
