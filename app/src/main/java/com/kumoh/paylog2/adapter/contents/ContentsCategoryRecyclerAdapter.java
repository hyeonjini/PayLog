package com.kumoh.paylog2.adapter.contents;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.ContentsCategoryItem;

import java.util.List;

public class ContentsCategoryRecyclerAdapter
        extends RecyclerView.Adapter<ContentsCategoryRecyclerAdapter.CategoryViewHolder> {

    private List<ContentsCategoryItem> list;

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        protected TextView percent;
        protected TextView category;
        protected TextView value;

        public CategoryViewHolder(View view){
            super(view);
            this.percent = view.findViewById(R.id.stat_percent);
            this.category = view.findViewById(R.id.stat_category);
            this.value = view.findViewById(R.id.stat_value);
        }
    }

    public ContentsCategoryRecyclerAdapter(List<ContentsCategoryItem> list) {
        this.list = list;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_contents_statistics, viewGroup, false);

        CategoryViewHolder viewHolder = new CategoryViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder viewHolder, int pos)
    {
        viewHolder.percent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewHolder.category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewHolder.value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewHolder.percent.setGravity(Gravity.CENTER);
        viewHolder.category.setGravity(Gravity.CENTER);
        viewHolder.value.setGravity(Gravity.CENTER);

        viewHolder.percent.setText(Float.toString(list.get(pos).getPercent()));
        viewHolder.category.setText(list.get(pos).getCategory());
        viewHolder.value.setText(list.get(pos).getValue());
    }
    @Override
    public int getItemCount()
    {
        return (null != list ? list.size() : 0);
    }
}
