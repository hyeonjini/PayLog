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
import com.kumoh.paylog2.dto.AccountInfo;
import com.kumoh.paylog2.util.MyException;

public class AddAccountDialog extends Dialog implements View.OnClickListener{
    private AccountInfo accountInfo;

    private TextView warningMessage;

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
    public AddAccountDialog(@NonNull Context context, AccountInfo accountInfo) {
        super(context);
        this.accountInfo = accountInfo;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.dialog_add_group);

        warningMessage = (TextView) findViewById(R.id.add_account_warning_message);
        accountName = (EditText) findViewById(R.id.add_account_name);
        subscribe = (EditText) findViewById(R.id.add_account_subscribe);
        budget = (EditText) findViewById(R.id.add_account_budget);
        addButton = (Button)findViewById(R.id.account_add_button);
        cancelButton = (Button)findViewById(R.id.account_add_cancel);

        if(accountInfo != null) {
            accountName.setText(accountInfo.getName());
            subscribe.setText(accountInfo.getSubscribe());
            budget.setText(Integer.toString(accountInfo.getBudget()));
            addButton.setText("수정");
        }

        //리스너 등록
        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.account_add_button:
                try {
                    String accountName = this.accountName.getText().toString();
                    String subscribe = this.subscribe.getText().toString();
                    int budget = Integer.parseInt(this.budget.getText().toString());

                    if(accountName.equals("")) throw new MyException("이름을 입력 해주세요.");
                    if(subscribe.equals("")) throw new MyException("설명을 입력 해주세요.");
                    // if(budget == null) 인 경우는
                    // 이미 Integer.parseInt 에서 throw 되어서 NumberFormatException 에서 catch 됨

                    if(accountInfo == null) {
                        addAccountDialogListener.onAddButtonClicked(budget,accountName,subscribe,false);
                    } else {
                        addAccountDialogListener.onReviseButtonClicked(accountInfo.getAccountId(), budget, accountName, subscribe);
                    }

                    dismiss();
                } catch (NumberFormatException e) {
                    warningMessage.setText("예산을 입력 해주세요.");
                } catch (MyException e) {
                    warningMessage.setText(e.getMessage());
                }
                break;
            case R.id.account_add_cancel:
                cancel();
                break;
        }
    }

    public interface AddAccountDialogListener {
        void onAddButtonClicked(int budget,String accountName, String subscribe, boolean isMain);
        void onReviseButtonClicked(int accountId, int budget, String accountName, String subscribe);
        // void onCancelButtonClicked();
    }
    public void setListener(AddAccountDialogListener addAccountDialogListener){
        this.addAccountDialogListener = addAccountDialogListener;
    }
}