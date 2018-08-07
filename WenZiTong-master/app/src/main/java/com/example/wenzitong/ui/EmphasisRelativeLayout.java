package com.example.wenzitong.ui;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.wenzitong.untils.DpPxUtil;


/**
 * Created by Ellly on 2017/8/26.
 * 设置界面精简的自定义RelativeLayout
 * 点击的时候自带的下划线会变蓝
 */

public class EmphasisRelativeLayout extends RelativeLayout implements View.OnFocusChangeListener{

    private View mBottomLine;
    private View mBottomEmphasisLine;

    public EmphasisRelativeLayout(Context context) {
        super(context);
        init();
    }

    public EmphasisRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmphasisRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                requestFocus();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void init() {

        setFocusable(true);
        setFocusableInTouchMode(true);

        mBottomLine = new View(getContext());
        mBottomEmphasisLine = new View(getContext());
        mBottomLine.setBackgroundColor(Color.parseColor("#a197a1"));
        mBottomEmphasisLine.setBackgroundColor(Color.parseColor("#47bafe"));
        mBottomEmphasisLine.setVisibility(View.INVISIBLE);
        LayoutParams mParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpPxUtil.dip2px(getContext(), 1));
        mParams.addRule(ALIGN_PARENT_BOTTOM);
        addView(mBottomLine, mParams);
        addView(mBottomEmphasisLine, mParams);
        setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d("THIS", "FocusChanged");
        if (hasFocus){
            mBottomEmphasisLine.setVisibility(View.VISIBLE);
        }else {
            mBottomEmphasisLine.setVisibility(View.INVISIBLE);
        }
    }
}
