package com.example.wenzitong.ui.SuperRecyclerView;

import android.content.Context;

/**
 * @param <T> DataBean
 */
public interface DataHolder<T> {
    /**
     *  将（对应Item的）数据和（对应Item的）ViewHolder进行绑定，并设置监听。
     * 【eg：TextView tvName = holder.getView(R.id.tv_name);】
     * @param context Content
     * @param holder ViewHolder
     * @param item Item
     * @param position Position
     */
    void bind(Context context, SuperViewHolder holder, T item, int position);
}

