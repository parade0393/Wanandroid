package me.parade.lib_demo.download

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import me.parade.lib_base.base.BaseActivity
import me.parade.lib_base.download.MediaDownloader
import me.parade.lib_common.ext.getMimeTypeFromFromUrl
import me.parade.lib_common.toast.ToastManager
import me.parade.lib_common.toast.ToastType
import me.parade.lib_common.utils.DownloadResult
import me.parade.lib_demo.R
import me.parade.lib_demo.databinding.ActivityDownloadDemoBinding

class DownloadDemoActivity : BaseActivity<ActivityDownloadDemoBinding,DownloadDemoModel>() {

    private val imgNetUrl = "http://www.wuzhen.com.cn/uploads/summary/2018110109053169919.jpg"
    private val fileUrl =
        "http://www.moe.gov.cn/jyb_sjzl/ziliao/A22/201804/W020180420344917914799.doc"

    private val fileUrl2 = "http://www.rongchang.gov.cn/zwgk_264/zcwj/xzgfxwj/202411/W020241108519785656150.docx"
    private val fileUrl3 = "http://www.rongchang.gov.cn/zwgk_264/zcwj/xzgfxwj/202409/W020240925391671580459.pdf"

    private val fileUrl4 =
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
                                openFile(this@DownloadDemoActivity,result.uri,result.fileName)
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
            viewModel.downLoadImage(this,fileUrl2,MediaDownloader.FileType.GENERAL)
        }
    }


    private fun openFile(context: Context, uri: Uri, fileName: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                setDataAndType(uri, fileName.getMimeTypeFromFromUrl(fileName))
            }

            // 检查是否有应用可以处理
            val activities = context.packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )

            if (activities.isNotEmpty()) {
                context.startActivity(Intent.createChooser(intent, "选择打开方式"))
            } else {
                Toast.makeText(context, "没有找到可以打开该类型文件的应用", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "打开文件失败: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}