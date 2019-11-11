package com.kumoh.paylog2.fragment.main;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.activity.ContentsActivity;
import com.kumoh.paylog2.adapter.contents.ContentsStatisticsRecyclerAdapter;
import com.kumoh.paylog2.db.Category;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.dto.ContentsCategoryItem;
import com.kumoh.paylog2.dto.ContentsStatisticsCategoryItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainAccountFragment extends Fragment implements View.OnClickListener {

    private CardView accountInfoCardView;
    private int accountId;
    private TextView accountName, budget, spending, income ;
    private LocalDatabase db;
    private List<ContentsStatisticsCategoryItem> list;
    private List<ContentsCategoryItem> spendingCategories;
    private RecyclerView recyclerView;
    private ContentsStatisticsRecyclerAdapter adapter;

    public MainAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_group, container, false);

        accountInfoCardView = rootView.findViewById(R.id.main_account_info_cardview);
        recyclerView = rootView.findViewById(R.id.main_category_recycler);

        accountName = (TextView) rootView.findViewById(R.id.main_account_name);
        budget = (TextView) rootView.findViewById(R.id.main_account_budget);
        spending = (TextView) rootView.findViewById(R.id.main_account_spending);
        income = (TextView) rootView.findViewById(R.id.main_account_income);

        adapter = new ContentsStatisticsRecyclerAdapter(new ArrayList<>(), new ArrayList<>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        db = LocalDatabase.getInstance(getContext());

        db.accountDao().getMainAccountInfo().observe(this, accountInfo -> {
            setMainAccountInfo(accountInfo.getAccountId(), accountInfo.getName(), accountInfo.getBudget(), accountInfo.getSpending(), accountInfo.getIncome());

            db.historyDao().getGroupedListByCategory(accountId).observe(this,list->{
                makeItemList(list);
                adapter.setData(this.list);
            });
        });

        db.categoryDao().getSpendingCategories().observe(this, list->{
            for(int pos = 0; pos < list.size(); pos++){
                initCategories(list);
            }
            adapter.setCategories(this.spendingCategories);
        });

        accountInfoCardView.setOnClickListener(this);

        return rootView;
    }

    private void setMainAccountInfo(int id, String name, int budget, int spending, int income){
        DecimalFormat dc = new DecimalFormat("###,###,###,###");
        this.accountId = id;
        this.accountName.setText(name);
        this.budget.setText(dc.format(budget));
        this.spending.setText(dc.format(spending));
        this.income.setText(dc.format(income));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.main_account_info_cardview:
                Intent intent = new Intent(getActivity(), ContentsActivity.class);
                intent.putExtra("selectedGroupId",Integer.toString(accountId));
                startActivity(intent);
        }
    }

    private void initCategories(List<Category> list){
        spendingCategories = new ArrayList<>();
        for(int pos = 0; pos < list.size(); pos++){
            spendingCategories.add(new ContentsCategoryItem(list.get(pos).getCategoryId(), list.get(pos).getName(), list.get(pos).getKind()));
        }
    }

    private void makeItemList(List<ContentsStatisticsCategoryItem> newData){
        if(newData.size() > 0){
            list = new ArrayList<>();
            ContentsStatisticsCategoryItem temp = new ContentsStatisticsCategoryItem(0, 0, 1);
            for(ContentsStatisticsCategoryItem h : newData){
                if(h.getKind() == 1 || h.getKind() == 3){
                    Log.i("크기", " "+list.size());
                    if(list.size() > 5){
                        temp.setAmount(temp.getAmount() - h.getAmount());
                    }else{
                        list.add(new ContentsStatisticsCategoryItem(-h.getAmount(), h.getCategoryId(), h.getKind()));
                    }
                }
            }
            if(temp.getAmount() != 0)
                list.add(temp);
        }else{
            list = new ArrayList<>();
            list.add(new ContentsStatisticsCategoryItem(0,-1, 1));
        }
    }
}
