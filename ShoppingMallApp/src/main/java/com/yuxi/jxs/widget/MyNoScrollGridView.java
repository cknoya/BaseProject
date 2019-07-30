package com.yuxi.jxs.widget;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.widget.GridView;

public class MyNoScrollGridView extends GridView  
{
    public boolean isOnMeasure;

    private OnTouchInvalidPositionListener onTouchInvalidPositionListener;

    public MyNoScrollGridView(android.content.Context context,
                              android.util.AttributeSet attrs)
    {  
        super(context, attrs);  
    }  
  
    
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
    {
        isOnMeasure = true;
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);  
  
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        isOnMeasure = false;
        super.onLayout(changed, l, t, r, b);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //先创建一个监听接口，一旦点击了无效区域，便实现onTouchInvalidPosition方法，返回true or false来确认是否消费了这个事件
        if(onTouchInvalidPositionListener!=null){
            if(!isEnabled()){
                return isClickable()||isLongClickable();
            }
            int motionPosition = pointToPosition((int)ev.getX(), (int)ev.getY());
            if(ev.getAction()== MotionEvent.ACTION_UP&&motionPosition == INVALID_POSITION){
                super.onTouchEvent(ev);
                return onTouchInvalidPositionListener.onTouchInvalidPosition(motionPosition);
            }
        }
        return super.onTouchEvent(ev);
    }

    public void setOnTouchInvalidPositionListener(
            OnTouchInvalidPositionListener onTouchInvalidPositionListener) {
        this.onTouchInvalidPositionListener = onTouchInvalidPositionListener;
    }

    public interface OnTouchInvalidPositionListener{
        public boolean onTouchInvalidPosition(int motionEvent);
    }

} 
