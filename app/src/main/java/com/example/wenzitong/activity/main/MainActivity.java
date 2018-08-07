package com.example.wenzitong.activity.main;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wenzitong.R;
import com.example.wenzitong.adapter.FragAdapter;
import com.example.wenzitong.entity.BaseResponse;
import com.example.wenzitong.entity.LoginResponseData;
import com.example.wenzitong.ui.Fragment.CollectPageFragment;
import com.example.wenzitong.ui.Fragment.FirstPageFragment;
import com.example.wenzitong.ui.Fragment.PersonalPageFragment;
import com.example.wenzitong.untils.retrofitUtil.RetroHttpUtil;
import com.example.wenzitong.untils.retrofitUtil.callback.AbstractCommonHttpCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * 主活动界面
 */
public class MainActivity extends AppCompatActivity {
    /**
     * onCreate方法编写应该遵守solid原则，即单一功能
     */
    private TabLayout myTabLayout;
    private ViewPager mViewPager;
    private FragAdapter mFragAdapter;
    private FirstPageFragment firstPageFragment;//首页
    private CollectPageFragment collectPageFragment;//收藏页
    private PersonalPageFragment personalPageFragment;//个人中心页
    private List<Fragment> fragmentList;//存储fragment
    private List<String> titleList;//tabLayout标题
    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideActionBar();
        initView();
        initPages();
    }
    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
    }

    /**
     * 获取需要的所有布局控件
     */
    private void initView() {
        myTabLayout = findViewById(R.id.myTabLayout);
        mViewPager = findViewById(R.id.view_pager);
        firstPageFragment = new FirstPageFragment();
        collectPageFragment = new CollectPageFragment();
        personalPageFragment = new PersonalPageFragment();
        fragmentList=new ArrayList<>();
        fragmentList.add(firstPageFragment);
        fragmentList.add(collectPageFragment);
        fragmentList.add(personalPageFragment);
        titleList=new ArrayList<>();
        titleList.add("首页");
        titleList.add("收藏");
        titleList.add("个人中心");
//        test = findViewById(R.id.test);
//        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/test.ttf");
//        test.setTypeface(typeface);

    }

    /**
     * 初始化TabLayout
     */
    private void initPages() {
        mFragAdapter = new FragAdapter(getSupportFragmentManager(), fragmentList, titleList);
        mViewPager.setAdapter(mFragAdapter);
        myTabLayout.setupWithViewPager(mViewPager);
    }
}
