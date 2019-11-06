package com.kumoh.paylog2.adapter.contents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.ContentsListDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ContentsCalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int HEADER_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;

    private ArrayList<Object> calList;
    private Context context;

    public ContentsCalendarAdapter(ArrayList<Object> calList, Context context){
        this.calList = calList;
        this.context = context;
    }

    // 헤더 타입을 위한 뷰 홀더
    private class HeaderViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private TextView weeklyIncome;
        private TextView weeklySpending;

        public HeaderViewHolder(@NonNull View view){
            super(view);
            this.textView = view.findViewById(R.id.cal_header_text);
            this.weeklyIncome = view.findViewById(R.id.cal_header_income);
            this.weeklySpending = view.findViewById(R.id.cal_header_spending);
        }
    }
    // 빈 일자 타입을 위한 뷰 홀더
    private class EmptyViewHolder extends RecyclerView.ViewHolder{

        public EmptyViewHolder(@NonNull View view){
            super(view);
        }
    }
    // 일자 타입을 위한 뷰 홀더
    private class DayViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private View view;

        public DayViewHolder(@NonNull View view){
            super(view);
            this.textView = view.findViewById(R.id.cal_day_text);
            this.view = view.findViewById(R.id.cal_day_view);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type){

        if(type == HEADER_TYPE){ // 헤더 타입
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_calendar_header, viewGroup, false);

            StaggeredGridLayoutManager.LayoutParams params
                    = (StaggeredGridLayoutManager.LayoutParams)view.getLayoutParams();
            params.setFullSpan(true); // Span 하나로 통합
            view.setLayoutParams(params);

            return new HeaderViewHolder(view);
        } else if(type == EMPTY_TYPE){ // 빈 일자 타입
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_calendar_empty, viewGroup, false);

            return new EmptyViewHolder(view);
        } else{ // 일자 타입
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_calendar_day, viewGroup, false);

            return new DayViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pos){
        int viewType = getItemViewType(pos);

        if(viewType == HEADER_TYPE){
            HeaderViewHolder holder = (HeaderViewHolder)viewHolder;
            Object item = calList.get(pos);
            if(item instanceof Long){
                ((HeaderViewHolder) viewHolder).textView.setText(formatDate((Long)item, "yyyy년 MM월"));
            }
        } else if(viewType == EMPTY_TYPE){
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
        } else if(viewType == DAY_TYPE){
            DayViewHolder holder = (DayViewHolder)viewHolder;
            Object item = calList.get(pos);
            if(item instanceof Calendar){
                Long temp = ((Calendar) item).getTimeInMillis();
                ((DayViewHolder) viewHolder).textView.setText(formatDate((Long)temp, "dd"));
            }
        }
    }

    @Override
    public int getItemViewType(int pos){ // 뷰타입 구분
        Object item = calList.get(pos);

        if(item instanceof Long){ // 헤더 타입
            return HEADER_TYPE;
        } else if(item instanceof String){ // 비어있는 일자 타입
            return EMPTY_TYPE;
        } else{ // 일자 타입
            return DAY_TYPE;
        }
    }

    @Override
    public int getItemCount(){
        return (null != calList ? calList.size() : 0);
    }

    public static String formatDate(Long date, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
            Date d = new Date(date);
            return formatter.format(d).toUpperCase();
        } catch (Exception e) {
            return " ";
        }
    }
}
