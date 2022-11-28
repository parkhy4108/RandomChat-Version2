package com.dev_musashi.ranchat.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class SaveImage @Inject constructor(
    context: Context
) {
    private val _context = context
    private val _contentResolver = _context.contentResolver

    private fun uriToBitmap(imageUri: Uri): Bitmap? {
        try {
            val url = URL(imageUri.toString())
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            SnackBarManager.showMessage(e.message.toString())
        }
        return null
    }

    fun saveImage(imageUri: Uri) {
        if (Build.VERSION.SDK_INT < 29) {
            saveImageUnder29(imageUri = imageUri)
        } else {
            saveImageUpper29(imageUri = imageUri)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageUpper29(imageUri: Uri) {
        val fileName = System.currentTimeMillis().toString() + ".png"
        val contentValues = ContentValues()
        contentValues.apply {
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/RandomChat")
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val uri =
            _contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        try {
            if (uri != null) {
                val img = _contentResolver.openFileDescriptor(uri, "w", null)
                if (img != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val fos = FileOutputStream(img.fileDescriptor)
                        val bitmap = uriToBitmap(imageUri)
                        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
                        withContext(Dispatchers.IO) {
                            fos.flush()
                            fos.close()
                        }
                        contentValues.clear()
                        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                        _contentResolver.update(uri, contentValues, null, null)
                        SnackBarManager.showMessage("사진 저장이 완료되었습니다.")
                        img.close()
                    }
                }
            }
        } catch (e: Exception) {
            SnackBarManager.showMessage(e.message.toString())
        }
    }

    private fun saveImageUnder29(imageUri: Uri) {
        val fileName = System.currentTimeMillis().toString() + ".png"
        val externalStorage = Environment.getExternalStorageDirectory().absolutePath
        val path = "$externalStorage/DCIM/RandomChat"
        val dir = File(path)

        if (dir.exists().not()) {
            dir.mkdir()
        }

        try {
            val fileItem = File("$dir/$fileName")
            fileItem.createNewFile()
            val fos = FileOutputStream(fileItem)
            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = uriToBitmap(imageUri)
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
                withContext(Dispatchers.IO) {
                    fos.flush()
                    fos.close()
                }
                _context.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fileItem)
                    )
                )
                SnackBarManager.showMessage("사진 저장이 완료되었습니다.")
            }


        } catch (e: Exception) {
            SnackBarManager.showMessage(e.message.toString())
        }
    }
}