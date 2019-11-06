package com.kumoh.paylog2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.contents.category.ContentsSpendingCategoryAdapter;
import com.kumoh.paylog2.dto.ContentsCategoryItem;

import java.util.List;

// 지출 카테고리 선택 다이얼로그
public class SelectSpendingCategoryDialog extends Dialog{

    private ContentsSpendingCategoryAdapter adapter;
    private ContentsCategoryItem item;
    private List<ContentsCategoryItem> lists;
    private CategorySelectDialogListener categorySelectDialogListener;

    public SelectSpendingCategoryDialog(@NonNull Context context, List<ContentsCategoryItem> lists) {
        super(context);
        this.lists = lists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_category_spending);

        RecyclerView recyclerView = findViewById(R.id.recycler_spending_category);
        adapter = new ContentsSpendingCategoryAdapter(lists);
        StaggeredGridLayoutManager manager
                = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.setOnCategoryClickListener(
                new ContentsSpendingCategoryAdapter.OnCategoryClickListener() {
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

    public void setAddSpendingHistoryDialogListener(CategorySelectDialogListener categorySelectDialogListener){
        this.categorySelectDialogListener = categorySelectDialogListener;
    }
}
