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

public class NewGroupAddDialog extends Dialog implements View.OnClickListener{
    private EditText groupName;
    private EditText contents;
    private Button addButton;
    private Button cancelButton;

    private NewGroupAddDialogListener newGroupAddDialogListener;

    public NewGroupAddDialog(@NonNull Context context) {
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

        groupName = (EditText) findViewById(R.id.add_group_text);
        contents = (EditText) findViewById(R.id.add_group_contents);

        addButton = (Button)findViewById(R.id.group_add_button);
        cancelButton = (Button)findViewById(R.id.group_add_cancel);

        //리스너 등록
        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.group_add_button:
                String groupName = this.groupName.getText().toString();
                String contents = this.contents.getText().toString();
                newGroupAddDialogListener.onAddButtonClicked(groupName,contents);
                dismiss();
                break;
            case R.id.group_add_cancel:
                cancel();
                break;
        }
    }

    public interface NewGroupAddDialogListener {
        void onAddButtonClicked(String groupName, String contents);
        void onCancelButtonClicked();
    }
    public void setListener(NewGroupAddDialogListener newGroupAddDialogListener){
        this.newGroupAddDialogListener = newGroupAddDialogListener;
    }
}
