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
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dialog.button.DeleteButton;
import com.kumoh.paylog2.dto.ContentsCategoryItem;
import com.kumoh.paylog2.dto.ContentsListBody;

import java.util.Calendar;
import java.util.List;

public class ControlIncomeHistoryDialog extends Dialog implements View.OnClickListener{
    Button dateSelectButton, categorySelectButton;
    EditText amount, description;
    Button updateButton, cancelButton, deleteButton;
    DatePickerDialog.OnDateSetListener pickerCallBack;
    ContentsCategoryItem categoryItem;
    List<ContentsCategoryItem> lists;
    ContentsListBody item;
    ControlIncomeHistoryDialogListener controlIncomeHistoryDialogListener;

    public ControlIncomeHistoryDialog(@NonNull Context context, List<ContentsCategoryItem> lists, ContentsListBody item) {
        super(context);
        this.lists = lists;
        this.item = item;
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
        updateButton = (Button) findViewById(R.id.add_income_ok_button);
        cancelButton = (Button) findViewById(R.id.add_income_cancel_button);

        // 기존 레이아웃에 삭제 버튼 추가
        DeleteButton delLayout = new DeleteButton(getContext());
        LinearLayout mainLayout = findViewById(R.id.dialog_layout);
        mainLayout.addView(delLayout);

        deleteButton = findViewById(R.id.delete_button);
        deleteButton.setBackgroundColor(getContext().getResources().getColor(R.color.income_Color));

        dateSelectButton.setText(item.getDate());
        for(ContentsCategoryItem c : lists){
            if(item.getCategoryId() == c.getId())
                categorySelectButton.setText(c.getCategory());
        }
        amount.setText(Integer.toString(item.getAmount()));
        description.setText(item.getDescription());
        updateButton.setText("수정");

        //날짜 선택 리스너
        dateSelectButton.setOnClickListener(this);
        //카테고리 선택 리스너
        categorySelectButton.setOnClickListener(this);
        //수정 버튼 리스너
        updateButton.setOnClickListener(this);
        //취소 버튼 리스너
        cancelButton.setOnClickListener(this);
        //삭제 버튼 리스너
        deleteButton.setOnClickListener(this);

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
                controlIncomeHistoryDialogListener.onUpdateButtonClicked(categoryItem.getKind(),dateSelectButton.getText().toString(),
                        categoryItem.getId(),description.getText().toString(),Integer.parseInt(amount.getText().toString()));
                dismiss();
                break;
            case R.id.add_income_cancel_button:
                cancel();
                break;
            case R.id.delete_button:
                controlIncomeHistoryDialogListener.onDeleteButtonClicked();
                dismiss();
                break;
        }
    }

    public interface ControlIncomeHistoryDialogListener{
        void onUpdateButtonClicked(int kind, String date, int category, String description, int amount);
        void onDeleteButtonClicked();
    }
    public void setControlIncomeHistoryDialogListener(ControlIncomeHistoryDialogListener controlIncomeHistoryDialogListener){
        this.controlIncomeHistoryDialogListener = controlIncomeHistoryDialogListener;
    }
}
