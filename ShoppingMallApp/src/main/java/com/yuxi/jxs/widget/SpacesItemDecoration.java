package com.yuxi.jxs.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    public static final int NOT_TOP = 0;
    public static final int NOT_BOOTOM = 1;
    public static final int GRIDLE_LAYOUT = 2;
    public static final int LINEAR_LAYOUT = 3;
    public static final int HORIZONTAL = 4;
    private int type;

    public SpacesItemDecoration(int space, int type) {
        this.space = space;
        this.type = type;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        switch (type) {
            case NOT_TOP:
                if (parent.getChildPosition(view) == 0) {
                    outRect.top = space;
                }
                outRect.right = space;
                outRect.bottom = space;
                outRect.left = space;
                break;
            case NOT_BOOTOM:
                outRect.right = space;
                outRect.top = space;
                outRect.left = space;
                break;
            case GRIDLE_LAYOUT:
                if (parent.getChildPosition(view) == 0 || parent.getChildPosition(view) == 1) {
                    outRect.top = space;
                }
                outRect.bottom = space;
                if (parent.getChildPosition(view) % 2 == 0) {
                    outRect.left = space;
                    outRect.right = space;
                } else {
                    outRect.right = space;
                }
                break;
            case LINEAR_LAYOUT:
                outRect.bottom = space;
                break;
            case HORIZONTAL:

                break;
        }


//        outRect.top = space;
        // Add top margin only for the first item to avoid double space between items


    }
}