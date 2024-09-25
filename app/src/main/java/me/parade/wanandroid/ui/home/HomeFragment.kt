package me.parade.wanandroid.ui.home

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.launch
import me.parade.lib_base.base.BaseFragment
import me.parade.lib_common.ext.logd
import me.parade.lib_image.ImageLoaderManager
import me.parade.wanandroid.R
import me.parade.wanandroid.databinding.FragmentHomeBinding
import me.parade.wanandroid.net.model.Banner

class HomeFragment : BaseFragment<FragmentHomeBinding,HomeViewModel>() {

    private val bannerAdapter: BannerImageAdapter<Banner> = object : BannerImageAdapter<Banner>(
        emptyList()
    ) {
        override fun onBindView(
            holder: BannerImageHolder?,
            data: Banner?,
            position: Int,
            size: Int
        ) {
            holder?.imageView?.let { image ->
                data?.let { item ->
                    ImageLoaderManager.loadImage(item.imagePath, image)
                }

            }
        }

    }.apply {
        setOnBannerListener { data, position ->
            data.url.logd()
        }
    }


    private lateinit var childAdapter: HomeChildFragmentAdapter

    override fun initView(savedInstanceState: Bundle?) {
//        ViewCompat.setOnApplyWindowInsetsListener(binding.homeTab){v,insets->
//            val stateBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
//            v.setPadding(stateBars.left,stateBars.top,stateBars.right,stateBars.bottom)
//            insets
//        }
        val items = listOf(
            getString(R.string.title_home),
            getString(R.string.tab_home_square),
        )
        childAdapter = HomeChildFragmentAdapter(items,childFragmentManager,lifecycle)
        binding.apply {
            with(homeVp2){
                adapter = childAdapter
            }
            TabLayoutMediator(homeTab,homeVp2){ tab: TabLayout.Tab, position: Int ->
                tab.text = items[position]
            }.also { it.attach() }

            banner.setAdapter(bannerAdapter)
                .addBannerLifecycleObserver(this@HomeFragment)
                .setIndicator(CircleIndicator(requireContext()))
        }
    }

    override fun initData() {
        requestBanners()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.banners.collect{
                    //重新回到前台后会重新collect,屏幕旋转后会重新网络请求，重新collect
                    binding.banner.setDatas(it)
                }
            }
        }

    }

    private fun requestBanners() {
        viewModel.getBannerData()
    }

    override fun lazyLoad(tag: String) {
        updateStatusBarAppearance(true)
    }
}