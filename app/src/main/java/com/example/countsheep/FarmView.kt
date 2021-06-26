package com.example.countsheep

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView

/**
 * @author fanxiaoyang
 * date 2021/6/26
 * desc
 */
class FarmView : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        val sheepIv = ImageView(context)
        sheepIv.layoutParams.width = DensityUtil.dip2px(context, 100)
        sheepIv.layoutParams.height = DensityUtil.dip2px(context, 50)
        sheepIv.setBackgroundColor(Color.parseColor("#5f5f5f"))
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        repeat(10) {

        }
    }
}