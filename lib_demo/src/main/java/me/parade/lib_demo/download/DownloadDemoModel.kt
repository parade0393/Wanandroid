package me.parade.lib_demo.download

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.parade.lib_base.base.BaseViewModel
import me.parade.lib_base.download.DownloadResult
import me.parade.lib_base.download.MediaDownloader

/**
 * @author : parade
 * date : 2024/11/17
 * description :
 */
class DownloadDemoModel:BaseViewModel() {



    private val _down = MutableStateFlow<DownloadResult>(DownloadResult.Idle)
    val down: StateFlow<DownloadResult> = _down

    fun downLoadImage(context: Context,fileUrl:String,fileType:MediaDownloader.FileType){
        viewModelScope.launch {
            MediaDownloader.downloadFile(context,fileUrl,fileType,"parade").collect{
                _down.emit(it)
            }
        }
    }
}