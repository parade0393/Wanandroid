package me.parade.lib_demo.recyclerview.diff

import android.os.Bundle
import me.parade.lib_base.base.BaseActivity
import me.parade.lib_base.base.EmptyViewModel
import me.parade.lib_demo.R
import me.parade.lib_demo.databinding.ActivityDiffThirdBinding

class DiffThirdActivity : BaseActivity<ActivityDiffThirdBinding,EmptyViewModel>() {
    override fun getLayoutResId() = R.layout.activity_diff_third

    override fun initView(savedInstanceState: Bundle?) {
        binding.button2.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

}