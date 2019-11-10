package com.kumoh.paylog2.adapter.contents;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.ContentsCalendarItem;
import com.kumoh.paylog2.dto.ContentsListDay;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ContentsCalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int HEADER_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;

    private ArrayList<Object> calList;
    private ArrayList<ContentsCalendarItem> dataList;
    private ArrayList<ContentsCalendarItem> headerList;
    private int weekCounter = 0;
    private Context context;

    public ContentsCalendarAdapter(ArrayList<Object> calList, ArrayList<ContentsCalendarItem> dataList, Context context){
        this.calList = calList;
        this.dataList = dataList;
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
        private TextView incomeView;
        private TextView spendingView;

        public DayViewHolder(@NonNull View view){
            super(view);
            this.textView = view.findViewById(R.id.cal_day_text);
            this.incomeView = view.findViewById(R.id.cal_day_income);
            this.spendingView = view.findViewById(R.id.cal_day_spending);
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
                holder.weeklyIncome.setText("수입 " + formatMoney(headerList.get(weekCounter).getIncome()));
                holder.weeklySpending.setText("지출 " + formatMoney(headerList.get(weekCounter).getSpending()));
                holder.textView.setText((++weekCounter) + "주차");
            }
        }
        else if(viewType == EMPTY_TYPE){
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
        }
        else if(viewType == DAY_TYPE){
            DayViewHolder holder = (DayViewHolder)viewHolder;
            Object item = calList.get(pos);
            if(item instanceof Calendar){
                Long temp = ((Calendar) item).getTimeInMillis();
                String date = formatDate(temp, "dd");
                holder.textView.setText(date);
                if(dataList != null){
                    for(ContentsCalendarItem c : dataList){
                        if(c.getDate().equals(date)){
                            Log.i("데이터 있음", date);
                            holder.incomeView.setText(formatMoney(c.getIncome()));
                            holder.spendingView.setText(formatMoney(c.getSpending()));
                            break;
                        }
                    }
                }
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

    private ArrayList<ContentsCalendarItem> setHeaderData(){
        ArrayList<ContentsCalendarItem> headerData = new ArrayList<>();
        ContentsCalendarItem temp = null;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis((Long)calList.get(0));

        int dayCounter = 1;
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int lastDateOfMonth = cal.getActualMaximum(Calendar.DAY_OF_WEEK);

        // 주간 별 헤더 추가
        for(int weekCounter = 1; dayCounter <= lastDateOfMonth; weekCounter++){
            temp = new ContentsCalendarItem(Integer.toString(weekCounter), 0,0);
            if(dataList != null){
                for(ContentsCalendarItem c : dataList){
                    if(Integer.parseInt(c.getDate()) > (7 * (weekCounter-1) + 1 - dayOfWeek)
                            && Integer.parseInt(c.getDate()) <= (7 * weekCounter + 1 - dayOfWeek)){
                        temp.setIncome(temp.getIncome() + c.getIncome());
                        temp.setSpending(temp.getSpending() + c.getSpending());
                    }
                }
            }
            dayCounter++;
            headerData.add(temp);
        }

        return headerData;
    }

    private String formatDate(Long date, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
            Date d = new Date(date);
            return formatter.format(d).toUpperCase();
        } catch (Exception e) {
            return " ";
        }
    }

    private String formatMoney(int amount){
        try{
            DecimalFormat format = new DecimalFormat(" ###,###");
            return format.format(amount);
        } catch(Exception e){
            return Integer.toString(amount);
        }
    }

    public void resetData(ArrayList<Object> calList, ArrayList<ContentsCalendarItem> dataList){
        weekCounter = 0;
        this.calList = calList;
        this.dataList = dataList;
        this.headerList = setHeaderData();
        Log.i("데이터 크기", this.dataList.size() + "");
        notifyDataSetChanged();
    }
}
