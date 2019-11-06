package com.kumoh.paylog2.fragment.contents;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.contents.ContentsCalendarMainAdapter;
import com.kumoh.paylog2.db.History;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.dto.ContentsListDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ContentsCalendarFragment extends Fragment implements View.OnClickListener{

    private int accountId;
    private int myCalPos; // 현재 달력의 위치 지정
    private Calendar now;
    private ArrayList<ArrayList<Object>> myMonthList;
    private LocalDatabase db;

    private ViewGroup rootView;
    private TextView mainHeader;
    private RecyclerView recyclerView;
    private ContentsCalendarMainAdapter adapter;
    private  Button btnBefore;
    private Button btnAfter;

    public ContentsCalendarFragment(int accountId){
        this.accountId = accountId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(
                R.layout.fragment_contents_calendar, container, false);

        mainHeader = rootView.findViewById(R.id.main_header);
        recyclerView = rootView.findViewById(R.id.my_calendar);
        btnBefore = rootView.findViewById(R.id.btn_month_before);
        btnAfter = rootView.findViewById(R.id.btn_month_after);

        db = LocalDatabase.getInstance(getContext());

        myMonthList = setCalendarList(120);
        adapter = new ContentsCalendarMainAdapter(getContext(), myMonthList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false){
                    @Override
                    public boolean canScrollHorizontally(){
                        return false;
                    }
                });

        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(myCalPos);

        btnBefore.setOnClickListener(this);
        btnAfter.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_month_before:
                myCalPos -= 1;
                recyclerView.scrollToPosition(myCalPos);
                adapter.notifyItemChanged(myCalPos);
                setHeader(now, -1);
                break;
            case R.id.btn_month_after:
                myCalPos += 1;
                recyclerView.scrollToPosition(myCalPos);
                adapter.notifyItemChanged(myCalPos);
                setHeader(now, 1);
                break;
        }
    }

    public ArrayList<ArrayList<Object>> setCalendarList(int scope){
        ArrayList<ArrayList<Object>> monthList = new ArrayList<>(); // 월 별 리스트를 가진 리스트
        GregorianCalendar cal = new GregorianCalendar(); // 현재 날짜

        // scope 통해 현재 월로부터 얼마나 떨어진 지점까지 생성할지 결정
        for(int i = -scope; i < scope; i++){
            ArrayList<Object> calendarList = new ArrayList<>();

            GregorianCalendar calendar // 결정한 범위의 첫달 첫날부터 시작
                    = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + i,
                    1, 0, 0, 0);
            if(i == 0){ // 현재 월 지정 및 헤더 설정
                myCalPos = monthList.size();
                now = calendar;
                mainHeader.setText(
                        new SimpleDateFormat("yyyy년 MM월", Locale.ENGLISH)
                                .format(calendar.getTimeInMillis()));
            }
            calendarList.add(calendar.getTimeInMillis()); // 1. 첫 주의 헤더 타입(Long) 추가

            // DAY_OF_WEEK 일~토(1~7) 반환
            // 해당 월의 첫날이 포함된 첫 요일 - 1 값 지정
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            // 해당 월의 마지막 요일
            int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            for(int j = 1; j < dayOfWeek; j++){
                calendarList.add("Empty"); // 2. 비어있는 일자 타입(String) 추가
            }
            for(int j = 1; j <= max; j++){
                GregorianCalendar day
                        = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),j);
                // 3-1. 첫주 이후 주가 시작될때 헤더 타입(Long) 추가
                if(day.get(Calendar.DAY_OF_WEEK) == 1){
                    calendarList.add(calendar.getTimeInMillis());
                }
                calendarList.add(day);  // 3. 일자 타입(Calendar) 추가
            }
            monthList.add(calendarList); // 완성된 한달을 monthList 에 추가
        }

        return monthList;
    }

    public void setHeader(Calendar now, int amount){
        now.add(Calendar.MONTH, amount);
        mainHeader.setText(
                new SimpleDateFormat("yyyy년 MM월", Locale.ENGLISH)
                        .format(now.getTimeInMillis()));
    }
}
