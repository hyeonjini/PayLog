package com.kumoh.paylog2.adapter.contents;

import android.net.Uri;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.ContentsCategoryItem;
import com.kumoh.paylog2.dto.ContentsStatisticsCategoryItem;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class ContentsStatisticsRecyclerAdapter
        extends RecyclerView.Adapter<ContentsStatisticsRecyclerAdapter.StatisticsCategoryViewHolder> {

    private List<ContentsStatisticsCategoryItem> list;
    private List<ContentsCategoryItem> categories;

    public class StatisticsCategoryViewHolder extends RecyclerView.ViewHolder{
        protected ImageView icon;
        protected TextView category;
        protected TextView value;

        public StatisticsCategoryViewHolder(View view){
            super(view);
            this.icon = view.findViewById(R.id.stat_icon);
            this.category = view.findViewById(R.id.stat_category);
            this.value = view.findViewById(R.id.stat_value);
        }
    }

    public ContentsStatisticsRecyclerAdapter(List<ContentsStatisticsCategoryItem> list, List<ContentsCategoryItem> categories) {
        this.list = list;
        this.categories = categories;
    }

    @Override
    public StatisticsCategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_contents_statistics, viewGroup, false);

        StatisticsCategoryViewHolder viewHolder = new StatisticsCategoryViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull StatisticsCategoryViewHolder viewHolder, int pos)
    {
        viewHolder.category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        viewHolder.category.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        viewHolder.value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        viewHolder.value.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);

        viewHolder.value.setTextColor(ContextCompat.getColor(viewHolder.value.getContext(), R.color.mainColor));

        viewHolder.icon.setImageURI(
                Uri.parse("android.resource://com.kumoh.paylog2/" + getIconValue(list.get(pos).getCategoryId())));
        viewHolder.category.setText(getSpendingCategoryById(list.get(pos).getCategoryId()));
        DecimalFormat format = new DecimalFormat(" ###,###");
        String symbol = Currency.getInstance(Locale.KOREA).getSymbol();
        viewHolder.value.setText(symbol + format.format(list.get(pos).getAmount()));
    }
    @Override
    public int getItemCount()
    {
        return (null != list ? list.size() : 0);
    }

    public void setData(List<ContentsStatisticsCategoryItem> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void setCategories(List<ContentsCategoryItem> categories){
        this.categories = categories;
        notifyDataSetChanged();
    }

    private String getSpendingCategoryById(int id){
        String category = null;
        for(ContentsCategoryItem c : categories){
            if(id == c.getId())
                category = c.getCategory();
        }
        return category;
    }

    // 카테고리 별 아이콘 R.drawable 값 가져오기
    public int getIconValue(int categoryId) {
        switch (categoryId){
            case 5:
                return R.drawable.black_icon_fork_14dp;
            case 6:
                return R.drawable.black_icon_cafesnack_24dp;
            case 7:
                return R.drawable.black_icon_drink_24dp;
            case 8:
                return R.drawable.black_icon_broom_14dp;
            case 9:
                return R.drawable.black_icon_culture_24dp;
            case 10:
                return R.drawable.black_icon_shirt_14dp;
            case 11:
                return R.drawable.black_icon_bus_24dp;
            case 12:
                return R.drawable.black_icon_car_24dp;
            case 13:
                return R.drawable.black_icon_house_24dp;
            case 14:
                return R.drawable.black_icon_phone_14dp;
            case 15:
                return R.drawable.black_icon_medikit_24dp;
            case 16:
                return R.drawable.black_icon_wallet_24dp;
            case 17:
                return R.drawable.black_icon_travel_24dp;
            case 18:
                return R.drawable.black_icon_education_24dp;
            case 19:
                return R.drawable.black_icon_pencil_24dp;
            case 20:
                return R.drawable.black_icon_present_24dp;
            default:
                return R.drawable.black_icon_question_24dp;
        }
    }
}