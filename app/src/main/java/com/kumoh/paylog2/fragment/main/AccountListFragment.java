package com.kumoh.paylog2.fragment.main;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kumoh.paylog2.R;
import com.kumoh.paylog2.activity.ContentsActivity;
import com.kumoh.paylog2.adapter.AccountListRecyclerAdapter;
import com.kumoh.paylog2.db.Account;
import com.kumoh.paylog2.db.AccountDao;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.dialog.AddAccountDialog;
import com.kumoh.paylog2.dto.AccountInfo;
import com.kumoh.paylog2.util.MyException;

import java.util.ArrayList;
import java.util.List;

public class AccountListFragment extends Fragment implements AccountListRecyclerAdapter.AccountListRecyclerOnClickListener, AccountListRecyclerAdapter.AccountListRecyclerLongClickListener {
    private LocalDatabase db;
    private AccountListRecyclerAdapter adapter;

    FloatingActionButton groupAddFab;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = true;


    public AccountListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list_group, container, false);

        List<AccountInfo> accountList = new ArrayList<>();
        RecyclerView recyclerView = rootView.findViewById(R.id.account_list_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AccountListRecyclerAdapter(accountList);
        //리스너 등록
        adapter.setOnClickListener(this);
        adapter.setLongClickListener(this);

        recyclerView.setAdapter(adapter);
        //db 연결
        db = LocalDatabase.getInstance(getContext());
        db.accountDao().getAccountInfoList().observe(this, list->{
            adapter.setData(list);
        });

        recyclerView.addOnScrollListener(onScrollListener);

        //FloatingActionButton 설정
        groupAddFab = (FloatingActionButton) rootView.findViewById(R.id.add_account_fab);
        groupAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.add_account_fab:
                        AddAccountDialog dialog = new AddAccountDialog(getContext());
                        dialog.setListener(new AddAccountDialog.AddAccountDialogListener() {
                            @Override
                            public void onAddButtonClicked(int budget,String AccountName, String subscribe, boolean isMain) {
                                new InsertAccountAsyncTask(db.accountDao()).execute(new Account(budget,AccountName,subscribe, 0));
                            }
                            @Override // not used
                            public void onReviseButtonClicked(int accountId, int budget, String accountName, String subscribe) {}
                        });
                        dialog.show();
                        break;
                }
            }
        });

        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);

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

    private static class UpdateAccountAsyncTask extends AsyncTask<AccountInfo, Void, Void> {
        private AccountDao dao;
        public UpdateAccountAsyncTask(AccountDao dao) { this.dao = dao;}

        @Override
        protected Void doInBackground(AccountInfo... accountInfos) {
            dao.updateAccount(accountInfos[0].getAccountId(), accountInfos[0].getName(),
                    accountInfos[0].getSubscribe(), accountInfos[0].getBudget());

            return null;
        }
    }

    private static class DeleteAccountAsyncTask extends AsyncTask<Integer, Void, Void> {
        private AccountDao dao;
        public DeleteAccountAsyncTask(AccountDao dao) { this.dao = dao; }

        @Override
        protected Void doInBackground(Integer... accounts) {
            dao.deleteAccountById(accounts[0]);

            return null;
        }
    }

    private static class WipeOutAndAppointMainAccountAsyncTask extends AsyncTask<Integer, Void, Void> {
        private AccountDao dao;
        public WipeOutAndAppointMainAccountAsyncTask(AccountDao dao) { this.dao = dao;}

        @Override
        protected Void doInBackground(Integer... integers) {
            dao.wipeOutAndAppointMainAcocuntById(integers[0]);

            return null;
        }
    }

    //리스너 동작 구현
    //짧게 터치
    @Override
    public void onItemClicked(int position) {
        AccountInfo account = null;
        account = adapter.getItem(position);

        Toast.makeText(getContext(), "그룹id : "+account.getAccountId(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ContentsActivity.class);
        intent.putExtra("selectedGroupId",Integer.toString(account.getAccountId()));
        startActivity(intent);
    }

    //길게 터치
    @Override
    public void onItemLongClicked(View view, int position) {
        PopupMenu popup=new PopupMenu(getActivity(), view);
        popup.getMenuInflater().inflate(R.menu.item_account_popup_menu,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AccountInfo accountInfo = adapter.getItem(position);
                switch(item.getItemId()){
                    // account 수정
                    case R.id.account_list_revise_popup_item:
                        AddAccountDialog dialog = new AddAccountDialog(getContext(), accountInfo);
                        dialog.setListener(new AddAccountDialog.AddAccountDialogListener() {
                            @Override // not used
                            public void onAddButtonClicked(int budget, String accountName, String subscribe, boolean isMain) {}
                            @Override
                            public void onReviseButtonClicked(int accountId, int budget, String accountName, String subscribe) {
                                new UpdateAccountAsyncTask(db.accountDao()).execute(new AccountInfo(accountId, accountName, false, budget, subscribe, 0, 0));
                            }
                        });
                        dialog.show();
                        break;

                    // account 삭제
                    case R.id.account_list_delete_popup_item:
                        try {
                            if(accountInfo.isMain() == true) throw new MyException("Main Group은 삭제할 수 없습니다");

                            new DeleteAccountAsyncTask(db.accountDao()).execute(accountInfo.getAccountId());
                        } catch (MyException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        break;

                    // 기존의 Group을 Main Group으로 등록
                    case R.id.account_list_main_popup_item:
                        try {
                            if(accountInfo.isMain() == true) throw new MyException("이미 Main Group 입니다.");

                            new WipeOutAndAppointMainAccountAsyncTask(db.accountDao()).execute(accountInfo.getAccountId());
                        } catch (MyException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            switch(newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    anim();
                    break;
            }
        }
    };

    public void anim() {
        if (isFabOpen) {
            groupAddFab.startAnimation(fab_close);
            isFabOpen = false;
        } else {
            groupAddFab.startAnimation(fab_open);
            isFabOpen = true;
        }
    }
}
