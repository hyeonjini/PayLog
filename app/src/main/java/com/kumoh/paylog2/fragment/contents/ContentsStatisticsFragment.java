package com.kumoh.paylog2.fragment.contents;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.contents.ContentsCategoryRecyclerAdapter;
import com.kumoh.paylog2.dto.ContentsCategoryItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContentsStatisticsFragment extends Fragment {
    ViewGroup rootView;
    private List<ContentsCategoryItem> list;
    private ContentsCategoryRecyclerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_contents_statistics, container, false);

        list = new ArrayList<ContentsCategoryItem>();
        list.add(new ContentsCategoryItem(320000,"식비"));
        list.add(new ContentsCategoryItem(200000,"생활비"));
        list.add(new ContentsCategoryItem(100000,"쇼핑"));
        list.add(new ContentsCategoryItem(65000,"통신비"));
        list.add(new ContentsCategoryItem(56000,"교통비"));
        list.add(new ContentsCategoryItem(30000,"문화"));


        setPieChart(list);
        setCategoryView(list);
        //setLineChart();

        return rootView;
    }

    // 파이 차트 생성 함수
    private void setPieChart(List<ContentsCategoryItem> list){
        PieChart pieChart = rootView.findViewById(R.id.category_chart);

        pieChart.setUsePercentValues(true); // true로 하면 percent, false로 하면 rawData
        pieChart.setTouchEnabled(true); // 차트 터치 활성화
        pieChart.setDragDecelerationEnabled(false); // 차트 회전 비활성화
        pieChart.getDescription().setEnabled(false); // 설명 비활성화
        pieChart.setHoleRadius(0); // 차트 out->in 채울 비율
        pieChart.setTransparentCircleRadius(0); // 차트 중앙 원 비율
        pieChart.getLegend().setEnabled(false); // 범례 비활성화
        pieChart.setExtraOffsets(0,13,0,13);

        // 그래프로 그릴 데이터의 값 저장
        ArrayList<PieEntry> entries=new ArrayList<>();
        int index = 0;
        int etcSum = 0;
        for(ContentsCategoryItem item : list) {
            if(index < 4) // 4번째 항목까지 별개 항목으로 취급
                entries.add(new PieEntry(item.getValue(), item.getCategory()));
            else // 5번째 항목부터 "그 외" 분류에 추가
                etcSum += item.getValue();
            index++;
        }
        entries.add(new PieEntry(etcSum, "그 외"));

        // dataSet 설정
        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE); // dataSet의 value 값  차트 밖에 표기
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

        pieChart.setEntryLabelColor(R.color.colorBlack); // 카테고리 표기 text 색

        pieChart.setData(data);
        pieChart.invalidate();
    }

    // 카테고리 별 목록 리사이클러뷰 생성 함수
    private void setCategoryView(List<ContentsCategoryItem> list){
        RecyclerView recyclerView = rootView.findViewById(R.id.category_items);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ContentsCategoryRecyclerAdapter(list);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration
                (recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    /*
    // 꺾은선 차트 생성 함수
    private void setLineChart(){
        LineChart lineChart = rootView.findViewById(R.id.day_chart);
        lineChart.setTouchEnabled(false); // 차트 터치 비활성화
        lineChart.getLegend().setEnabled(false); // 범례 비활성화
        lineChart.getDescription().setEnabled(false); // 설명 비활성화

        List<Entry> entries = new ArrayList<>();
        for(int i = 1; i < 31; i++){
            entries.add(new Entry(i,new Random().nextInt(10)));
        }

        LineDataSet dataset = new LineDataSet(entries, "일별 지출");
        dataset.setColor(ContextCompat.getColor(getContext(),R.color.mainColor)); // LineColor 설정
        dataset.setCircleColor(ContextCompat.getColor(getContext(),R.color.colorAccent)); // LineCircleColor 설정
        dataset.setCircleHoleColor(ContextCompat.getColor(getContext(),R.color.mainColor)); // LineHoleColor 설정

        LineData data = new LineData();
        data.addDataSet(dataset);
        data.setValueTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack)); //라인 데이터의 텍스트 컬러
        data.setValueTextSize(0); // 라인 데이터 텍스트 안보이게

        XAxis xAxis = lineChart.getXAxis(); // x축 설정
        xAxis.setDrawGridLinesBehindData(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // x축 위치 설정
        xAxis.setLabelCount(entries.size(),true); // x축 데이터 최대 개수
        xAxis.setDrawGridLines(false);

        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //

        lineChart.setData(data);
        lineChart.animateY(1000);
        lineChart.invalidate();
    }
    */
}
