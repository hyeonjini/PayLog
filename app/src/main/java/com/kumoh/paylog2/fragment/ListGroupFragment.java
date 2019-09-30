package com.kumoh.paylog2.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.GroupListRecyclerAdapter;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.db.PurchaseGroup;

import java.util.ArrayList;
import java.util.List;

public class ListGroupFragment extends Fragment {

    private LocalDatabase db;

    public ListGroupFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_list_group, container, false);

        List<PurchaseGroup> groupList = new ArrayList<>();
        RecyclerView recyclerView = rootView.findViewById(R.id.group_list_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);

        GroupListRecyclerAdapter adapter = new GroupListRecyclerAdapter(groupList);
        recyclerView.setAdapter(adapter);

        //db 연결
        db = LocalDatabase.getInstance(getContext());
        db.purchaseGroupDao().getAll().observe(this, list->{
            System.out.println(list.size()+"개");
            adapter.setData(list);
        });
        // Inflate the layout for this fragment
        return rootView;
    }

}
