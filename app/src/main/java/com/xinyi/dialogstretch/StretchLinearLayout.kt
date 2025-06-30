package com.xinyi.dialogstretch

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
import androidx.core.animation.doOnEnd

/**
 * 支持拉伸动画的 LinearLayout
 *
 * 用法：
 *   // 拉伸到内容实际高度
 *   stretchLayout.expandToWrapContent()
 *
 * @author 新一
 * @since 2025/6/27 10:02
 */
class StretchLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    /**
     * 是否正在执行动画
     */
    private var isAnimating = false

    /**
     * 当前正在执行的高度动画实例
     */
    private var mAnimator: ValueAnimator? = null

    /**
     * 拉伸到 wrap_content 的高度（内容实际高度）
     */
    fun expandToWrapContent(duration: Long = 250L) {
        if (isAnimating) {
            return
        }
        isAnimating = true

        // 先把当前高度固定下来，防止系统自动 layout 改变高度
        layoutParams = layoutParams.also { lp ->
            lp.height = this@StretchLinearLayout.height
        }

        post {
            val targetHeight = measureWrapContentHeight()
            animateHeightChange(height, targetHeight, duration)
        }
    }

    /**
     * 执行动画从 fromHeight 到 toHeight
     */
    private fun animateHeightChange(fromHeight: Int, toHeight: Int, duration: Long) {
        if (fromHeight == toHeight) {
            return
        }

        mAnimator?.cancel()
        mAnimator = ValueAnimator.ofInt(fromHeight, toHeight).apply {
            this.duration = duration

            // 加速插值器：动画开始慢，然后加速
            interpolator = AccelerateInterpolator()
            addUpdateListener { animator ->
                // 更新高度
                val value = animator.animatedValue as Int
                layoutParams = layoutParams.apply {
                    height = value
                }
            }
            doOnEnd {
                isAnimating = false
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