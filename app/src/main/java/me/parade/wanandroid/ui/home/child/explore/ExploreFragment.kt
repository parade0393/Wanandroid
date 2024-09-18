package me.parade.wanandroid.ui.home.child.explore

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.dslItem
import com.angcyo.dsladapter.findItemByTag
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import kotlinx.coroutines.launch
import me.parade.lib_base.base.BaseFragment
import me.parade.lib_base.ext.logd
import me.parade.lib_image.ImageLoaderManager
import me.parade.wanandroid.databinding.FragmentExploreBinding
import me.parade.wanandroid.dsl.DslBannerItem
import me.parade.wanandroid.net.model.Banner


class ExploreFragment : BaseFragment<FragmentExploreBinding, ExploreVM>() {


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

    private val dslAdapter: DslAdapter by lazy { DslAdapter() }

    companion object {
        fun newInstance(): ExploreFragment = with(ExploreFragment()) {
            arguments = bundleOf()
            this
        }

        val TAG_UPDATE_BANNER = "tag_update_banner"
    }


    override fun initView(savedInstanceState: Bundle?) {
        requestBanners()
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dslAdapter.apply {
                render {
                    dslItem(DslBannerItem(bannerAdapter,this@ExploreFragment)).apply {
                        itemTag = TAG_UPDATE_BANNER
                    }
                }
            }
        }
    }

    override fun initData() {
        lifecycleScope.launch {
            launch {
                viewModel.banners.collect { result ->
                    dslAdapter.findItemByTag(TAG_UPDATE_BANNER).apply {
                        if (this is DslBannerItem) {
                            bannerList = result
                            updateAdapterItem(TAG_UPDATE_BANNER)
                        }
                    }
                }
            }
        }
    }

    private fun requestBanners() {
        viewModel.getBannerData()
    }


}