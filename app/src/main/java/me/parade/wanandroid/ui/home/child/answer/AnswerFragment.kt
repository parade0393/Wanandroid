package me.parade.wanandroid.ui.home.child.answer

import android.os.Bundle
import androidx.core.os.bundleOf
import me.parade.lib_base.base.BaseFragment
import me.parade.lib_base.base.EmptyViewModel
import me.parade.wanandroid.databinding.FragmentAnswerBinding


class AnswerFragment : BaseFragment<FragmentAnswerBinding,EmptyViewModel>() {

    companion object{
        fun newInstance():AnswerFragment = with(AnswerFragment()){
            arguments = bundleOf()
            this
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

}