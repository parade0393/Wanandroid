package me.parade.wanandroid.ui.home.child.explore

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import me.parade.lib_base.base.BaseFragment
import me.parade.wanandroid.databinding.FragmentExploreBinding


class ExploreFragment : BaseFragment<FragmentExploreBinding,ExploreVM>() {

    companion object{
        fun newInstance(): ExploreFragment = with(ExploreFragment()){
            arguments = bundleOf()
            this
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        requestBanners()

    }

    override fun initData() {
        lifecycleScope.launch {
            launch {
                viewModel.banners.collect{result->

                }
            }
        }
    }

    private fun requestBanners(){
      viewModel.getBannerData()
    }


}