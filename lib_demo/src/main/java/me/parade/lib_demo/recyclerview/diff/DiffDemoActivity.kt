package me.parade.lib_demo.recyclerview.diff

import android.os.Bundle
import me.parade.lib_base.base.BaseActivity
import me.parade.lib_base.base.EmptyViewModel
import me.parade.lib_demo.R
import me.parade.lib_demo.databinding.ActivityDiffDemoBinding

class DiffDemoActivity : BaseActivity<ActivityDiffDemoBinding,EmptyViewModel>() {
    override fun getLayoutResId() = R.layout.activity_diff_demo

    override fun initView(savedInstanceState: Bundle?) {

    }

}