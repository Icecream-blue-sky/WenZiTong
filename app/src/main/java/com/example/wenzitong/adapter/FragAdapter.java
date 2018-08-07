package com.example.wenzitong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 邹特强 on 2017/11/22.
 */

public class FragAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private List<String> mTitleList;
    public FragAdapter(FragmentManager fm, List<Fragment> fragmentList,List<String>mTitleList) {
        super(fm);
        this.mFragmentList = fragmentList;
        this.mTitleList = mTitleList;
    }
//获取对应位置的Fragment
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
//设置tabLayout标题,这个方法决定了标题，所以就算你在定义布局时就确定标题也没有用
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}
