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

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.ContentsCategoryItem;
import com.kumoh.paylog2.util.MyException;

import java.util.Calendar;
import java.util.List;

public class AddIncomeHistoryDialog extends Dialog implements View.OnClickListener {
    Button dateSelectButton, categorySelectButton;
    EditText amountEdit, descriptionEdit;
    Button addButton, cancelButton;
    TextView warningMessage;
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
        amountEdit = (EditText) findViewById(R.id.add_income_amount_text);
        descriptionEdit = (EditText) findViewById(R.id.add_income_description_text);
        addButton = (Button) findViewById(R.id.add_income_ok_button);
        cancelButton = (Button) findViewById(R.id.add_income_cancel_button);
        warningMessage = (TextView) findViewById(R.id.add_income_warning_message);

        // 리스너 등록
        dateSelectButton.setOnClickListener(this);
        categorySelectButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
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
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_income_select_date_button:
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); int mMonth = c.get(Calendar.MONTH); int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(getContext(), R.style.DatePickerDialogTheme, pickerCallBack, mYear,mMonth,mDay);
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
                try {
                    if(categoryItem == null) throw new MyException("분류를 선택 해주세요");

                    int kind = categoryItem.getKind();
                    String date = dateSelectButton.getText().toString();
                    int category = categoryItem.getId();
                    String description = descriptionEdit.getText().toString();
                    int amount = Integer.parseInt(amountEdit.getText().toString());

                    // 날짜 선택이 기본 값임
                    if(date.equals("날짜 선택")) throw new MyException("날짜를 선택 해주세요");
                    if(description.equals("")) throw new MyException("내용을 기재 해주세요");

                    addIncomeHistoryDialogListener.onAddButtonClicked(kind, date, category, description, amount);

                    dismiss();
                } catch (NumberFormatException e) {
                    warningMessage.setText("금액을 입력 해주세요");
                }
                catch (MyException e) {
                    warningMessage.setText(e.getMessage());
                }
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
