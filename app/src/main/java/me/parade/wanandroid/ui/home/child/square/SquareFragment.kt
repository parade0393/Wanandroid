package me.parade.wanandroid.ui.home.child.square

import android.os.Bundle
import androidx.core.os.bundleOf
import me.parade.lib_base.base.BaseFragment
import me.parade.lib_base.base.EmptyViewModel
import me.parade.wanandroid.databinding.FragmentSquareBinding


class SquareFragment : BaseFragment<FragmentSquareBinding,EmptyViewModel>() {

    companion object{
        fun newInstance(): SquareFragment = with(SquareFragment()){
            arguments = bundleOf()
            this
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

}