package com.kumoh.paylog2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.db.Account;

import java.util.List;

public class AccountListRecyclerAdapter extends RecyclerView.Adapter<AccountListRecyclerAdapter.AccountListViewHolder>{

    private AccountListRecyclerOnClickListener clickListener;
    private AccountListRecyclerLongClickListener longClickListener;
    private List<Account> data;
    public AccountListRecyclerAdapter(List<Account> data){
        this.data = data;
    }
    @NonNull
    @Override
    public AccountListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_list, parent, false);
        return new AccountListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountListViewHolder holder, int position) {
        Account account = data.get(position);
        holder.accountName.setText(account.getName());
        holder.budget.setText(Integer.toString(account.getBudget()));

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

    public static class AccountListViewHolder extends RecyclerView.ViewHolder{

        TextView accountName;
        TextView budget;
        public AccountListViewHolder(@NonNull View itemView) {
            super(itemView);
            accountName = itemView.findViewById(R.id.list_item_text);
            budget = itemView.findViewById(R.id.account_item_budget);
        }
    }
    public Account getItem(int position){
        return data.get(position);
    }
    //데이터가 새로 들어옴
    public void setData(List<Account> accounts){
       data = accounts;
        notifyDataSetChanged();
    }

    //리스너 인터페이스 (짧게 터치)
    public interface AccountListRecyclerOnClickListener{
        void onItemClicked(int position);
    }
    //리스너 셋
    public void setOnClickListener(AccountListRecyclerOnClickListener clickListener){
        this.clickListener = clickListener;
    }

    //리스너 인터페이스 (길게 터치)
    public interface AccountListRecyclerLongClickListener{
        void onItemLongClicked(int position);
    }
    public void setLongClickListener(AccountListRecyclerLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }
}
