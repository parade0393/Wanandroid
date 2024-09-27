package me.parade.wanandroid.ui.home.child.answer

import android.os.Bundle
import androidx.core.os.bundleOf
import me.parade.lib_base.base.BaseFragment
import me.parade.lib_base.base.EmptyViewModel
import me.parade.lib_image.CoilImageLoader
import me.parade.lib_image.ImageLoaderManager
import me.parade.lib_image.ImageLoaderOptions
import me.parade.wanandroid.databinding.FragmentAnswerBinding


class AnswerFragment : BaseFragment<FragmentAnswerBinding,EmptyViewModel>() {

    companion object{
        fun newInstance():AnswerFragment = with(AnswerFragment()){
            arguments = bundleOf()
            this
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        val imageUrl = "http://www.wuzhen.com.cn/uploads/cultrue/2023061116535140257.jpg"

        ImageLoaderManager.loadImage(imageUrl, binding.iv1)
        ImageLoaderManager.loadImage(imageUrl,binding.ivRound, ImageLoaderOptions(isCircle = true))
        ImageLoaderManager.loadImage(imageUrl,binding.ivCorner,
            ImageLoaderOptions(cornerRadius = 100f)
        )
        val coilImageLoader = CoilImageLoader()
        ImageLoaderManager.loadImage(imageUrl, binding.ivCoil, loader = coilImageLoader)
        ImageLoaderManager.loadImage(imageUrl,binding.ivRoundCoil, ImageLoaderOptions(isCircle = true),coilImageLoader)
        ImageLoaderManager.loadImage(imageUrl,binding.ivCornerCoil,
            ImageLoaderOptions(cornerRadius = 100f),
            coilImageLoader
        )
    }

    override fun initData() {

    }

    override fun initObserver() {

    }

}