package me.parade.lib_demo

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import me.parade.lib_base.base.BaseActivity
import me.parade.lib_base.base.EmptyViewModel
import me.parade.lib_demo.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding,EmptyViewModel>() {
    override fun getLayoutResId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        binding.rcy.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = DemoAdapter(DataServer.getDemoData())
        }
    }

}