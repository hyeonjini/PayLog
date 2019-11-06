package com.kumoh.paylog2.adapter.contents.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.ContentsCategoryItem;

import java.util.List;

public class ContentsIncomeCategoryAdapter
        extends RecyclerView.Adapter<ContentsIncomeCategoryAdapter.CategoryViewHolder> {

    private List<ContentsCategoryItem> list;
    private OnCategoryClickListener listener;

    public ContentsIncomeCategoryAdapter(List<ContentsCategoryItem> list){
        this.list = list;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        protected TextView textView;

        public CategoryViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.category_income_text);

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
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_contents_category_income, null);

        CategoryViewHolder viewHolder = new CategoryViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder viewHolder, int pos)
    {
        viewHolder.textView.setText(list.get(pos).getCategory());
    }

    @Override
    public int getItemCount()
    {
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
}
