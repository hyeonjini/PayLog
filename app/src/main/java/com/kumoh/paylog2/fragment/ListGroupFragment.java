package com.kumoh.paylog2.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kumoh.paylog2.R;
import com.kumoh.paylog2.activity.ContentsActivity;
import com.kumoh.paylog2.adapter.GroupListRecyclerAdapter;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.db.PurchaseGroup;
import com.kumoh.paylog2.db.PurchaseGroupDao;
import com.kumoh.paylog2.dialog.NewGroupAddDialog;

import java.util.ArrayList;
import java.util.List;

public class ListGroupFragment extends Fragment implements GroupListRecyclerAdapter.GroupListRecyclerOnClickListener {

    private LocalDatabase db;
    private GroupListRecyclerAdapter adapter;
    public ListGroupFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list_group, container, false);

        List<PurchaseGroup> groupList = new ArrayList<>();
        RecyclerView recyclerView = rootView.findViewById(R.id.group_list_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GroupListRecyclerAdapter(groupList);
        //리스너 등록
        adapter.setOnClickListener(this);

        recyclerView.setAdapter(adapter);
        //db 연결
        db = LocalDatabase.getInstance(getContext());
        db.purchaseGroupDao().getAll().observe(this, list->{
            System.out.println(list.size()+"개");
            adapter.setData(list);
        });

        //FloatingActionButton 설정
        FloatingActionButton groupAddFab = (FloatingActionButton) rootView.findViewById(R.id.add_group_fab);
        groupAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.add_group_fab:
                        NewGroupAddDialog dialog = new NewGroupAddDialog(getContext());
                        dialog.setListener(new NewGroupAddDialog.NewGroupAddDialogListener() {
                            @Override
                            public void onAddButtonClicked(String groupName, String contents) {
                                new InsertGroupAsyncTask(db.purchaseGroupDao()).execute(new PurchaseGroup(groupName,contents));
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
    private static class InsertGroupAsyncTask extends AsyncTask<PurchaseGroup, Void, Void>{

        private PurchaseGroupDao dao;
        public InsertGroupAsyncTask(PurchaseGroupDao dao){
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(PurchaseGroup... purchaseGroups) {
            dao.insertPurchaseGroup(purchaseGroups[0]);

            return null;
        }
    }

    //리스너 동작 구현
    //짧게 터치
    @Override
    public void onItemClicked(int position){
        PurchaseGroup group = null;
        group = adapter.getItem(position);

        Toast.makeText(getContext(), "그룹id : "+group.getId(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ContentsActivity.class);
        intent.putExtra("selectedGroupId",Integer.toString(group.getId()));
        startActivity(intent);
    }

    //길게 터치

}
