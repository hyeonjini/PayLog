package com.kumoh.paylog2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.contents.category.ContentsIncomeCategoryAdapter;
import com.kumoh.paylog2.dto.ContentsCategoryItem;

import java.util.List;

// 수입 카테고리 선택 다이얼로그
public class SelectIncomeCategoryDialog extends Dialog {

    private ContentsIncomeCategoryAdapter adapter;
    private ContentsCategoryItem item;
    private List<ContentsCategoryItem> lists;
    private CategorySelectDialogListener categorySelectDialogListener;

    public SelectIncomeCategoryDialog(@NonNull Context context, List<ContentsCategoryItem> lists) {
        super(context);
        this.lists = lists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_category_income);

        RecyclerView recyclerView = findViewById(R.id.recycler_income_category);
        adapter = new ContentsIncomeCategoryAdapter(lists);
        LinearLayoutManager manager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.setOnCategoryClickListener(
                new ContentsIncomeCategoryAdapter.OnCategoryClickListener() {
                    @Override
                    public void onCategoryClick(View v, int pos) {
                        item = adapter.getAdapterItem(pos);
                        categorySelectDialogListener.onCategorySelected(item);
                        dismiss();
                    }
                });
    }

    public ContentsCategoryItem getSelectedItem(){
        return item;
    }

    public interface CategorySelectDialogListener{
        void onCategorySelected(ContentsCategoryItem item);
    }

    public void setAddIncomeHistoryDialogListener(CategorySelectDialogListener categorySelectDialogListener){
        this.categorySelectDialogListener = categorySelectDialogListener;
    }
}
