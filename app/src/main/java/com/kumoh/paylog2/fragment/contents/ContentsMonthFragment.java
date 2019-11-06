package com.kumoh.paylog2.fragment.contents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.contents.ContentsMonthRecyclerAdapter;
import com.kumoh.paylog2.db.History;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.dto.ContentsListBody;
import com.kumoh.paylog2.dto.ContentsListHeader;
import com.kumoh.paylog2.dto.ContentsMonthItem;

import java.util.ArrayList;
import java.util.List;

public class ContentsMonthFragment extends Fragment {
    private int accountId;
    private LocalDatabase db;
    private ArrayList<ContentsMonthItem> listItem;

    public ContentsMonthFragment(int accountId){
        this.accountId = accountId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_contentsmonth, container, false);

        //리사이클러뷰 설정
        RecyclerView recyclerView = rootView.findViewById(R.id.contents_month_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        listItem = new ArrayList<ContentsMonthItem>();
        ContentsMonthRecyclerAdapter adapter = new ContentsMonthRecyclerAdapter(listItem);
        recyclerView.setAdapter(adapter);

        //DB설정
        db = LocalDatabase.getInstance(getContext());
        db.historyDao().getAllByAccountId(this.accountId).observe(this, list->{
            if(list.size() >0) {
                makeList(list);
                adapter.setData(listItem);
            }
        });
        /*db.historyDao().getAllByMonthAndAccountId(accountId).observe(this, list->{

            adapter.setData(list);
        });*/
        return rootView;
    }

    public void makeList(List<History> newData){
        listItem = null;
        listItem = new ArrayList<ContentsMonthItem>();
        int flag = 0;
        int spending =0, income = 0;
        listItem.add(new ContentsMonthItem(newData.get(0).getDate().substring(0,7),0,0));
        for (int i = 0 ; i < newData.size()-1; i ++){

            if(newData.get(i).getKind() == 1 || newData.get(i).getKind() == 3){
                spending += newData.get(i).getAmount();
            }else if(newData.get(i).getKind() == 0 || newData.get(i).getKind() == 2){
                income += newData.get(i).getAmount();
            }

            if(!newData.get(i).getDate().substring(0,7).equals(newData.get(i+1).getDate().substring(0,7))){
                listItem.get(flag).setIncome(income);
                listItem.get(flag).setSpending(spending); //기존에 있던놈 넣어주고
                flag = listItem.size(); //flag 잡아주고
                spending = 0; income = 0; //다시 출발
                listItem.add(new ContentsMonthItem(newData.get(i+1).getDate().substring(0,7),0,0));
            }
        }
        int last = newData.size()-1;
        if(newData.get(last).getKind() == 1 || newData.get(last).getKind() == 3){
            spending += newData.get(last).getAmount();
        }else if(newData.get(last).getKind() == 0 || newData.get(last).getKind() == 2){
            income += newData.get(last).getAmount();
        }
        listItem.get(flag).setIncome(income);
        listItem.get(flag).setSpending(spending);
    }
}
