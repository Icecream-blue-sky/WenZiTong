package com.example.wenzitong.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wenzitong.R;

/**
 * Created by 邹特强 on 2017/11/22.
 * 个人中心解码对应fragment
 */

public class PersonalPageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personalpage,container,false);
        return view;
    }
}
