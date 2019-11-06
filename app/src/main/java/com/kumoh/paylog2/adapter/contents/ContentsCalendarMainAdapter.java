package com.kumoh.paylog2.adapter.contents;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ContentsCalendarMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ArrayList<Object>> listOfLists;
    private Context context;

    public ContentsCalendarMainAdapter(Context context, ArrayList<ArrayList<Object>> listOfLists){
        this.context = context;
        this.listOfLists = listOfLists;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        protected RecyclerView recyclerView;

        public ListViewHolder(View view){
            super(view);
            this.recyclerView = view.findViewById(R.id.recycler_calendar);
        }
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_calendar_list,null);
        DisplayMetrics dm = context.getResources().getDisplayMetrics(); // 기기의 디스플레이 정보
        int width = dm.widthPixels; // 기기의 가로 길이
        view.setLayoutParams(new RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.MATCH_PARENT));

        return new ContentsCalendarMainAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder listViewHolder, int pos){
        ContentsCalendarAdapter adapter = new ContentsCalendarAdapter(listOfLists.get(pos), context);

        ((ListViewHolder) listViewHolder).recyclerView.setHasFixedSize(true);
        ((ListViewHolder) listViewHolder).recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL));
        ((ListViewHolder) listViewHolder).recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount(){
        return listOfLists.size();
    }
}
