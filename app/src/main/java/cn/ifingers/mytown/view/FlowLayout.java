package cn.ifingers.mytown.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by syfing on 2016/5/20.
 */
public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }

        int mode = MeasureSpec.getMode(heightMeasureSpec);
        LayoutParams lp = new LayoutParams(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = lp.width;
        if(mode == MeasureSpec.AT_MOST){
            int totalHeight = 0;
            int totalWidth = 0;
            int maxHeight = 0;
            for(int i = 0; i < childCount; i++){
                View view = getChildAt(i);
                int width = view.getMeasuredWidth();
                int height = view.getMeasuredHeight();

                MarginLayoutParams lp1 = (MarginLayoutParams) view.getLayoutParams();
                int ml = lp1.leftMargin;
                int mt = lp1.topMargin;
                int mr = lp1.rightMargin;
                int mb = lp1.bottomMargin;

                if(totalWidth + width + ml + mr > getMeasuredWidth()){
                    totalWidth = 0;
                    totalHeight += maxHeight;
                    maxHeight = 0;
                }

                maxHeight = Math.max(maxHeight, height + mt + mb);

                totalWidth += ml + width + mr;
            }
            setMeasuredDimension(parentWidth, totalHeight + maxHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int totalHeight = 0;
        int totalWidth = 0;
        int maxHeight = 0;
        for(int i = 0; i < childCount; i++){
            View view = getChildAt(i);
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();

            MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
            int ml = lp.leftMargin;
            int mt = lp.topMargin;
            int mr = lp.rightMargin;
            int mb = lp.bottomMargin;

            if(totalWidth + width + ml + mr > getMeasuredWidth()){
                totalWidth = 0;
                totalHeight += maxHeight;
            }

            int left = totalWidth + ml;
            int top = totalHeight + mt;
            int right = totalWidth + width + ml;
            int bottom = totalHeight + height + mt;

            maxHeight = Math.max(maxHeight, height + mt + mb);

            view.layout(left, top, right, bottom);
            totalWidth += ml + width + mr;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}