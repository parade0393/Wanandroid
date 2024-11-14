package me.parade.lib_demo.toast

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import me.parade.lib_common.toast.ToastManager
import me.parade.lib_common.toast.ToastType
import me.parade.lib_demo.R
import me.parade.lib_demo.databinding.ActivityToastDemoBinding

class ToastDemoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityToastDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToastDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn1.setOnClickListener {
            ToastManager.show(supportFragmentManager,ToastType.NORMAL,"您的信息已提交成功")
        }
        binding.btn2.setOnClickListener {
            ToastManager.show(supportFragmentManager,ToastType.SUCCESS,"登录成功")
        }
        binding.btn3.setOnClickListener {
            ToastManager.show(supportFragmentManager,ToastType.WARNING,"请填写正确手机号")
        }
        binding.btn4.setOnClickListener {
            ToastManager.show(supportFragmentManager,ToastType.ERROR,"服务器异常")
        }
        binding.btn5.setOnClickListener {
            ToastManager.show(supportFragmentManager,ToastType.LOADING,"提交中")
        }
    }
}