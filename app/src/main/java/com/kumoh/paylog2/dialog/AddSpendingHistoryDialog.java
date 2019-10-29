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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kumoh.paylog2.R;

public class AddSpendingHistoryDialog extends Dialog implements View.OnClickListener{
    Button dateSelectButton, categorySelectButton;
    EditText amount, description;
    Button addButton, cancelButton;
    DatePickerDialog.OnDateSetListener pickerCallBack;
    AddSpendingHistoryDialogListener addSpendingHistoryDialogListener;
    public AddSpendingHistoryDialog(@NonNull Context context){ super(context);}

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
    public void onClick(View v){
        switch (v.getId()){
            case R.id.add_spending_select_date_button:
                DatePickerDialog dpd = new DatePickerDialog(getContext(), pickerCallBack, 2019,9,01);
                dpd.show();
                break;
            case R.id.add_spending_select_category_button:
                break;
            case R.id.add_spending_ok_button:
                addSpendingHistoryDialogListener.onAddButtonClicked(-1,dateSelectButton.getText().toString(),
                        1,description.getText().toString(),Integer.parseInt(amount.getText().toString())*-1);
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
