package me.parade.wanandroid.dsl

import androidx.lifecycle.LifecycleOwner
import com.angcyo.dsladapter.DslAdapterItem
import com.angcyo.dsladapter.DslViewHolder
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.indicator.CircleIndicator
import me.parade.wanandroid.R
import me.parade.wanandroid.net.model.Banner

class DslBannerItem(private  val bannerAdapter:BannerImageAdapter<Banner>,private val lifecycleOwner: LifecycleOwner):DslAdapterItem() {



   var bannerList:List<Banner> = emptyList()

    init {
        itemLayoutId = R.layout.dsl_banner_item
    }

    override fun onItemBind(
        itemHolder: DslViewHolder,
        itemPosition: Int,
        adapterItem: DslAdapterItem,
        payloads: List<Any>
    ) {
        super.onItemBind(itemHolder, itemPosition, adapterItem, payloads)
        itemHolder.v<com.youth.banner.Banner<Banner,BannerImageAdapter<Banner>>>(R.id.banner)?.let {
            if (bannerAdapter != it.adapter){
                it.setAdapter(bannerAdapter).addBannerLifecycleObserver(lifecycleOwner)
                    .setIndicator(CircleIndicator(it.context))
            }
            it.setDatas(bannerList)
        }
    }
}