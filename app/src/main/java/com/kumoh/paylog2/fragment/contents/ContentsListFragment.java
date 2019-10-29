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
import com.kumoh.paylog2.db.History;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.dto.ContentsListBody;
import com.kumoh.paylog2.dto.ContentsListHeader;
import com.kumoh.paylog2.dto.ContentsListItem;

import java.util.ArrayList;
import java.util.List;

public class ContentsListFragment extends Fragment {
    LocalDatabase db;
    private int accountId;
    private ContentsListRecyclerAdapter adapter;
    private ArrayList<ContentsListItem> listItem;

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
        adapter = new ContentsListRecyclerAdapter(new ArrayList<ContentsListItem>());

        recyclerView.setAdapter(adapter);
        db = LocalDatabase.getInstance(getContext());
        db.historyDao().getAllByAccountId(this.accountId).observe(this, list->{
            if(list.size() >0) {
                makeListHeader(list);
                adapter.setData(listItem);
            }
            //notify만 하면끝
        });

        return rootView;
    }

    private void makeListHeader(List<History> newData){
        listItem = null;
        listItem = new ArrayList<ContentsListItem>();
        /*int flag = 0;
        for(int i = 0; i < newData.size(); i ++){
            listItem.add(new ContentsListHeader(newData.get(i).getDate())); //헤더추가
            int spend = 0;
            int income = 0;
            for(int j = i; j < newData.size()-1; j++){
                if(newData.get(j).getKind() == 1){
                    income += newData.get(j).getAmount();
                }else if (newData.get(j).getKind() == -1){
                    spend += newData.get(j).getAmount();
                }
                listItem.add(new ContentsListBody(newData.get(j).getKind(),newData.get(j).getDate(), newData.get(j).getCategoryId(),
                        newData.get(j).getDescription(), newData.get(j).getAmount(), newData.get(j).getHistoryId()));

                if(newData.get(j).getDate().equals(newData.get(j+1).getDate())){ //끝내기전 헤더 데이터 변경
                    listItem.get(i).setSpending(spend);
                    listItem.get(i).setIncome(income);
                    i = j+1;
                }
            }
        }*/
        int flag = 0;
        int spending =0, income = 0;
        listItem.add(new ContentsListHeader(newData.get(0).getDate()));
        for (int i = 0 ; i < newData.size()-1; i ++){

            if(newData.get(i).getKind() == -1){
                listItem.add(new ContentsListBody(newData.get(i).getKind(),newData.get(i).getDate(), newData.get(i).getCategoryId(),
                        newData.get(i).getDescription(), newData.get(i).getAmount(), newData.get(i).getHistoryId()));
                spending += newData.get(i).getAmount();
            }else if(newData.get(i).getKind() ==1){
                listItem.add(new ContentsListBody(newData.get(i).getKind(),newData.get(i).getDate(), newData.get(i).getCategoryId(),
                        newData.get(i).getDescription(), newData.get(i).getAmount(), newData.get(i).getHistoryId()));
                income += newData.get(i).getAmount();
            }

            if(!newData.get(i).getDate().equals(newData.get(i+1).getDate())){
                listItem.get(flag).setIncome(income);
                listItem.get(flag).setSpending(spending); //기존에 있던놈 넣어주고
                flag = listItem.size(); //flag 잡아주고
                spending = 0; income = 0; //다시 출발
                listItem.add(new ContentsListHeader(newData.get(i+1).getDate()));
            }
        }
        int last = newData.size()-1;
        if(newData.get(last).getKind() == -1){
            listItem.add(new ContentsListBody(newData.get(last).getKind(),newData.get(last).getDate(), newData.get(last).getCategoryId(),
                    newData.get(last).getDescription(), newData.get(last).getAmount(), newData.get(last).getHistoryId()));
            spending += newData.get(last).getAmount();
        }else if(newData.get(last).getKind() ==1){
            listItem.add(new ContentsListBody(newData.get(last).getKind(),newData.get(last).getDate(), newData.get(last).getCategoryId(),
                    newData.get(last).getDescription(), newData.get(last).getAmount(), newData.get(last).getHistoryId()));
            income += newData.get(last).getAmount();
        }
        listItem.get(flag).setIncome(income);
        listItem.get(flag).setSpending(spending);
    }
}


