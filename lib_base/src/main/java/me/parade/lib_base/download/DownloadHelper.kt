package me.parade.lib_base.download

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import me.parade.lib_base.ext.getFileNameByUrl
import me.parade.lib_base.ext.getMimeTypeFromFromUrl
import me.parade.lib_base.ext.isImageFile
import me.parade.lib_base.net.RetrofitManager
import me.parade.lib_base.net.service.Api
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * 图片默认保存到相册
 * 其他文件下载到系统下载目录
 */
object DownloadHelper {
    suspend fun downloadFile(context:Context,fileUrl:String,childFolder:String = context.packageName):Flow<DownloadResult> = flow {
        try {
            val body = RetrofitManager.create(Api::class.java).downloadFile(fileUrl)
            val contentLength = body.contentLength()
//            val httpUrl = URL(fileUrl)
//            val urlConnection = httpUrl.openConnection() as HttpURLConnection
//            urlConnection.requestMethod = "GET"
//            val contentLength = urlConnection.contentLengthLong
            var uri: Uri? = null
            var outputStream: OutputStream? = null
            val outputFile = OutputFileTaker()
            if (fileUrl.isImageFile()) {
                val pair = saveImageToGallery(context,fileUrl,outputFile,childFolder)
                outputStream = pair.first
                uri = pair.second
            } else {
                outputStream = saveFileToDownloads(context,fileUrl)
            }
            body.byteStream().use {inputStream->
                outputStream.use {output->
                    var totalBytesRead = 0L
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var bytesRead: Int

                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        output.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead
                        val progress = ((totalBytesRead * 100) / contentLength).toInt()
                        emit(DownloadResult.Progress(progress, totalBytesRead, contentLength))
                    }
                    output.flush()
                }
            }
            if (fileUrl.isImageFile() && uri != null) {
                val contentValues = ContentValues()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING,0)
                    context.contentResolver.update(uri,contentValues,null,null)
                }else{
                    outputFile.file?.let { file ->
                        contentValues.put(MediaStore.MediaColumns.SIZE,file.length())
                        context.contentResolver.update(uri,contentValues,null,null)
                        // 通知媒体库更新，部分设备不更新 图库看不到???
                        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
                        context.sendBroadcast(intent)
                    }
                }

            }
            emit(DownloadResult.Success(fileUrl.getFileNameByUrl(), uri?:Uri.EMPTY))
        }catch (e:Exception){
            emit(DownloadResult.Error(e.message?:"未知异常"))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun saveImageToGallery(context: Context,fileUrl:String,outputFile:OutputFileTaker,childFolder:String):Pair<OutputStream, Uri> = withContext(Dispatchers.IO){
        val contentValues = ContentValues().apply {
            val date = System.currentTimeMillis() / 1000
            put(MediaStore.MediaColumns.DATE_ADDED, date)
            put(MediaStore.MediaColumns.DATE_MODIFIED, date)
            put(MediaStore.MediaColumns.MIME_TYPE,fileUrl.getMimeTypeFromFromUrl())
        }
        //保存的位置
        val imageCollection:Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val path = "${Environment.DIRECTORY_PICTURES}/$childFolder"
            contentValues.apply {
                put(MediaStore.MediaColumns.RELATIVE_PATH,path)
                put(MediaStore.MediaColumns.IS_PENDING,1)
                put(MediaStore.MediaColumns.DISPLAY_NAME,fileUrl.getFileNameByUrl())
            }
            imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }else{
            // 老版本
            val pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val saveDir = pictures

            if (!saveDir.exists() && !saveDir.mkdirs()) {
                throw IllegalStateException("路径不存在")
            }

            // 文件路径查重，重复的话在文件名后拼接数字
            var imageFile = File(saveDir, fileUrl.getFileNameByUrl())
            val fileNameWithoutExtension = imageFile.nameWithoutExtension
            val fileExtension = imageFile.extension

            // 查询文件是否已经存在
            var queryUri = context.contentResolver.queryMediaImage28(imageFile.absolutePath)
            var suffix = 1
            while (queryUri != null) {
                // 存在的话重命名，路径后面拼接 fileNameWithoutExtension(数字).png
                val newName = fileNameWithoutExtension + "(${suffix++})." + fileExtension
                imageFile = File(saveDir, newName)
                queryUri = context.contentResolver.queryMediaImage28(imageFile.absolutePath)
            }

            contentValues.apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.name)
                // 保存路径
                val imagePath = imageFile.absolutePath
                put(MediaStore.Images.Media.DATA, imagePath)
            }
            outputFile.file = imageFile
            imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val imageUri = context.contentResolver.insert(imageCollection, contentValues)
            ?: throw IllegalStateException("Uri创建失败")

        val outputStream  = context.contentResolver.openOutputStream(imageUri)
            ?: throw IllegalStateException("保存失败")
        Pair(outputStream, imageUri)
    }

    private suspend fun saveFileToDownloads(context: Context,fileName: String,childFolder:String = context.packageName): OutputStream = withContext(Dispatchers.IO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "*/*")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + childFolder)
            }

            val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                ?: throw IllegalStateException("Uri创建失败")

            context.contentResolver.openOutputStream(uri)
                ?: throw IllegalStateException("保存失败")
        } else {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val customDir = File(downloadsDir, childFolder)
            if (!customDir.exists()) {
                customDir.mkdirs()
            }
            val file = File(customDir, fileName)
            FileOutputStream(file)
        }
    }

    /**
     * Android Q以下版本，查询媒体库中当前路径是否存在
     * @return Uri 返回null时说明不存在，可以进行图片插入逻辑
     */
    private fun ContentResolver.queryMediaImage28(imagePath: String): Uri? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) return null

        val imageFile = File(imagePath)
        if (imageFile.canRead() && imageFile.exists()) {
            // 文件已存在，返回一个file://xxx的uri
            // 这个逻辑也可以不要，但是为了减少媒体库查询次数，可以直接判断文件是否存在
            return Uri.fromFile(imageFile)
        }
        // 保存的位置
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        // 查询是否已经存在相同图片
        val query = this.query(
            collection,
            arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA),
            "${MediaStore.Images.Media.DATA} == ?",
            arrayOf(imagePath), null
        )
        query?.use {
            while (it.moveToNext()) {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val id = it.getLong(idColumn)
                val existsUri = ContentUris.withAppendedId(collection, id)
                return existsUri
            }
        }
        return null
    }

    /**
     * 用于Q以下系统获取图片文件大小来更新[MediaStore.Images.Media.SIZE]
     */
    private class OutputFileTaker(var file: File? = null)
}

