package me.parade.lib_demo.recyclerview.diff

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import me.parade.lib_base.base.BaseActivity
import me.parade.lib_base.base.EmptyViewModel
import me.parade.lib_demo.R
import me.parade.lib_demo.databinding.ActivityDiffSecondBinding

class DiffSecondActivity : BaseActivity<ActivityDiffSecondBinding,EmptyViewModel>() {
    override fun getLayoutResId() = R.layout.activity_diff_second

    override fun initView(savedInstanceState: Bundle?) {
        binding.button.setOnClickListener {
            Intent().apply {
                putExtra("newCount",(1..100).random())
            }.also {
                setResult(Activity.RESULT_OK,it)
                finish()
            }
        }
    }


}