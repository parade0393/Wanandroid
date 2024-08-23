package me.parade.wanandroid.ui.home.child.explore

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import me.parade.lib_base.base.BaseFragment
import me.parade.lib_base.ext.logd
import me.parade.wanandroid.databinding.FragmentExploreBinding


class ExploreFragment : BaseFragment<FragmentExploreBinding,ExploreVM>() {

    companion object{
        fun newInstance(): ExploreFragment = with(ExploreFragment()){
            arguments = bundleOf()
            this
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.data.collect{
                    logd(it)
                }
            }
        }
        binding.btnQuest.setOnClickListener {
            viewModel.getData()
        }
    }

    override fun lazyLoad(tag: String) {
        super.lazyLoad(tag)
        viewModel.getData()
    }

}