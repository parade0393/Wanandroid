package me.parade.lib_base.download

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import me.parade.lib_base.net.RetrofitManager
import me.parade.lib_base.net.service.Api
import me.parade.lib_common.ext.getFileNameByUrl
import me.parade.lib_common.ext.getMimeTypeFromFromUrl
import me.parade.lib_common.ext.logd
import me.parade.lib_common.utils.DownloadResult
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
                    directory = Environment.DIRECTORY_PICTURES,
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
            downloadInfo.mimeType.logd()
            val uniqueFileName = getUniqueFileName(context, fileUrl.getFileNameByUrl(), childFolder, downloadInfo)
            val (uri, outputStream) = createOutputFile(
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
            finishDownload(context, uri, downloadInfo.mimeType)

            emit(DownloadResult.Success(uniqueFileName, uri))

        } catch (e: Exception) {
            emit(DownloadResult.Error(e.message ?: "Unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    private data class DownloadInfo(
        val collection: Uri? = null,
        val directory: String,
        val mimeType: String
    )

    private suspend fun getUniqueFileName(
        context: Context,
        originalFileName: String,
        childFolder: String,
        downloadInfo: DownloadInfo
    ): String = withContext(Dispatchers.IO) {
        val nameWithoutExtension = originalFileName.substringBeforeLast(".")
        val extension = originalFileName.substringAfterLast(".", "")

        fun generateFileName(timestamp: String = "") = if (extension.isEmpty()) {
            "$nameWithoutExtension$timestamp"
        } else {
            "$nameWithoutExtension$timestamp.$extension"
        }

        // 检查文件是否存在
        if (!isFileExists(context, originalFileName, childFolder, downloadInfo)) {
            return@withContext originalFileName
        }

        // 文件存在，使用时间戳生成新文件名
        val timestamp = SimpleDateFormat("_yyyyMMdd_HHmmss", Locale.getDefault())
            .format(Date())
        return@withContext generateFileName(timestamp)
    }

    private fun isFileExists(
        context: Context,
        fileName: String,
        childFolder: String,
        downloadInfo: DownloadInfo
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isFileExistsQ(context, fileName, childFolder, downloadInfo)
        } else {
            isFileExistsLegacy(fileName, childFolder, downloadInfo)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun isFileExistsQ(
        context: Context,
        fileName: String,
        childFolder: String,
        downloadInfo: DownloadInfo
    ): Boolean {
        val relativePath = buildString {
            append(downloadInfo.directory)
            if (childFolder.isNotEmpty()) {
                append(File.separator)
                append(childFolder)
            }
        }

        val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND " +
                "${MediaStore.MediaColumns.RELATIVE_PATH} = ?"
        val selectionArgs = arrayOf(fileName, "$relativePath/")

        return context.contentResolver.query(
            downloadInfo.collection!!,
            arrayOf(MediaStore.MediaColumns._ID),
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            cursor.count > 0
        } ?: false
    }

    private fun isFileExistsLegacy(
        fileName: String,
        childFolder: String,
        downloadInfo: DownloadInfo
    ): Boolean {
        val baseDir = Environment.getExternalStoragePublicDirectory(downloadInfo.directory)
        val targetDir = if (childFolder.isNotEmpty()) {
            File(baseDir, childFolder)
        } else {
            baseDir
        }
        return File(targetDir, fileName).exists()
    }

    private fun createOutputFile(
        context: Context,
        fileName: String,
        childFolder: String,
        downloadInfo: DownloadInfo
    ): Pair<Uri, OutputStream> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            createOutputFileQ(context, fileName, childFolder, downloadInfo)
        } else {
            createOutputFileLegacy(context, fileName, childFolder, downloadInfo)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createOutputFileQ(
        context: Context,
        fileName: String,
        childFolder: String,
        downloadInfo: DownloadInfo
    ): Pair<Uri, OutputStream> {
        val relativePath = buildString {
            append(downloadInfo.directory)
            if (childFolder.isNotEmpty()) {
                append(File.separator)
                append(childFolder)
            }
        }

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, downloadInfo.mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(downloadInfo.collection!!, values)
            ?: throw IOException("Failed to create MediaStore entry")

        val outputStream = resolver.openOutputStream(uri)
            ?: throw IOException("Failed to open output stream")

        return Pair(uri, outputStream)
    }

    private fun createOutputFileLegacy(
        context: Context,
        fileName: String,
        childFolder: String,
        downloadInfo: DownloadInfo
    ): Pair<Uri, OutputStream> {
        val baseDir = Environment.getExternalStoragePublicDirectory(downloadInfo.directory)
        val targetDir = if (childFolder.isNotEmpty()) {
            File(baseDir, childFolder).apply { mkdirs() }
        } else {
            baseDir.apply { mkdirs() }
        }

        val file = File(targetDir, fileName)
        return Pair(Uri.fromFile(file), FileOutputStream(file))
    }

    private fun finishDownload(context: Context, uri: Uri, mimeType: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.IS_PENDING, 0)
            }
            context.contentResolver.update(uri, values, null, null)
        } else {
            uri.path?.let { path ->
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(path),
                    arrayOf(mimeType)
                ) { _, _ -> }
            }
        }
    }



    enum class FileType {
        IMAGE,
        GENERAL
    }
}
