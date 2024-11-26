package me.parade.lib_base.download

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.blankj.utilcode.util.FileUtils.isFileExists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import me.parade.lib_base.net.RetrofitManager
import me.parade.lib_base.net.service.Api
import me.parade.lib_common.download.DownloadInfo
import me.parade.lib_common.ext.getFileNameByUrl
import me.parade.lib_common.ext.getMimeTypeFromFromUrl
import me.parade.lib_common.download.DownloadResult
import me.parade.lib_common.download.DownloadUtil
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MediaDownloader {
    /**
     * 下载文件到指定位置
     * @param context Context
     * @param fileUrl 文件URL
     * @param fileName 文件名
     * @param fileType 下载类型
     * @param childFolder 子文件夹名称（用于自定义保存路径）
     */
    fun downloadFile(
        context: Context,
        fileUrl: String,
        fileType: FileType,
        childFolder: String = ""
    ): Flow<DownloadResult> = flow {
        try {
            val api = RetrofitManager.create(Api::class.java)
            val response = api.downloadFile(fileUrl)
            val contentLength = response.contentLength()

            val downloadInfo = when (fileType) {
                FileType.IMAGE -> DownloadInfo(
                    collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    directory = Environment.DIRECTORY_DCIM,
                    mimeType = fileUrl.getMimeTypeFromFromUrl()
                )
                FileType.GENERAL -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    DownloadInfo(
                        collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                        directory = Environment.DIRECTORY_DOWNLOADS,
                        mimeType = fileUrl.getMimeTypeFromFromUrl()
                    )
                } else {
                    DownloadInfo(
                        directory = Environment.DIRECTORY_DOWNLOADS,
                        mimeType = fileUrl.getMimeTypeFromFromUrl()
                    )
                }
            }
            val uniqueFileName = DownloadUtil.getUniqueFileName(context, fileUrl.getFileNameByUrl(), childFolder, downloadInfo)
            val (uri, outputStream) = DownloadUtil.createOutputFile(
                context,
                uniqueFileName,
                childFolder,
                downloadInfo
            )

            // 写入文件数据
            outputStream.use { output ->
                response.byteStream().use { input ->
                    var totalBytesRead = 0L
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var bytesRead: Int

                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead
                        val progress = ((totalBytesRead * 100) / contentLength).toInt()
                        emit(DownloadResult.Progress(progress, totalBytesRead, contentLength))
                    }
                    output.flush()
                }
            }

            // 完成下载，更新文件状态
            DownloadUtil.finishDownload(context, uri, downloadInfo.mimeType)

            emit(DownloadResult.Success(uniqueFileName, uri))

        } catch (e: Exception) {
            emit(DownloadResult.Error(e.message ?: "Unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)


    enum class FileType {
        IMAGE,
        GENERAL
    }
}
