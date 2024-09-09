package me.parade.wanandroid.ui.navigator

import android.os.Bundle
import me.parade.lib_base.base.BaseFragment
import me.parade.wanandroid.DemoVM
import me.parade.wanandroid.databinding.FragmentNavigatorBinding


class NavigatorFragment : BaseFragment<FragmentNavigatorBinding,DemoVM>() {
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoad(tag: String) {
        updateStatusBarAppearance(false)
    }

    override fun initData() {

    }
}