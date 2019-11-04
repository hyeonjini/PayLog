package com.kumoh.paylog2.adapter.contents;

import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.ContentsCategoryItem;

import java.util.List;

public class ContentsCategoryDialogAdapter
        extends RecyclerView.Adapter<ContentsCategoryDialogAdapter.CategorySubViewHolder> {

    private List<ContentsCategoryItem> list;
    private OnCategoryClickListener listener;

    public ContentsCategoryDialogAdapter(List<ContentsCategoryItem> list){
        this.list = list;
    }

    public class CategorySubViewHolder extends RecyclerView.ViewHolder{
        protected ImageView imageView;
        protected TextView textView;

        public CategorySubViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.category_img);
            textView = view.findViewById(R.id.category_text);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(listener != null){
                            listener.onCategoryClick(v,pos);
                        }
                    }
                }
            });
        }
    }

    @Override
    public CategorySubViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_contents_category, null);

        CategorySubViewHolder viewHolder = new CategorySubViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull CategorySubViewHolder viewHolder, int pos)
    {
        viewHolder.imageView.setImageURI(
                Uri.parse("android.resource://com.kumoh.paylog2/"
                        + getIconValue(list.get(pos).getCategory())));
        viewHolder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        viewHolder.textView.setText(list.get(pos).getCategory());
    }
    @Override
    public int getItemCount()
    {
        Log.i("크기",Integer.toString(list.size()));
        return (null != list ? list.size() : 0);
    }

    public interface OnCategoryClickListener{
        void onCategoryClick(View v, int pos);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener){
        this.listener = listener;
    }

    public ContentsCategoryItem getAdapterItem(int pos){
        return list.get(pos);
    }

    // 카테고리 별 아이콘 R.drawable 값 가져오기
    public int getIconValue(String category) {
        switch (category){
            case "식비":
                return R.drawable.black_icon_fork_14dp;
            case "카페/간식":
                return R.drawable.black_icon_cafesnack_24dp;
            case "술/유흥":
                return R.drawable.black_icon_drink_24dp;
            case "생활":
                return R.drawable.black_icon_broom_14dp;
            case "문화/여가":
                return R.drawable.black_icon_culture_24dp;
            case "패션/쇼핑":
                return R.drawable.black_icon_shirt_14dp;
            case "교통":
                return R.drawable.black_icon_bus_24dp;
            case "자동차":
                return R.drawable.black_icon_car_24dp;
            case "주거":
                return R.drawable.black_icon_house_24dp;
            case "통신비":
                return R.drawable.black_icon_phone_14dp;
            case "의료/건강":
                return R.drawable.black_icon_medikit_24dp;
            case "금융":
                return R.drawable.black_icon_wallet_24dp;
            case "여행":
                return R.drawable.black_icon_travel_24dp;
            case "교육":
                return R.drawable.black_icon_education_24dp;
            case "자녀":
                return R.drawable.black_icon_pencil_24dp;
            case "경조사/선물":
                return R.drawable.black_icon_present_24dp;
            default:
                return 0;
        }
    }
}
