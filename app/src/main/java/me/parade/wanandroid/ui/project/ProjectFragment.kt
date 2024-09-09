package me.parade.wanandroid.ui.project

import android.os.Bundle
import me.parade.lib_base.base.BaseFragment
import me.parade.lib_base.base.EmptyViewModel
import me.parade.wanandroid.databinding.FragmentProjectBinding


class ProjectFragment : BaseFragment<FragmentProjectBinding,EmptyViewModel>() {

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoad(tag: String) {
        updateStatusBarAppearance(false)
    }

    override fun initData() {

    }
}