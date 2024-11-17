package me.parade.lib_demo.download

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import me.parade.lib_base.base.BaseActivity
import me.parade.lib_base.download.DownloadResult
import me.parade.lib_base.download.MediaDownloader
import me.parade.lib_common.toast.ToastManager
import me.parade.lib_common.toast.ToastType
import me.parade.lib_demo.R
import me.parade.lib_demo.databinding.ActivityDownloadDemoBinding

class DownloadDemoActivity : BaseActivity<ActivityDownloadDemoBinding,DownloadDemoModel>() {

    private val imgNetUrl = "http://www.wuzhen.com.cn/uploads/summary/2018110109053169919.jpg"
    private val fileUrl =
        "https://dldir1.qq.com/weixin/android/weixin8053android2740_0x28003533_arm64.apk"


    override fun getLayoutResId() = R.layout.activity_download_demo
    override fun initView(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            launch {
                viewModel.down.collect{ result->
                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)){
                        when (result){
                            is DownloadResult.Progress -> {
                                if (!ToastManager.isShow()){
                                    ToastManager.show(supportFragmentManager,ToastType.LOADING,"下载中")
                                }
                            }
                            is DownloadResult.Success -> {
                                ToastManager.dismissLoading()
                                ToastManager.show(supportFragmentManager,ToastType.SUCCESS,"下载成功")
                            }
                            is DownloadResult.Error -> {}
                            DownloadResult.Idle -> {}
                        }
                    }
                }
            }
        }


        binding.btn1.setOnClickListener {
            viewModel.downLoadImage(this,imgNetUrl,MediaDownloader.FileType.IMAGE)
        }

        binding.btn3.setOnClickListener {
            viewModel.downLoadImage(this,fileUrl,MediaDownloader.FileType.GENERAL)
        }
    }


}