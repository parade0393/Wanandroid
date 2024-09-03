package me.parade.wanandroid.ui.home.child.explore

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import me.parade.lib_base.base.BaseFragment
import me.parade.lib_base.download.DownloadResult
import me.parade.lib_base.ext.logd
import me.parade.wanandroid.R
import me.parade.wanandroid.databinding.FragmentExploreBinding


class ExploreFragment : BaseFragment<FragmentExploreBinding,ExploreVM>() {

    companion object{
        fun newInstance(): ExploreFragment = with(ExploreFragment()){
            arguments = bundleOf()
            this
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        viewModel.getData()
        lifecycleScope.launch {
                launch {
                    viewModel.data.collect{
                        logd(it)
                    }
                }
                launch {
                    viewModel.down.collect{ result->
                        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)){
                            when (result){
                                is DownloadResult.Progress -> {
                                    "${result.progress}-${result.totalBytes}".logd()
                                }
                                is DownloadResult.Success -> {
                                    "下载成功".logd()
                                    Toast.makeText(requireContext(),"下载成功:${result.fileName}",Toast.LENGTH_SHORT).show()
                                }
                                is DownloadResult.Error -> {}
                                DownloadResult.Idle -> {}
                            }
                        }
                    }
                }

            launch {
                viewModel.bitFloe.collect{result->
                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)){
                        when (result){
                            is DownloadResult.Progress -> {

                            }
                            is DownloadResult.Success -> {
                                "保存成功".logd()
                                Toast.makeText(requireContext(),"下载成功:${result.fileName}",Toast.LENGTH_SHORT).show()
                            }
                            is DownloadResult.Error -> {}
                            DownloadResult.Idle -> {}
                        }
                    }
                }
            }

//            }
        }
        binding.btnQuest.setOnClickListener {
            viewModel.getData()
        }
        binding.btnDown.setOnClickListener {
          viewModel.downLoad(requireContext())
        }

        binding.btnBitmap.setOnClickListener {
            ContextCompat.getDrawable(requireContext(), R.drawable.demo)?.let {bitmap->
               viewModel.saveBitMapToAlbum(bitmap.toBitmap(),"demo.jpg",requireContext(),"",90)
            }
        }
    }

    override fun lazyLoad(tag: String) {
        super.lazyLoad(tag)

    }

}