package com.salaheddin.store.helpers;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ListSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int spanCount;

    public ListSpacingItemDecoration(int space, int spanCount) {
        this.space = space;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildPosition(view);

        outRect.left = space;
        outRect.bottom = space;

        if(position == 0) {
            outRect.top = space;
        }
    }
}