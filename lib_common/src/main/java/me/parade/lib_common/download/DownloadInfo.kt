package me.parade.lib_common.download

import android.net.Uri

data class DownloadInfo(
    val collection: Uri? = null,
    val directory: String,
    val mimeType: String
    )