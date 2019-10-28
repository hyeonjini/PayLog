package com.kumoh.paylog2.fragment.main;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.db.LocalDatabase;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainAccountFragment extends Fragment {

    private TextView accountName, budget, spending, income ;
    private LocalDatabase db;

    public MainAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_group, container, false);
        accountName = (TextView) rootView.findViewById(R.id.main_account_name);
        budget = (TextView) rootView.findViewById(R.id.main_account_budget);
        spending = (TextView) rootView.findViewById(R.id.main_account_spending);
        income = (TextView) rootView.findViewById(R.id.main_account_income);

        db = LocalDatabase.getInstance(getContext());
        db.accountDao().getMainAccountInfo().observe(this, accountInfo -> {
            System.out.println("여기이이이"+accountInfo.getName() + accountInfo.getIncome() + accountInfo.getSpending());
            setMainAccountInfo(accountInfo.getName(), accountInfo.getBudget(), accountInfo.getSpending(), accountInfo.getIncome());
        });

        return rootView;
    }

    public void setMainAccountInfo(String name, int budget, int spending, int income){
        DecimalFormat dc = new DecimalFormat("###,###,###,###");
        this.accountName.setText(name);
        this.budget.setText(dc.format(budget));
        this.spending.setText(dc.format(spending));
        this.income.setText(dc.format(income));
    }

}
