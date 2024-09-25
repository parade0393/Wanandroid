package me.parade.lib_demo.flow

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import me.parade.lib_base.base.BaseActivity
import me.parade.lib_common.ext.logd
import me.parade.lib_demo.R
import me.parade.lib_demo.databinding.ActivityFlowDemoBinding

class FlowDemoActivity : BaseActivity<ActivityFlowDemoBinding, FlowDemoModel>() {
    override fun getLayoutResId() = R.layout.activity_flow_demo

    override fun initView(savedInstanceState: Bundle?) {
        binding.btnCollect.setOnClickListener {
            viewModel.startTimer()
        }

//        viewModel.stateFlow.observe(this) {
//            //退到后台后flow会产生新数据，但是重新回到前台后这里收不到
//            binding.tvTime.text = it.toString()
//            "update：$it in UI".logd()
//
//        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.stateFlow.collect{
                    binding.tvTime.text = it.toString()
                    "update：$it in UI".logd()
                }
            }
        }

    }

}