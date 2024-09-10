package me.parade.wanandroid.ui.home.child.explore

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import com.google.android.material.carousel.CarouselLayoutManager
import kotlinx.coroutines.launch
import me.parade.lib_base.base.BaseFragment
import me.parade.lib_base.ext.logd
import me.parade.wanandroid.databinding.FragmentExploreBinding
import me.parade.wanandroid.net.model.Banner


class ExploreFragment : BaseFragment<FragmentExploreBinding,ExploreVM>() {

//    private val bannerAdapter by lazy { ImageAdapter(emptyList<Banner>()) }

    companion object{
        fun newInstance(): ExploreFragment = with(ExploreFragment()){
            arguments = bundleOf()
            this
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        requestBanners()

//        with(binding.recycler){
//            adapter = bannerAdapter
//            layoutManager = CarouselLayoutManager()
//        }
    }

    override fun initData() {
        lifecycleScope.launch {
            launch {
                viewModel.banners.collect{result->
                    withResumed {
                        if (result.isNotEmpty()){
                            binding.recycler.layoutManager = CarouselLayoutManager()
                            binding.recycler.adapter = ImageAdapter(result)
                            "${result.size}".logd()
                        }
                    }
                }
            }
        }
    }

    private fun requestBanners(){
      viewModel.getBannerData()
    }

    override fun lazyLoad(tag: String) {
        super.lazyLoad(tag)

    }

}