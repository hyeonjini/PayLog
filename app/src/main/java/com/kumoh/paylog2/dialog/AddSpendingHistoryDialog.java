package com.kumoh.paylog2.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.ContentsCategoryItem;

import java.util.Calendar;
import java.util.List;

public class AddSpendingHistoryDialog extends Dialog implements View.OnClickListener{
    Button dateSelectButton, categorySelectButton;
    EditText amount, description;
    Button addButton, cancelButton;
    DatePickerDialog.OnDateSetListener pickerCallBack;
    ContentsCategoryItem categoryItem;
    List<ContentsCategoryItem> lists;
    AddSpendingHistoryDialogListener addSpendingHistoryDialogListener;

    public AddSpendingHistoryDialog(@NonNull Context context, List<ContentsCategoryItem> lists){
        super(context);
        this.lists = lists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.dialog_add_spending);

        dateSelectButton = (Button) findViewById(R.id.add_spending_select_date_button);
        categorySelectButton = (Button) findViewById(R.id.add_spending_select_category_button);
        amount = (EditText) findViewById(R.id.add_spending_amount_text);
        description = (EditText) findViewById(R.id.add_spending_description_text);
        addButton = (Button) findViewById(R.id.add_spending_ok_button);
        cancelButton = (Button) findViewById(R.id.add_spending_cancel_button);

        //날짜 선택 리스너
        dateSelectButton.setOnClickListener(this);
        //카테고리 선택 리스너
        categorySelectButton.setOnClickListener(this);
        //추가 버튼 리스너
        addButton.setOnClickListener(this);
        //취소 버튼 리스너
        cancelButton.setOnClickListener(this);

        //date picker callback method
        pickerCallBack = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateSelectButton.setText(year+"-"+(month+1)+"-"+day);
            }
        };
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.add_spending_select_date_button:
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); int mMonth = c.get(Calendar.MONTH); int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(getContext(), pickerCallBack, mYear,mMonth,mDay);
                dpd.show();
                break;
            case R.id.add_spending_select_category_button:
                SelectSpendingCategoryDialog ssd = new SelectSpendingCategoryDialog(getContext(), lists);
                ssd.setAddSpendingHistoryDialogListener(new SelectSpendingCategoryDialog.CategorySelectDialogListener() {
                    @Override
                    public void onCategorySelected(ContentsCategoryItem item) {
                        categoryItem = ssd.getSelectedItem();
                        categorySelectButton.setText(categoryItem.getCategory());
                    }
                });
                ssd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                ssd.show();
                break;
            case R.id.add_spending_ok_button:
                addSpendingHistoryDialogListener.onAddButtonClicked(categoryItem.getKind(),dateSelectButton.getText().toString(),
                        categoryItem.getId(),description.getText().toString(),Integer.parseInt(amount.getText().toString())*-1);
                dismiss();
                break;
            case R.id.add_spending_cancel_button:
                cancel();
                break;
        }
    }
    public interface AddSpendingHistoryDialogListener{
        void onAddButtonClicked(int kind, String date, int category, String description, int amount);
    }
    public void setAddSpendingHistoryDialogListener(AddSpendingHistoryDialogListener addSpendingHistoryDialogListener){
        this.addSpendingHistoryDialogListener = addSpendingHistoryDialogListener;
    }
}
