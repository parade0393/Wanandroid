package me.parade.wanandroid.ui.home

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import me.parade.lib_base.base.BaseFragment
import me.parade.lib_common.ext.logd
import me.parade.lib_image.ImageLoaderManager
import me.parade.wanandroid.R
import me.parade.wanandroid.databinding.FragmentHomeBinding
import me.parade.wanandroid.net.model.Banner

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(),OnRefreshListener {

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
        binding.refreshLayout.apply {
            autoRefresh()
            setEnableRefresh(true)
            setEnableLoadMore(false)
            setOnRefreshListener(this@HomeFragment)
        }
        initTabAndVp()
        initBanner()
    }

    private fun initBanner() {
        binding.banner.setAdapter(bannerAdapter)
            .addBannerLifecycleObserver(this@HomeFragment)
            .setIndicator(CircleIndicator(requireContext()))
    }

    private fun initTabAndVp() {
        val items = listOf(
            getString(R.string.title_home),
            getString(R.string.tab_home_square),
        )
        childAdapter = HomeChildFragmentAdapter(items, childFragmentManager, lifecycle)
        binding.apply {
            with(homeVp2) {
                adapter = childAdapter
            }
            TabLayoutMediator(homeTab, homeVp2) { tab: TabLayout.Tab, position: Int ->
                tab.text = items[position]
            }.also { it.attach() }
        }
    }

    override fun initData() {
//        viewModel.getBannerData()
    }

    override fun initObserver() {
        viewModel.banners.observe(this) {
            binding.banner.setDatas(it)
            binding.refreshLayout.finishRefresh()
        }
    }

    override fun lazyLoad(tag: String) {
        updateStatusBarAppearance(true)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }


    private fun refresh(){
        viewModel.getBannerData()
    }
}