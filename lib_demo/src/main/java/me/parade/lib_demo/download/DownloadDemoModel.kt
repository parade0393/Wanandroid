package me.parade.lib_demo.download

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import me.parade.lib_base.base.BaseViewModel
import me.parade.lib_base.download.MediaDownloader
import me.parade.lib_common.download.DownloadResult

/**
 * @author : parade
 * date : 2024/11/17
 * description :
 */
class DownloadDemoModel:BaseViewModel() {



    private val _down = MutableStateFlow<DownloadResult>(DownloadResult.Idle)
    val down: StateFlow<DownloadResult> = _down

    private val _downloadState = MutableStateFlow<DownloadResult>(DownloadResult.Idle)
    val downloadState = _downloadState.asStateFlow()

    fun downLoadImage(context: Context,fileUrl:String,fileType:MediaDownloader.FileType){
        viewModelScope.launch {
            MediaDownloader.downloadFile(context,fileUrl,fileType,"parade").collect{
                _down.emit(it)
            }
        }
    }

    fun downLoadFile(context: Context,fileUrl:String){
        viewModelScope.launch {
            MediaDownloader.downloadFile(context,fileUrl,MediaDownloader.FileType.GENERAL,"parade")
                .catch {e->
                    _downloadState.emit(DownloadResult.Error(e.message?:"未知错误"))
                }
                .collect{ result->
                    _downloadState.emit(result)
                }
        }
    }
}