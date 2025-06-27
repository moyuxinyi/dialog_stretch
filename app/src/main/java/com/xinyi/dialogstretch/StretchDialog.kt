package com.xinyi.dialogstretch

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.graphics.drawable.toDrawable
import com.xinyi.dialogstretch.databinding.DialogStretchBinding

/**
 * 带有拉伸动画的Dialog
 *
 * @author 新一
 * @since 2025/6/27 9:23
 */
class StretchDialog(context: Context) : Dialog(context, R.style.BaseDialogTheme) {

    private var binding: DialogStretchBinding

    private val mHandler = Handler(Looper.getMainLooper())

    init {
        window?.attributes?.gravity = Gravity.BOTTOM
        // 去除窗口透明部分显示的黑色
        window?.setBackgroundDrawable(0.toDrawable())
        // 取消遮罩
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        // 设置不可取消
        setCancelable(false)

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setWindowAnimations(R.style.LowRightScaleAnimStyle)

        // 绑定布局
        binding = DialogStretchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    override fun show() {
        super.show()
        binding.llUiContainer.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        initParams()
    }

    private fun initParams() {
        initParticularsText()
        var currentCount = 0

        // 拼接文案
        val appendRunnable = object : Runnable {
            override fun run() {
                if (currentCount < 4) {
                    currentCount++

                    if (currentCount == 4) {
                        initParticularsText()
                        // 收缩布局
                        binding.llUiContainer.collapseTo(ViewGroup.LayoutParams.WRAP_CONTENT)
                    } else {
                        // 拼接文案
                        binding.tvParticularsText.append(buildString {
                            repeat(currentCount) {
                                append("\n\n${PhilosophyQuotes.getRandomQuote()}")
                            }
                        })
                        // 拉伸布局
                        binding.llUiContainer.expandToWrapContent()
                    }
                    mHandler.postDelayed(this, 1000)
                }
            }
        }
        mHandler.postDelayed(appendRunnable, 1000)
    }

    private fun initParticularsText() {
        binding.tvParticularsText.text = "人类试图掌控时间，结果只是被钟表牵着走。"
    }

    private fun initListeners() {
        binding.tvParticularsOk.setOnClickListener {
            dismiss()
        }
    }

    /**
     * 通过属性动画平滑地改变指定 View 的高度，实现拉伸或收缩动画效果
     *
     * @param view 需要调整高度的目标 View，一般是容器布局
     * @param targetHeight 目标高度，单位 px，动画将从当前高度变到此高度
     * @param duration 动画时长，单位毫秒，默认300ms
     */
    private fun animateHeightChange(view: View, targetHeight: Int, duration: Long = 300L) {
        // 获取当前View的高度，作为动画起始高度
        val startHeight = view.height

        // 创建一个整数属性动画，从起始高度到目标高度
        val animator = ValueAnimator.ofInt(startHeight, targetHeight).apply {
            this.duration = duration
            // 每帧动画更新时，获取当前动画值，动态设置View高度
            addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                val layoutParams = view.layoutParams
                layoutParams.height = animatedValue
                view.layoutParams = layoutParams
            }
        }

        // 启动动画
        animator.start()
    }

    override fun dismiss() {
        super.dismiss()
        mHandler.removeCallbacksAndMessages(null)
        binding.tvParticularsText.text = ""
    }
}