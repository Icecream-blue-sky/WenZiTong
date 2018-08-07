package com.example.wenzitong.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wenzitong.R;
import com.example.wenzitong.entity.NewWordBean;
import com.example.wenzitong.ui.SuperRecyclerView.DataHolder;
import com.example.wenzitong.ui.SuperRecyclerView.LayoutWrapper;
import com.example.wenzitong.ui.SuperRecyclerView.SuperAdapter;
import com.example.wenzitong.ui.SuperRecyclerView.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 邹特强 on 2017/11/22.
 * 收藏界面对应fragment
 */

public class CollectPageFragment extends Fragment {
    private View view;//fragment对应总布局
    private RecyclerView newWordRv;//生词列表
    private SuperAdapter newWordRvAdapter;//对应adapter
    private TextView newWordContentTv;//生词解释
    private boolean isNewContentTvClicked=false;//记录生词解释内容是否被点击

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collectpage, container, false);
        initViews();
        initRecyclerView();
        return view;
    }

    /**
     * 初始化布局控件
     */
    public void initViews() {
        newWordRv = view.findViewById(R.id.new_word_recycler);
    }

    /**
     * 初始化RecyclerView
     */
    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        newWordRv.setLayoutManager(layoutManager);
        int[] layoutIds = {R.layout.new_word_recycler_item};
        newWordRvAdapter = new SuperAdapter(getContext(), layoutIds);
        newWordRv.setAdapter(newWordRvAdapter);
        final DataHolder<NewWordBean> newWordDataHolder = new DataHolder<NewWordBean>() {
            @Override
            public void bind(Context context, SuperViewHolder holder, NewWordBean item, int position) {
                newWordContentTv = holder.getView(R.id.content_tv);
                newWordContentTv.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        newWordContentTv=(TextView)v;
                        if(!isNewContentTvClicked) {
                            newWordContentTv.setMaxLines(10000);
                            isNewContentTvClicked=true;
                        }
                        else{
                            newWordContentTv.setMaxLines(4);
                            isNewContentTvClicked=false;
                        }
                    }
                });
            }
        };
        List<LayoutWrapper> data = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            data.add(new LayoutWrapper(R.layout.new_word_recycler_item, new NewWordBean(), newWordDataHolder));
        }
        newWordRvAdapter.setData(data);

    }

    /**
     * 初始化监听事件
     */
    public void initListeners() {

    }
}
