package com.kumoh.paylog2.fragment.contents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.contents.ContentsListRecyclerAdapter;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.dto.ContentsListForm;
import com.kumoh.paylog2.dto.ListItemDto;

import java.util.ArrayList;

public class ContentsListFragment extends Fragment {
    LocalDatabase db;
    private int accountId;
    private ContentsListRecyclerAdapter adapter;
    public ContentsListFragment(int accountId){
        this.accountId = accountId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_contentslist, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.contents_list_item_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ContentsListRecyclerAdapter(new ArrayList<ListItemDto>());

        recyclerView.setAdapter(adapter);

        db = LocalDatabase.getInstance(getContext());
        db.accountDao().getListItem(accountId).observe(this, listItemDtos -> {
            adapter.setData(listItemDtos);
        });


        return rootView;
    }
}


