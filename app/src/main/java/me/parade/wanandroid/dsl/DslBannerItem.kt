package me.parade.wanandroid.dsl

import com.angcyo.dsladapter.DslAdapterItem
import com.angcyo.dsladapter.DslViewHolder

class DslBannerItem:DslAdapterItem() {



    init {

    }

    override fun onItemBind(
        itemHolder: DslViewHolder,
        itemPosition: Int,
        adapterItem: DslAdapterItem,
        payloads: List<Any>
    ) {
        super.onItemBind(itemHolder, itemPosition, adapterItem, payloads)
    }
}