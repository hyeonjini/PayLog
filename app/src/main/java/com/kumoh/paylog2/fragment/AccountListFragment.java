package com.kumoh.paylog2.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kumoh.paylog2.R;
import com.kumoh.paylog2.activity.ContentsActivity;
import com.kumoh.paylog2.adapter.AccountListRecyclerAdapter;
import com.kumoh.paylog2.db.Account;
import com.kumoh.paylog2.db.AccountDao;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.dialog.AddAccountDialog;

import java.util.ArrayList;
import java.util.List;

public class AccountListFragment extends Fragment implements AccountListRecyclerAdapter.AccountListRecyclerOnClickListener {

    private LocalDatabase db;
    private AccountListRecyclerAdapter adapter;
    public AccountListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list_group, container, false);

        List<Account> accountList = new ArrayList<>();
        RecyclerView recyclerView = rootView.findViewById(R.id.account_list_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AccountListRecyclerAdapter(accountList);
        //리스너 등록
        adapter.setOnClickListener(this);

        recyclerView.setAdapter(adapter);
        //db 연결
        db = LocalDatabase.getInstance(getContext());
        db.accountDao().getAll().observe(this, list->{
            System.out.println(list.size()+"개");
            adapter.setData(list);
        });

        //FloatingActionButton 설정
        FloatingActionButton groupAddFab = (FloatingActionButton) rootView.findViewById(R.id.add_account_fab);
        groupAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.add_account_fab:
                        AddAccountDialog dialog = new AddAccountDialog(getContext());
                        dialog.setListener(new AddAccountDialog.AddAccountDialogListener() {
                            @Override
                            public void onAddButtonClicked(int budget,String AccountName, String subscribe, boolean isMain) {
                                new InsertAccountAsyncTask(db.accountDao()).execute(new Account(budget,AccountName,subscribe, false));
                            }
                            @Override
                            public void onCancelButtonClicked() {

                            }
                        });
                        dialog.show();
                        break;
                }
            }
        });
        return rootView;
    }
    //DB 접근 비동기 처리
    private static class InsertAccountAsyncTask extends AsyncTask<Account, Void, Void>{

        private AccountDao dao;
        public InsertAccountAsyncTask(AccountDao dao){
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Account... accounts) {
            dao.insertAccount(accounts[0]);

            return null;
        }
    }

    //리스너 동작 구현
    //짧게 터치
    @Override
    public void onItemClicked(int position){
        Account account = null;
        account = adapter.getItem(position);

        Toast.makeText(getContext(), "그룹id : "+account.getAccountId(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ContentsActivity.class);
        intent.putExtra("selectedGroupId",Integer.toString(account.getAccountId()));
        startActivity(intent);
    }

    //길게 터치

}
