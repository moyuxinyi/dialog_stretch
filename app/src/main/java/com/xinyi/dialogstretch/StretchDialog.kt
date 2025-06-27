package com.xinyi.dialogstretch

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
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

    override fun dismiss() {
        super.dismiss()
        mHandler.removeCallbacksAndMessages(null)
        binding.tvParticularsText.text = ""
    }
}