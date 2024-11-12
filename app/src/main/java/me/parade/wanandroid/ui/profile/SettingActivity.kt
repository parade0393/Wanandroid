package me.parade.wanandroid.ui.profile

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.DslItemDecoration
import com.angcyo.dsladapter.dslItem
import com.angcyo.dsladapter.isFirstPosition
import com.angcyo.dsladapter.isLastPosition
import com.angcyo.dsladapter.isOnlyOne
import me.parade.lib_base.base.BaseActivity
import me.parade.lib_common.ext.px
import me.parade.lib_dslitem.DslSettingItem
import me.parade.lib_dslitem.SettingItem
import me.parade.wanandroid.R
import me.parade.wanandroid.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding,SettingVm>() {
    override fun getLayoutResId() = R.layout.activity_setting

    private val dslAdapter by lazy { DslAdapter() }

    override fun initView(savedInstanceState: Bundle?) {

        binding.root.findViewById<Toolbar>(me.parade.lib_common.R.id.compatToolBar)?.apply {
            findViewById<TextView>(R.id.toolbarTitle)?.apply {
                text = getString(R.string.settings)
            }
        }

        binding.main.apply {
            layoutManager = LinearLayoutManager(this@SettingActivity)
            addItemDecoration(DslItemDecoration())
            adapter = dslAdapter
        }

        val list = listOf(
            SettingItem(mainTitle = "深色模式", tag = "UI"),
            SettingItem(mainTitle = "字体大小", tag = "UI"),
            SettingItem(mainTitle = "个人信息", tag = "person"),
            SettingItem(mainTitle = "账户与安全", tag = "person"),
            SettingItem(mainTitle = "草稿箱", tag = "person"),
            SettingItem(mainTitle = "隐私政策", tag = "privacy"),
            SettingItem(mainTitle = "当前版本", tag = "privacy"),
            SettingItem(mainTitle = "清除缓存", tag = "other")
        )

        dslAdapter.render {
            for (i in list.indices) {
                dslItem(DslSettingItem()){
                    settingData = list[i]
                    itemGroups = listOf(list[i].tag)
                    itemBindOverride =  {itemHolder, itemPosition, adapterItem, _ ->
                        itemGroupParams.apply {
                            if (isOnlyOne()){
                                itemTopInsert = 12.px
                                itemHolder.itemView.setBackgroundResource(R.color.item_group_item_select_background)
                            }else if (isFirstPosition()){
                                itemTopInsert = 12.px
                                itemHolder.itemView.setBackgroundResource(R.drawable.item_group_header_select_background)
                            }else if (isLastPosition()){
                                itemTopInsert = 1.px
                                itemLeftOffset = 12.px
                                itemRightOffset = 12.px
                                itemDecorationColor = ContextCompat.getColor(this@SettingActivity,me.parade.lib_common.R.color.md_theme_outlineVariant)
                                itemHolder.itemView.setBackgroundResource(R.drawable.item_group_footer_select_background)
                            }else{
                                itemTopInsert = 1.px
                                itemLeftOffset = 12.px
                                itemRightOffset = 12.px
                                itemDecorationColor = ContextCompat.getColor(this@SettingActivity,me.parade.lib_common.R.color.md_theme_outlineVariant)
                                itemHolder.itemView.setBackgroundResource(R.color.item_group_item_select_background)
                            }
                        }
                    }
                }
            }
        }
    }

}