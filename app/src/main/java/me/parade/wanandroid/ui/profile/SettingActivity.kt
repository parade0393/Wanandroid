package me.parade.wanandroid.ui.profile

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.angcyo.dsladapter.DslAdapter
import com.angcyo.dsladapter.dslItem
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

        binding.main.apply {
            layoutManager = LinearLayoutManager(this@SettingActivity)
            adapter = dslAdapter
        }

        dslAdapter.render {
            dslItem(DslSettingItem()){
                itemTopInsert = 12.px
                itemGroups = mutableListOf("person")
                settingData = SettingItem(mainTitle = "个人信息")
                itemGroupParams.apply {

                }
            }
            dslItem(DslSettingItem()){
                settingData = SettingItem(mainTitle = "账户与安全")
                itemGroups = mutableListOf("person")
            }


        }
    }

}