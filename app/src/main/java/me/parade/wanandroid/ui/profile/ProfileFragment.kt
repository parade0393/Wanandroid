package me.parade.wanandroid.ui.profile

import android.os.Bundle
import me.parade.lib_base.base.BaseFragment
import me.parade.lib_base.base.EmptyViewModel
import me.parade.wanandroid.databinding.FragmentProfileBinding


class ProfileFragment : BaseFragment<FragmentProfileBinding,EmptyViewModel>() {
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoad(tag: String) {
        updateStatusBarAppearance(true)
    }

    override fun initData() {

    }
}