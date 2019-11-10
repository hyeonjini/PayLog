package com.kumoh.paylog2.fragment.contents;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.contents.ContentsCalendarAdapter;
import com.kumoh.paylog2.db.History;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.dto.ContentsCalendarItem;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ContentsCalendarFragment extends Fragment implements View.OnClickListener{

    private int accountId;
    private GregorianCalendar selectedDay; // 현재 선택된 월 지정
    private ArrayList<Object> myCalendarList; // 현재 선택된 월의 달력 오브젝트 리스트
    private ArrayList<ContentsCalendarItem> myData; // 현재 선택된 월의 각 날짜에 들어갈 데이터
    private LocalDatabase db;

    private ViewGroup rootView;
    private TextView mainHeader;
    private RecyclerView recyclerView;
    private ContentsCalendarAdapter adapter;
    private  Button btnBefore;
    private Button btnAfter;

    public ContentsCalendarFragment(int accountId){
        this.accountId = accountId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        selectedDay = new GregorianCalendar();
        myCalendarList = setCalendarList(selectedDay);

        adapter = new ContentsCalendarAdapter(new ArrayList<>(), new ArrayList<>() ,getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        db = LocalDatabase.getInstance(getContext());
        setData();
        btnBefore.setOnClickListener(this);
        btnAfter.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_month_before:
                selectedDay.add(Calendar.MONTH, -1);
                myCalendarList = setCalendarList(selectedDay);
                setData();
                adapter.resetData(myCalendarList,myData);
                break;
            case R.id.btn_month_after:
                selectedDay.add(Calendar.MONTH, 1);
                myCalendarList = setCalendarList(selectedDay);
                setData();
                adapter.resetData(myCalendarList,myData);
                break;
        }
    }

    private ArrayList<Object> setCalendarList(GregorianCalendar cal){
        ArrayList<Object> calendarList = new ArrayList<>();

        GregorianCalendar calendar // 결정한 범위의 첫달 첫날부터 시작
                = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                1, 0, 0, 0);
        mainHeader.setText(new SimpleDateFormat("yyyy년 MM월", Locale.ENGLISH)
                .format(calendar.getTimeInMillis()));
        calendarList.add(calendar.getTimeInMillis()); // 1. 첫 주의 헤더 타입(Long) 추가

        // DAY_OF_WEEK 일~토(1~7) 반환
        // 해당 월의 첫날이 포함된 첫 요일 - 1 값 지정
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // 해당 월의 마지막 요일
        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for(int j = 1; j < dayOfWeek; j++){
            calendarList.add("Empty"); // 2. 비어있는 일자 타입(String) 추가
        }
        for(int j = 1; j <= max; j++) {
            GregorianCalendar day
                    = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),j);
            // 3-1. 첫주 이후 주가 시작될때 헤더 타입(Long) 추가
            if(day.get(Calendar.DAY_OF_WEEK) == 1 && j > 1){
                calendarList.add(calendar.getTimeInMillis());
            }
            calendarList.add(day);  // 3. 일자 타입(Calendar) 추가
        }

        return calendarList;
    }

    private void setData(){
        db.historyDao().getAllByAccountIdFromTo(
                this.accountId,myDateFormatter(selectedDay,true),myDateFormatter(selectedDay,false)).observe(this,list->{
            myData = null;
            myData = new ArrayList<>();
            if(list.size() > 0){
                String date = list.get(0).getDate().substring(list.get(0).getDate().lastIndexOf("-") + 1);
                int income = 0;
                int spending = 0;
                for(History h : list){
                    if(!date.equals(h.getDate().substring(h.getDate().lastIndexOf("-") + 1))){
                        myData.add(new ContentsCalendarItem(date,income,spending));
                        Log.i(date + "일", income + " " + spending);
                        date = h.getDate().substring(h.getDate().lastIndexOf("-") + 1);
                        income = 0;
                        spending = 0;
                    }
                    if(h.getKind() == 0 || h.getKind() == 2){
                        income += h.getAmount();
                    }else{
                        spending += h.getAmount();
                    }
                }
                myData.add(new ContentsCalendarItem(date,income,spending));
                Log.i(date + "일", income + " " + spending);
            }
            adapter.resetData(myCalendarList,myData);
        });
    }

    private String myDateFormatter(GregorianCalendar cal, boolean type){
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        if(type){
            Log.i("첫 날", year + "-" + month + "-" + "01");
            return year + "-" + month + "-" + "01";
        }else{
            Log.i("마지막 날", year + "-" + month + "-" + cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            return year + "-" + month + "-" + cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
    }
}
