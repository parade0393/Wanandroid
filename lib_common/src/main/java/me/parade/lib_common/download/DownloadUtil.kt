package me.parade.lib_common.download

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DownloadUtil {

    suspend fun getUniqueFileName(
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

    fun createOutputFile(
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
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, "${context.packageName}.FileProvider", file)
        } else {
            Uri.fromFile(file)
        }
        return Pair(uri, FileOutputStream(file))
    }

    fun finishDownload(context: Context, uri: Uri, mimeType: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.IS_PENDING, 0)
            }
            context.contentResolver.update(uri, values, null, null)
        } else {
            uri.path?.let { path ->
                val file = File(path)
                if (file.exists()) {
                    when {
                        mimeType.startsWith("image/") -> {
                            insertImageToMediaStore(context, file, mimeType, uri)
                            context.contentResolver.update(uri,ContentValues().apply {
                                put(
                                    MediaStore.Images.Media.SIZE,
                                    file.length()
                                )
                            },null,null)
                        }
                    }
                }


                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(path),
                    arrayOf(mimeType)
                ) { _, _ -> }

                context.sendBroadcast(
                    Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file))
                )

            }
        }
    }

    private fun insertImageToMediaStore(context: Context, file: File, mimeType: String, uri: Uri) {
        try {
            // 对于图片，尝试直接插入MediaStore
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues().apply {
                    put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                    put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
                    put(MediaStore.Images.Media.DATA, file.absolutePath)
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}