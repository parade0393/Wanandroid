package me.parade.lib_base.download

import android.net.Uri


sealed class DownloadResult {
    data object Idle : DownloadResult()
    data class Progress(val progress: Int,val downloadedBytes: Long,val totalBytes: Long) : DownloadResult()
    data class Success(val fileName: String,val uri: Uri) : DownloadResult()
    data class Error(val error: String) : DownloadResult()
}