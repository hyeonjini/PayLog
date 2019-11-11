package com.kumoh.paylog2.adapter;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.db.Account;
import com.kumoh.paylog2.dto.AccountInfo;

import java.text.DecimalFormat;
import java.util.List;

public class AccountListRecyclerAdapter extends RecyclerView.Adapter<AccountListRecyclerAdapter.AccountListViewHolder>{

    private AccountListRecyclerOnClickListener clickListener;
    private AccountListRecyclerLongClickListener longClickListener;
    private List<AccountInfo> data;
    public AccountListRecyclerAdapter(List<AccountInfo> data){
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
        DecimalFormat dc = new DecimalFormat("###,###,###,###");
        AccountInfo account = data.get(position);

        // Main Group 의 색깔 변경
        if(position == 0) {
            holder.coloredLayout.setBackgroundColor(holder.coloredLayout.getContext().getResources().getColor(R.color.spending_Color));
        }

        holder.accountName.setText(account.getName());
        holder.budget.setText(dc.format(account.getBudget()));
        holder.income.setText(dc.format(account.getIncome()));
        holder.used.setText(dc.format(account.getSpending()));
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
                    longClickListener.onItemLongClicked(v, pos);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class AccountListViewHolder extends RecyclerView.ViewHolder{
        LinearLayout coloredLayout;
        TextView accountName;
        TextView budget;
        TextView used;
        TextView income;
        public AccountListViewHolder(@NonNull View itemView) {
            super(itemView);
            coloredLayout = itemView.findViewById(R.id.account_list_colored_layout);
            accountName = itemView.findViewById(R.id.account_list_item_name);
            budget = itemView.findViewById(R.id.account_list_item_budget);
            used = itemView.findViewById(R.id.account_list_item_used);
            income = itemView.findViewById(R.id.account_list_item_income);
        }
    }
    public AccountInfo getItem(int position){
        return data.get(position);
    }
    //데이터가 새로 들어옴
    public void setData(List<AccountInfo> accounts){

        // main group 을 찾는다.
        int mainIndex = 0;
        for(int i = 0; i < accounts.size(); i++) {
            if(accounts.get(i).isMain() == true) {
                mainIndex = i;
                break;
            }
        }

        // 첫 번째 group 과 main group 의 위치를 바꾼다.
        // 만약, 첫 번째 group 이 main group 이라면 pass
        if(mainIndex != 0) {
            AccountInfo temp = accounts.get(0);
            accounts.set(0, accounts.get(mainIndex));
            accounts.set(mainIndex, temp);
        }

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
        void onItemLongClicked(View view, int position);
    }
    public void setLongClickListener(AccountListRecyclerLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }
}
