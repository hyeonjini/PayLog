package com.kumoh.paylog2.fragment.contents;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.contents.ContentsStatisticsRecyclerAdapter;
import com.kumoh.paylog2.db.Category;
import com.kumoh.paylog2.db.History;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.dto.ContentsCalendarItem;
import com.kumoh.paylog2.dto.ContentsCategoryItem;
import com.kumoh.paylog2.dto.ContentsStatisticsCategoryItem;

import java.util.ArrayList;
import java.util.List;

public class ContentsStatisticsFragment extends Fragment {
    private ViewGroup rootView;
    private LocalDatabase db;
    private int accountId;
    private List<ContentsStatisticsCategoryItem> list;
    private List<ContentsCategoryItem> spendingCategories;
    //private List<ContentsCategoryItem> incomeCategories;
    private RecyclerView recyclerView;
    private ContentsStatisticsRecyclerAdapter adapter;

    public ContentsStatisticsFragment(int accountId){
        this.accountId = accountId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_contents_statistics, container, false);

        recyclerView = rootView.findViewById(R.id.category_items);

        db = LocalDatabase.getInstance(getContext());

        adapter = new ContentsStatisticsRecyclerAdapter(new ArrayList<>(), new ArrayList<>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration
                (recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        db.categoryDao().getSpendingCategories().observe(this, list->{
            for(int pos = 0; pos < list.size(); pos++){
                initCategories(list);
            }
            adapter.setCategories(this.spendingCategories);
        });

        db.historyDao().getGroupedListByCategory(accountId).observe(this,list->{
            makeItemList(list);
            setPieChart(this.list);
            adapter.setData(this.list);
        });

        return rootView;
    }

    // 파이 차트 생성 함수
    private void setPieChart(List<ContentsStatisticsCategoryItem> list){
        PieChart pieChart = rootView.findViewById(R.id.category_chart);

        pieChart.setUsePercentValues(true); // true로 하면 percent, false로 하면 rawData
        pieChart.setTouchEnabled(true); // 차트 터치 활성화
        pieChart.setRotationEnabled(false); // 차트 회전 비활성화
        pieChart.getDescription().setEnabled(false); // 설명 비활성화
        pieChart.setHoleRadius(0); // 차트 out->in 채울 비율
        pieChart.setTransparentCircleRadius(0); // 차트 중앙 원 비율
        pieChart.getLegend().setEnabled(false); // 범례 비활성화
        pieChart.setExtraOffsets(0,13,0,13);

        // 그래프로 그릴 데이터의 값 저장
        ArrayList<PieEntry> entries=new ArrayList<>();
        int index = 0;
        int etcSum = 0;
        for(ContentsStatisticsCategoryItem item : list) {
            if(index < 4) // 4번째 항목까지 별개 항목으로 취급
                entries.add(new PieEntry(item.getAmount(), getSpendingCategoryById(item.getCategoryId())));
            else // 5번째 항목부터 "그 외" 분류에 추가
                etcSum += item.getAmount();
            index++;
        }
        if(list.size() > 4)
            entries.add(new PieEntry(etcSum, "그 외"));

        // dataSet 설정
        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE); // dataSet의 value 값 차트 밖에 표기
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE); // dataSet의 category 값 차트 밖에 표기
        dataSet.setValueLinePart1OffsetPercentage(100.f); // 차트 중앙으로부터 시작선 위치 지정(%)
        dataSet.setValueLinePart1Length(1.2f); // 선 앞부분 설정
        dataSet.setValueLinePart2Length(.2f); // 선 뒷부분 설정
        dataSet.setSelectionShift(2f); // 선택된 카테고리 돌출 비율
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS); // 차트 색상 그룹 설정

        PieData data = new PieData((dataSet));
        data.setValueTextColor(Color.BLACK); // 수치 표기 text 색
        data.setValueFormatter(new PercentFormatter()); // 수치 표기 text 포맷 설정
        data.setValueTextSize(10f); // 수치 표기 text 크기

        pieChart.setEntryLabelColor(getResources().getColor(R.color.textBlack)); // 카테고리 표기 text 색

        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void makeItemList(List<ContentsStatisticsCategoryItem> newData){
        if(newData.size() > 0){
            list = new ArrayList<>();
            for(ContentsStatisticsCategoryItem h : newData){
                if(h.getKind() == 1 || h.getKind() == 3){
                    list.add(new ContentsStatisticsCategoryItem(-h.getAmount(), h.getCategoryId(), h.getKind()));
                }
            }
        }else{
            list = new ArrayList<>();
            list.add(new ContentsStatisticsCategoryItem(0,-1, 1));
        }
    }

    // 카테고리 초기화
    private void initCategories(List<Category> list){
        spendingCategories = new ArrayList<>();
        //incomeCategories = new ArrayList<>();
        for(int pos = 0; pos < list.size(); pos++){
            spendingCategories.add(new ContentsCategoryItem(list.get(pos).getCategoryId(), list.get(pos).getName(), list.get(pos).getKind()));
        }
        /*
        db.categoryDao().getIncomeCategories().observe(this, list->{
            for(int pos = 0; pos < list.size(); pos++){
                incomeCategories.add(new ContentsCategoryItem(list.get(pos).getCategoryId(), list.get(pos).getName(), list.get(pos).getKind()));
                this.incomeCategories = incomeCategories;
            }
        });
        */
    }

    private String getSpendingCategoryById(int id){
        String category = null;
        if(spendingCategories != null){
            for(ContentsCategoryItem c : spendingCategories){
                if(id == c.getId())
                    category = c.getCategory();
            }
        }
        return category;
    }
}
