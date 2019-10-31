package com.kumoh.paylog2.fragment.contents;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContentsStatisticsFragment extends Fragment {
    ViewGroup rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_contents_statistics, container, false);
        setPieChart();
        setLineChart();
        return rootView;
    }

    private void setPieChart(){
        PieChart pieChart = rootView.findViewById(R.id.category_chart);

        pieChart.setUsePercentValues(true); // true로 하면 percent, false로 하면 rawData
        pieChart.setTouchEnabled(true); // 차트 터치 활성화
        pieChart.setDragDecelerationEnabled(false); // 차트 회전 비활성화
        pieChart.getLegend().setEnabled(false); // 범례 비활성화
        pieChart.getDescription().setEnabled(false); // 설명 비활성화

        // 그래프로 그릴 데이터의 값 저장
        ArrayList<PieEntry> entries=new ArrayList<>();
        entries.add(new PieEntry(250000,"식비"));
        entries.add(new PieEntry(200000,"생활"));
        entries.add(new PieEntry(65000,"통신"));
        entries.add(new PieEntry(56000,"교통"));
        entries.add(new PieEntry(30000,"기타"));

        // 그래프 중앙 텍스트 지정
        String mostL = entries.get(0).getLabel();
        int mostV = (int)entries.get(0).getValue();
        pieChart.setCenterText(mostL + "\n\n" + mostV + "원");
        pieChart.setCenterTextSize(15);

        // 범례 설정
        PieDataSet dataSet = new PieDataSet(entries,"Category");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK); //원그래프 안에 수치를 표시하는 text의 색

        pieChart.setData(data);
        pieChart.invalidate();
    }

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
        /*dataset.setDrawCubic(true); //선 둥글게 만들기
        dataset.setDrawFilled(true); //그래프 밑부분 색칠*/

        lineChart.setData(data);
        lineChart.animateY(1000);
        lineChart.invalidate();
    }
}
