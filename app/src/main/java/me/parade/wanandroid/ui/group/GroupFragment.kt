package me.parade.wanandroid.ui.group

import android.os.Bundle
import me.parade.lib_base.base.BaseFragment
import me.parade.lib_base.base.EmptyViewModel
import me.parade.wanandroid.databinding.FragmentGroupBinding


class GroupFragment : BaseFragment<FragmentGroupBinding,EmptyViewModel>() {
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {

    }

    override fun initObserver() {

    }


    override fun lazyLoad(tag: String) {
        updateStatusBarAppearance(true)
    }
}