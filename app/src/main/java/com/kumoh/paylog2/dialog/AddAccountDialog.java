package com.kumoh.paylog2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kumoh.paylog2.R;

public class AddAccountDialog extends Dialog implements View.OnClickListener{
    private EditText accountName;
    private EditText budget;
    private EditText subscribe;
    private EditText isMain;

    private Button addButton;
    private Button cancelButton;

    private AddAccountDialogListener addAccountDialogListener;

    public AddAccountDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.dialog_add_group);

        accountName = (EditText) findViewById(R.id.add_account_name);
        subscribe = (EditText) findViewById(R.id.add_account_subscribe);
        budget = (EditText) findViewById(R.id.add_account_budget);
        addButton = (Button)findViewById(R.id.account_add_button);
        cancelButton = (Button)findViewById(R.id.account_add_cancel);

        //리스너 등록
        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.account_add_button:
                int budget = Integer.parseInt(this.budget.getText().toString());
                boolean isMain = false;
                String accountName = this.accountName.getText().toString();
                String subscribe = this.subscribe.getText().toString();
                addAccountDialogListener.onAddButtonClicked(budget,accountName,subscribe,isMain);
                dismiss();
                break;
            case R.id.account_add_cancel:
                cancel();
                break;
        }
    }

    public interface AddAccountDialogListener {
        void onAddButtonClicked(int budget,String accountName, String subscribe, boolean isMain);
        void onCancelButtonClicked();
    }
    public void setListener(AddAccountDialogListener addAccountDialogListener){
        this.addAccountDialogListener = addAccountDialogListener;
    }
}