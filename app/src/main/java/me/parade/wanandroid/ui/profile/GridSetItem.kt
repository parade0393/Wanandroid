package me.parade.wanandroid.ui.profile

import com.angcyo.dsladapter.DslAdapterItem
import com.angcyo.dsladapter.DslViewHolder
import me.parade.wanandroid.R

class GridSetItem:DslAdapterItem() {


    init {
        itemLayoutId = R.layout.dsl_grid_item
    }

    /**显示的文本*/
    var itemText: CharSequence? = null

    /**图标*/
    var itemIcon: Int = -1

    override fun onItemBind(
        itemHolder: DslViewHolder,
        itemPosition: Int,
        adapterItem: DslAdapterItem,
        payloads: List<Any>
    ) {
        super.onItemBind(itemHolder, itemPosition, adapterItem, payloads)
        itemHolder.tv(R.id.tvTitle)?.apply {
            text = itemText
        }

        itemHolder.img(R.id.ivIcon)?.apply {
            setImageResource(itemIcon)
        }
    }
}