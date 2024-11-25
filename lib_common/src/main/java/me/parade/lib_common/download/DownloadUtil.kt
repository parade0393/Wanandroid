package me.parade.lib_common.download

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
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
        return Pair(Uri.fromFile(file), FileOutputStream(file))
    }

    fun finishDownload(context: Context, uri: Uri, mimeType: String) {
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
}