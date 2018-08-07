package com.example.wenzitong.ui.SuperRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 工具类：多布局的Adapter，复杂数据类型，使用LayoutWrapper包装
 * 多布局多实体：每个不同的layout（ViewHolder）需要在Adapter里实现不同的DataHolder接口来初始化各实体的布局
 * 同时需要引入：
 * LayoutWrapper：布局包装类
 * SuperViewHolder：ViewHolder的基类
 * DataHolder（Interface）： 接口中有bind方法，实现将（对应Item的）数据和（对应Item的）ViewHolder进行绑定，
 * 并设置监听。
 */
public class SuperAdapter extends RecyclerView.Adapter<SuperViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<LayoutWrapper> items = new ArrayList<>();
    /**
     *items的参数：
     *private int layoutId;//布局id
     * 注意：layoutId必须是layoutIds数组中的成员！
     *private int spanSize;//布局所占列数（可选）
     *private T data;//数据源
     *private DataHolder<T> holder;//控制器-负责将数据源绑定到布局上，并设置监听
     */
    private Map<Integer, Integer> layoutMap = new HashMap<>();

    //定义接口
    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }
    public interface OnItemLongClickListener {
        void OnItemLongClick(View view, int position);
    }
    //定义接口变量
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    public void setmOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public OnItemClickListener getmOnItemClickListener() {return mOnItemClickListener;
    }
    public void setmOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }
    public OnItemLongClickListener getmOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }



    /**
     * 建立SuperAdapter后，实际上是建立了一个layoutMap用于记录layoutId与viewType的对应关系，还未装载布局
     * 还需要建立一个LayoutWrapper数组items，执行setData(List<LayoutWrapper> list)
     *@param context 上下文
     * @param layoutIds items中所有布局的id组成的数组
     */
    public SuperAdapter(Context context, int[] layoutIds) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        for (int i = 0; i < layoutIds.length; i++) {
            layoutMap.put(i, layoutIds[i]);
        }
    }

    public Context getContext() {
        return context;
    }

    //ViewType转LayoutId
    public int getLayoutId(int viewType) {
        return layoutMap.get(viewType);
    }

    //LayoutId转ViewType
    public int getViewType(int position) {
        LayoutWrapper wrapper = items.get(position);
        Iterator iter = layoutMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Integer key = (Integer) entry.getKey();
            Integer val = (Integer) entry.getValue();
            if (val == wrapper.getLayoutId()) {
                return key;
            }
        }
        return 0;
    }

    //创建各个ViewHolder实例
    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SuperViewHolder(inflater.inflate(getLayoutId(viewType), parent, false));
    }

    //将ViewHolder绑定在对应position（item）
    // bind方法将（对应Item的）数据和（对应Item的）ViewHolder进行绑定，并设置监听。
    // bind为DataHolder接口中的函数，需要在adapter中重写！
    @Override
    public void onBindViewHolder(final SuperViewHolder holder,int position) {
        LayoutWrapper wrapper = items.get(position);
        wrapper.getHolder().bind(context, holder, wrapper.getData(), position);

    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //设置数据源
    public void setData(List<LayoutWrapper> list) {
        this.items = list;
        notifyDataSetChanged();
    }
}
/**
 //多布局多实体多列的Adapter
 private RecyclerView rvContent;

 private void testSuperAdapter02() {
 rvContent = (RecyclerView) findViewById(R.id.rv_content);
 GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

 final List<LayoutWrapper> data = new ArrayList<>();
 gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
@Override
public int getSpanSize(int position) {
return data.get(position).getSpanSize();
}
});
 rvContent.setLayoutManager(gridLayoutManager);
 int[] layoutIds = {R.layout.item_simple, R.layout.item_super,};
 SuperAdapter adapter = new SuperAdapter(this, layoutIds);
 rvContent.setAdapter(adapter);

 DataHolder<SimpleBean> holderSimple = new DataHolder<SimpleBean>() {
@Override
public void bind(Context context, SuperViewHolder holder, SimpleBean item, int position) {
//Do Something...
}
};

 DataHolder<SuperBean> holderSuper = new DataHolder<SuperBean>() {
@Override
public void bind(Context context, SuperViewHolder holder, SuperBean item, int position) {
//Do Something...
}
};

 data.add(new LayoutWrapper(R.layout.item_super, 2, new SuperBean(), holderSuper));
 data.add(new LayoutWrapper(R.layout.item_simple, 2, new SimpleBean(), holderSimple));

 adapter.setData(data);
 }
 */
