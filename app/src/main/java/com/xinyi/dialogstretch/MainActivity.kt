package com.xinyi.dialogstretch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.xinyi.dialogstretch.databinding.ActivityMainBinding

/**
 * 主页
 *
 * @author 新一
 * @since 2025/6/27 9:12
 */
class MainActivity : AppCompatActivity() {

    private var mStretchDialog: StretchDialog? = null

    private lateinit var binding: ActivityMainBinding

    private val mButtonOutAnim by lazy { AnimationUtils.loadAnimation(this, R.anim.window_scale_out) }

    private val mButtonInAnim by lazy { AnimationUtils.loadAnimation(this, R.anim.window_scale_in) }

    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.ibOpenDialog.setOnClickListener {
            showDialog()
        }
    }

    /**
     * 显示弹窗
     */
    private fun showDialog() {
        if (mStretchDialog == null) {
            mStretchDialog = StretchDialog(this)

            mStretchDialog?.setOnShowListener {
                mHandler.postDelayed({
                    binding.ibOpenDialog.startAnimation(mButtonOutAnim)
                    binding.ibOpenDialog.visibility = View.GONE
                }, 300)
            }
            mStretchDialog?.setOnDismissListener {
                mHandler.postDelayed({
                    binding.ibOpenDialog.visibility = View.VISIBLE
                    binding.ibOpenDialog.startAnimation(mButtonInAnim)
                }, 250)
            }
        }
        mStretchDialog?.show()
    }
}