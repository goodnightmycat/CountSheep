package com.example.countsheep;

import android.content.Context;

/**
 * @author fanxiaoyang
 * date 2021/6/26
 * desc
 */
public class DensityUtil {

    public static int dip2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
