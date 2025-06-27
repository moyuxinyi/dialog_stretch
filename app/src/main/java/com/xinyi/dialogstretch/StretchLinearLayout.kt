package com.xinyi.dialogstretch

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * 支持拉伸动画的 LinearLayout
 *
 * 用法：
 *   stretchLayout.expandToWrapContent()
 *   stretchLayout.collapseTo(初始高度)
 *
 * @author 杨耿雷
 * @since 2025/6/27 10:02
 */
class StretchLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private var animator: ValueAnimator? = null

    /**
     * 拉伸到 wrap_content 的高度（内容实际高度）
     */
    fun expandToWrapContent(duration: Long = 300L) {
        post {
            val targetHeight = measureWrapContentHeight()
            animateHeightChange(height, targetHeight, duration)
        }
    }

    /**
     * 收缩到指定高度（通常是初始高度）
     */
    fun collapseTo(targetHeight: Int, duration: Long = 300L) {
        animateHeightChange(height, targetHeight, duration)
    }

    /**
     * 执行动画从 fromHeight 到 toHeight
     */
    private fun animateHeightChange(fromHeight: Int, toHeight: Int, duration: Long) {
        if (fromHeight == toHeight) return

        animator?.cancel()
        animator = ValueAnimator.ofInt(fromHeight, toHeight).apply {
            this.duration = duration
            addUpdateListener { anim ->
                val value = anim.animatedValue as Int
                layoutParams.height = value
                requestLayout()
            }
            start()
        }
    }

    /**
     * 测量自身在 wrap_content 下的高度
     */
    private fun measureWrapContentHeight(): Int {
        val widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        measure(widthSpec, heightSpec)
        return measuredHeight
    }
}