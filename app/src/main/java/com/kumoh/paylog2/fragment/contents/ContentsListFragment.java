package com.kumoh.paylog2.fragment.contents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.ContentsListForm;

import java.util.ArrayList;

public class ContentsListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contentslist, container, false);
    }

    public ArrayList<ContentsListForm> setForm()
}


