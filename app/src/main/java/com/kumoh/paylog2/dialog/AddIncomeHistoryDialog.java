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

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.ContentsCategoryItem;

import java.util.Calendar;
import java.util.List;

public class AddIncomeHistoryDialog extends Dialog implements View.OnClickListener {
    Button dateSelectButton, categorySelectButton;
    EditText amount, description;
    Button addButton, cancelButton;
    DatePickerDialog.OnDateSetListener pickerCallBack;
    ContentsCategoryItem categoryItem;
    List<ContentsCategoryItem> lists;
    AddIncomeHistoryDialogListener addIncomeHistoryDialogListener;

    public AddIncomeHistoryDialog(Context context, List<ContentsCategoryItem> lists){
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

        setContentView(R.layout.dialog_add_income);

        dateSelectButton = (Button) findViewById(R.id.add_income_select_date_button);
        categorySelectButton = (Button) findViewById(R.id.add_income_select_category_button);
        amount = (EditText) findViewById(R.id.add_income_amount_text);
        description = (EditText) findViewById(R.id.add_income_description_text);
        addButton = (Button) findViewById(R.id.add_income_ok_button);
        cancelButton = (Button) findViewById(R.id.add_income_cancel_button);

        //날짜 선택 리스너
        dateSelectButton.setOnClickListener(this);
        //카테고리 선택 리스너
        categorySelectButton.setOnClickListener(this);
        //추가 버튼 리스터
        addButton.setOnClickListener(this);

        //date picker callback method
        pickerCallBack = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateSelectButton.setText(year+"-"+(month+1)+"-"+day);
            }
        };
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_income_select_date_button:
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); int mMonth = c.get(Calendar.MONTH); int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(getContext(), pickerCallBack, mYear,mMonth,mDay);
                dpd.show();
                break;
            case R.id.add_income_select_category_button:
                SelectIncomeCategoryDialog sid = new SelectIncomeCategoryDialog(getContext(), lists);
                sid.setAddIncomeHistoryDialogListener(new SelectIncomeCategoryDialog.CategorySelectDialogListener() {
                    @Override
                    public void onCategorySelected(ContentsCategoryItem item) {
                        categoryItem = sid.getSelectedItem();
                        categorySelectButton.setText(categoryItem.getCategory());
                    }
                });
                sid.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                sid.show();
                break;
            case R.id.add_income_ok_button:
                addIncomeHistoryDialogListener.onAddButtonClicked(categoryItem.getKind(),dateSelectButton.getText().toString(),
                        categoryItem.getId(),description.getText().toString(),Integer.parseInt(amount.getText().toString()));
                dismiss();
                break;
            case R.id.add_income_cancel_button:
                cancel();
                break;
        }
    }

    public interface AddIncomeHistoryDialogListener{
        void onAddButtonClicked(int kind, String date, int category, String description, int amount);
    }
    public void setAddIncomeHistoryDialogListener(AddIncomeHistoryDialogListener addIncomeHistoryDialogListener){
        this.addIncomeHistoryDialogListener = addIncomeHistoryDialogListener;
    }
}
