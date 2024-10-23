package me.parade.lib_dslitem

import android.view.View
import androidx.annotation.DrawableRes
import com.angcyo.dsladapter.DslAdapterItem
import com.angcyo.dsladapter.DslViewHolder
import me.parade.lib_dslitem.databinding.DslitemSettingItemBinding

class DslSettingItem : DslAdapterItem() {

    private var itemBinding: DslitemSettingItemBinding? = null

    init {
        itemLayoutId = R.layout.dslitem_setting_item
    }

    var settingData: SettingItem? = null

    override fun onItemBind(
        itemHolder: DslViewHolder,
        itemPosition: Int,
        adapterItem: DslAdapterItem,
        payloads: List<Any>
    ) {
        super.onItemBind(itemHolder, itemPosition, adapterItem, payloads)
        itemBinding = DslitemSettingItemBinding.bind(itemHolder.itemView)

        itemBinding?.apply {
            settingData?.let { item ->
               mainTitle.apply {
                   text = item.mainTitle
                   visibility = if (item.mainTitle.isNullOrBlank()) View.GONE else View.VISIBLE
               }
                subTitle.apply {
                    text = item.subTitle
                    visibility = if (item.subTitle.isNullOrBlank()) View.GONE else View.VISIBLE
                }
                ivIcon.run {
                    visibility = if (item.leftDrawable != null){
                        setImageResource(item.leftDrawable)
                        View.VISIBLE
                    }else{
                        View.GONE
                    }
                }

            }
        }
    }
}

data class SettingItem(val mainTitle: String?, val subTitle: String?,@DrawableRes val leftDrawable:Int?)