package com.kumoh.paylog2.fragment.contents;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.contents.ContentsFragmentAdapter;
import com.kumoh.paylog2.adapter.contents.ContentsListRecyclerAdapter;
import com.kumoh.paylog2.db.Category;
import com.kumoh.paylog2.db.History;
import com.kumoh.paylog2.db.HistoryDao;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.dialog.ControlIncomeHistoryDialog;
import com.kumoh.paylog2.dialog.ControlSpendingHistoryDialog;
import com.kumoh.paylog2.dto.ContentsCategoryItem;
import com.kumoh.paylog2.dto.ContentsListBody;
import com.kumoh.paylog2.dto.ContentsListHeader;
import com.kumoh.paylog2.dto.ContentsListItem;

import java.util.ArrayList;
import java.util.List;

public class ContentsListFragment extends Fragment implements ContentsListRecyclerAdapter.ContentsListRecyclerLongClickListener {
    final static int SPENDING_VIEW = -1;
    final static int INCOME_VIEW = 1;

    LocalDatabase db;
    private int accountId;
    private ContentsListRecyclerAdapter adapter;
    private ArrayList<ContentsListItem> listItem;
    private ArrayList<ContentsCategoryItem> categoryItems;
    List<ContentsCategoryItem> spendingCategories;
    List<ContentsCategoryItem> incomeCategories;

    private RecyclerScrollStateChangedListener recyclerScrollStateChangedListener;

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

        adapter = new ContentsListRecyclerAdapter(new ArrayList<ContentsListItem>(), new ArrayList<ContentsCategoryItem>());
        adapter.setLongClickListener(this);

        recyclerView.addOnScrollListener(onScrollListener);

        recyclerView.setAdapter(adapter);
        db = LocalDatabase.getInstance(getContext());
        db.historyDao().getAllByAccountId(this.accountId).observe(this, list->{
            if(list.size() >0) {
                makeListHeader(list);
                adapter.setData(listItem);
            }
            //notify만 하면끝
        });
        db.categoryDao().getEveryCategories().observe(this, list->{
            if(list.size() > 0){
                makeCategoryList(list);
                adapter.setCategoryItems(categoryItems);
            }
        });
        initCategories();

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

            if(newData.get(i).getKind() == 1 || newData.get(i).getKind() == 3){
                listItem.add(new ContentsListBody(newData.get(i).getKind(),newData.get(i).getDate(), newData.get(i).getCategoryId(),
                        newData.get(i).getDescription(), newData.get(i).getAmount(), newData.get(i).getHistoryId()));
                spending += newData.get(i).getAmount();
            }else if(newData.get(i).getKind() == 0 || newData.get(i).getKind() == 2){
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
        if(newData.get(last).getKind() == 1 || newData.get(last).getKind() == 3){
            listItem.add(new ContentsListBody(newData.get(last).getKind(),newData.get(last).getDate(), newData.get(last).getCategoryId(),
                    newData.get(last).getDescription(), newData.get(last).getAmount(), newData.get(last).getHistoryId()));
            spending += newData.get(last).getAmount();
        }else if(newData.get(last).getKind() == 0 || newData.get(last).getKind() == 2){
            listItem.add(new ContentsListBody(newData.get(last).getKind(),newData.get(last).getDate(), newData.get(last).getCategoryId(),
                    newData.get(last).getDescription(), newData.get(last).getAmount(), newData.get(last).getHistoryId()));
            income += newData.get(last).getAmount();
        }
        listItem.get(flag).setIncome(income);
        listItem.get(flag).setSpending(spending);
    }

    private void makeCategoryList(List<Category> data){
        categoryItems = new ArrayList<>();

        for(Category category : data){
            categoryItems.add(new ContentsCategoryItem(category.getCategoryId(), category.getName(), category.getKind()));
        }
    }

    @Override
    public void onItemLongClicked(int pos) {
        Toast.makeText(getContext(), "길게 눌림", Toast.LENGTH_SHORT).show();
        if(listItem.get(pos).getViewType() == INCOME_VIEW){
            ControlIncomeHistoryDialog controlIncomeHistoryDialog = new ControlIncomeHistoryDialog(getContext(), incomeCategories, (ContentsListBody) listItem.get(pos));
            controlIncomeHistoryDialog.setControlIncomeHistoryDialogListener(new ControlIncomeHistoryDialog.ControlIncomeHistoryDialogListener() {
                @Override
                public void onUpdateButtonClicked(int kind, String date, int category, String description, int amount) {
                    History historyToUpdate = new History(accountId, kind, date, category, description, amount);
                    historyToUpdate.setHistoryId(((ContentsListBody) listItem.get(pos)).getHistoryId());
                    new UpdateHistory(db.historyDao()).execute(historyToUpdate);
                }
                @Override
                public void onDeleteButtonClicked(){
                    new DeleteHistory(db.historyDao()).execute(((ContentsListBody) listItem.get(pos)).getHistoryId());
                }
            });
            controlIncomeHistoryDialog.show();
        } else if(listItem.get(pos).getViewType() == SPENDING_VIEW){
            ControlSpendingHistoryDialog controlSpendingHistoryDialog = new ControlSpendingHistoryDialog(getContext(), spendingCategories, (ContentsListBody) listItem.get(pos));
            controlSpendingHistoryDialog.setControlSpendingHistoryDialogListener(new ControlSpendingHistoryDialog.ControlSpendingHistoryDialogListener() {
                @Override
                public void onUpdateButtonClicked(int kind, String date, int category, String description, int amount) {
                    History historyToUpdate = new History(accountId, kind, date, category, description, amount);
                    historyToUpdate.setHistoryId(((ContentsListBody) listItem.get(pos)).getHistoryId());
                    new UpdateHistory(db.historyDao()).execute(historyToUpdate);
                }
                @Override
                public void onDeleteButtonClicked() {
                    new DeleteHistory(db.historyDao()).execute(((ContentsListBody) listItem.get(pos)).getHistoryId());
                }
            });
            controlSpendingHistoryDialog.show();
        }
    }

    // 카테고리 초기화
    private void initCategories(){
        List<ContentsCategoryItem> spendingCategories = new ArrayList<>();
        List<ContentsCategoryItem> incomeCategories = new ArrayList<>();

        db.categoryDao().getSpendingCategories().observe(this, list->{
            for(int pos = 0; pos < list.size(); pos++){
                spendingCategories.add(new ContentsCategoryItem(list.get(pos).getCategoryId(), list.get(pos).getName(), list.get(pos).getKind()));
            }
        });

        db.categoryDao().getIncomeCategories().observe(this, list->{
            for(int pos = 0; pos < list.size(); pos++){
                incomeCategories.add(new ContentsCategoryItem(list.get(pos).getCategoryId(), list.get(pos).getName(), list.get(pos).getKind()));
            }
        });

        this.spendingCategories =  spendingCategories;
        this.incomeCategories = incomeCategories;
    }

    private static class UpdateHistory extends AsyncTask<History, Void, Void> {
        HistoryDao dao;
        public UpdateHistory(HistoryDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(History... histories) {
            //날짜 보정
            String date = histories[0].getDate();
            String split[] = date.split("-");
            String year = split[0];
            String month = split[1];
            String day = split[2];
            if(month.length() == 1){
                month = "0"+month;
            }
            if(day.length() == 1){
                day = "0"+day;
            }
            date = year+"-"+month+"-"+day;
            histories[0].setDate(date);

            dao.updateHistory(histories[0]);
            return null;
        }
    }

    private static class DeleteHistory extends AsyncTask<Integer, Void, Void> {
        HistoryDao dao;
        public DeleteHistory(HistoryDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Integer... historyIds) {
            dao.deleteHistory(historyIds[0]);
            return null;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof RecyclerScrollStateChangedListener) {
            recyclerScrollStateChangedListener = (RecyclerScrollStateChangedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement RecyclerScrollStateChangedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        recyclerScrollStateChangedListener = null;
    }

    public interface RecyclerScrollStateChangedListener {
        void onContentsListScrollChanged(int newState);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            recyclerScrollStateChangedListener.onContentsListScrollChanged(newState);
        }
    };
}


