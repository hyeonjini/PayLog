package com.kumoh.paylog2.adapter.contents;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.ContentsCategoryItem;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class ContentsCategoryRecyclerAdapter
        extends RecyclerView.Adapter<ContentsCategoryRecyclerAdapter.CategoryViewHolder> {

    private List<ContentsCategoryItem> list;

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        protected ImageView icon;
        protected TextView category;
        protected TextView value;

        public CategoryViewHolder(View view){
            super(view);
            this.icon = view.findViewById(R.id.stat_icon);
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
        viewHolder.category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        viewHolder.value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        viewHolder.category.setGravity(Gravity.CENTER);
        viewHolder.value.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        viewHolder.value.setTextColor(ContextCompat.getColor(viewHolder.value.getContext(), R.color.mainColor));

        viewHolder.icon.setImageURI(
                Uri.parse("android.resource://com.kumoh.paylog2/" + getIconValue(list.get(pos).getCategory())));
        viewHolder.category.setText(list.get(pos).getCategory());
        DecimalFormat format = new DecimalFormat(" ###,###");
        String symbol = Currency.getInstance(Locale.KOREA).getSymbol();
        viewHolder.value.setText(symbol + format.format(list.get(pos).getValue()));
    }
    @Override
    public int getItemCount()
    {
        return (null != list ? list.size() : 0);
    }

    // 카테고리 별 아이콘 R.drawable 값 가져오기
    public int getIconValue(String category) {
        switch (category){
            case "식비":
                return R.drawable.black_icon_fork_14dp;
            case "생활비":
                return R.drawable.black_icon_broom_14dp;
            case "교통비":
                return R.drawable.black_icon_bus_14dp;
            case "통신비":
                return R.drawable.black_icon_phone_14dp;
            case "문화":
                return R.drawable.black_icon_culture_24dp;
            case "쇼핑":
                return R.drawable.black_icon_shirt_14dp;
            default:
                return 0;
        }
    }
}