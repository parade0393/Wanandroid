package me.parade.lib_demo.dialog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.parade.lib_common.dialog.AlertDialogFragment
import me.parade.lib_common.dialog.DialogAnimation
import me.parade.lib_common.dialog.InputDialogFragment
import me.parade.lib_common.dialog.SelectDialogFragment
import me.parade.lib_demo.R
import me.parade.lib_demo.databinding.ActivityDialogDemoBinding

class DialogDemoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDialogDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn1.setOnClickListener {
            AlertDialogFragment.Builder()
                .setTitle("友情提示")
                .setContent("确认删除吗?")
                .build()
                .show(supportFragmentManager,"btn1")
        }

        binding.btn2.setOnClickListener {
            AlertDialogFragment.Builder()
                .setTitle("友情提示")
                .setContent("确认删除吗?")
                .setAnimation(DialogAnimation.CENTER_UP)
                .build()
                .show(supportFragmentManager,"btn2")
        }

        binding.btn3.setOnClickListener {
            AlertDialogFragment.Builder()
                .setTitle("友情提示")
                .setContent("确认删除吗?")
                .setAnimation(DialogAnimation.CENTER_DOWN)
                .build()
                .show(supportFragmentManager,"btn3")
        }

        binding.btn4.setOnClickListener {
            AlertDialogFragment.Builder()
                .setTitle("友情提示")
                .setContent("确认删除吗?")
                .setAnimation(DialogAnimation.TOP)
                .build()
                .show(supportFragmentManager,"btn4")
        }

        binding.btn5.setOnClickListener {
            AlertDialogFragment.Builder()
                .setTitle("友情提示")
                .setContent("确认删除吗?")
                .setAnimation(DialogAnimation.BOTTOM)
                .build()
                .show(supportFragmentManager,"btn5")
        }
        binding.btn6.setOnClickListener {
            InputDialogFragment.Builder()
                .setTitle("提示")
                .setHintText("请输入您的姓名···")
                .build()
                .show(supportFragmentManager,"btn6")
        }
        binding.btn7.setOnClickListener {
            SelectDialogFragment.Builder()
                .setItems(mutableListOf("篮球","足球","乒乓球"))
                .build()
                .show(supportFragmentManager,"btn7")
        }
    }
}