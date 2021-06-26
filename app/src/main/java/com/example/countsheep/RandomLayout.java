package com.example.countsheep;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.ColorInt;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author fanxiaoyang
 * date 2021/6/26
 * desc
 */
public class RandomLayout extends ViewGroup {
    private final Context mContext;

    private int groupWidth, groupHeight;
    private int childWidth, childHeight;
    private int childLeft, childTop, childRight, childBottom;
    private CountListener countListener;

    public void setCountListener(CountListener countListener) {
        this.countListener = countListener;
    }

    public RandomLayout(Context context) {
        this(context, null);
    }

    public RandomLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RandomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setBackgroundColor(0xffffffff);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            measureChildWithMargins(getChildAt(i), widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        groupWidth = w;
        groupHeight = h;
    }

    private List<RandomLayout.ViewAreaIndex> mViewAreaIndices = new ArrayList<>();
    private List<View> mLayoutChildViews = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) {
            return;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            RandomLayout.ViewAreaIndex viewAreaIndex = i >= mViewAreaIndices.size() ? null : mViewAreaIndices.get(i);
            if (viewAreaIndex == null) {
                mViewAreaIndices.add(i, new RandomLayout.ViewAreaIndex(i, getChildAt(i)));
            } else {
                viewAreaIndex.setIndex(i);
                viewAreaIndex.setArea(getChildAt(i));
            }
        }
        //因为集合mViewAreaIndices是没有清空的（复用），当切换数据时，并且数据小于集合大小时，会导致空指针异常，所以要保证长度一致
        if (mViewAreaIndices.size() > childCount) {
            for (int i = mViewAreaIndices.size() - 1; i >= childCount; i--) {
                mViewAreaIndices.remove(i);
            }
        }
        Collections.sort(mViewAreaIndices);
        mLayoutChildViews.clear();
        for (int i = mViewAreaIndices.size() - 1; i >= 0; i--) {
            View childAt = getChildAt(mViewAreaIndices.get(i).mIndex);
            int tryCount = 10;
            while (true) {
                if (tryLayout(childAt)) {
                    mLayoutChildViews.add(childAt);
                    break;
                }

                if (tryCount <= 0) {
                    childAt.layout(-1, -1, -1, -1);
                    break;
                }

                tryCount--;
            }
        }
    }

    private Rect mLayoutRect = new Rect();
    private Rect mTryRect = new Rect();

    private boolean tryLayout(View childView) {
        int leftMargin = ((MarginLayoutParams) childView.getLayoutParams()).leftMargin;
        int topMargin = ((MarginLayoutParams) childView.getLayoutParams()).topMargin;
        int rightMargin = ((MarginLayoutParams) childView.getLayoutParams()).rightMargin;
        int bottomMargin = ((MarginLayoutParams) childView.getLayoutParams()).bottomMargin;
        childWidth = childView.getMeasuredWidth() + leftMargin + rightMargin;
        childHeight = childView.getMeasuredHeight() + topMargin + bottomMargin;
        childLeft = mRandom.nextInt(groupWidth - childWidth + 1 <= 0 ? 1 : groupWidth - childWidth + 1);
        childTop = mRandom.nextInt(groupHeight - childHeight + 1 <= 0 ? 1 : groupHeight - childHeight + 1);
        childRight = childLeft + childWidth;
        childBottom = childTop + childHeight;
        mTryRect.set(childLeft, childTop, childRight, childBottom);
        for (View v : mLayoutChildViews) {
            int leftMarginL = ((MarginLayoutParams) v.getLayoutParams()).leftMargin;
            int topMarginL = ((MarginLayoutParams) v.getLayoutParams()).topMargin;
            int rightMarginL = ((MarginLayoutParams) v.getLayoutParams()).rightMargin;
            int bottomMarginL = ((MarginLayoutParams) v.getLayoutParams()).bottomMargin;
            mLayoutRect.set(v.getLeft() - leftMarginL, v.getTop() - topMarginL, v.getRight() + rightMarginL, v.getBottom() + bottomMarginL);
            if (mTryRect.intersect(mLayoutRect)) {
                return false;
            }
        }
        childView.layout(childLeft + leftMargin, childTop + topMargin, childRight - rightMargin, childBottom - bottomMargin);
        return true;
    }

    private static class ViewAreaIndex implements Comparable<RandomLayout.ViewAreaIndex> {
        int mIndex;
        int mArea;

        public ViewAreaIndex(int index, View view) {
            mIndex = index;
            mArea = view.getMeasuredWidth() * view.getMeasuredHeight();
        }

        public void setIndex(int index) {
            mIndex = index;
        }

        public void setArea(View view) {
            mArea = view.getMeasuredWidth() * view.getMeasuredHeight();
        }

        @Override
        public int compareTo(RandomLayout.ViewAreaIndex o) {
            return this.mArea - o.mArea;
        }
    }

    public void initView(int size) {
        removeAllViews();
        for (int i = 0; i < size; i++) {
            ImageView imageView = getImageView();
            addView(imageView);
        }
    }

    private ImageView getImageView() {
        ImageView imageView = new ImageView(mContext);
        int size = getRandomWidth();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
        imageView.setImageResource(RandomSheepSrc.Companion.getSheep());
        imageView.setLayoutParams(params);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.RotateOut).duration(1000).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        removeView(imageView);
                    }
                }).playOn(imageView);
                if (countListener != null) {
                    countListener.onCount();
                }
            }
        });
        return imageView;
    }


    private Random mRandom = new Random();

    @ColorInt
    private int getBgColor() {
        return Color.argb(0xff, mRandom.nextInt(0x100), mRandom.nextInt(0x100), mRandom.nextInt(0x100));
    }

    private int getRandomWidth() {
        return DensityUtil.dip2px(mContext, kotlin.random.Random.Default.nextInt(10, 50));
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new RandomLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new RandomLayout.LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new RandomLayout.LayoutParams(getContext(), attrs);
    }


    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    public interface CountListener {
        void onCount();
    }

}
