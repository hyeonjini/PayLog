package com.kumoh.paylog2.dialog.button;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.kumoh.paylog2.R;

public class DeleteButton extends LinearLayout {

    public DeleteButton(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public DeleteButton(Context context){
        super(context);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.button_delete,this,true);
    }
}
