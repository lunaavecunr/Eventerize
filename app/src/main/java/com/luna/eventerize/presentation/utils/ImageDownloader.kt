package com.luna.eventerize.presentation.utils

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.luna.eventerize.R
import java.io.File

class ImageDownloader {

    fun downloadImage(
        imageURL: String,
        fileName : String,
        dirPath: String?,
        eventName: String?,
        context : Context
    ) {

        val file = File("$dirPath/Eventerize/$eventName/$fileName")

        if (file.exists()) {

        } else {
            var builder = NotificationCompat.Builder(context!!, "notif")
                .setContentTitle("Eventerize")
                .setContentText("Image download name : $fileName")
                .setSmallIcon(R.mipmap.eventerize)
                .setPriority(NotificationCompat.PRIORITY_MAX)

            with(NotificationManagerCompat.from(context!!)) {
                notify(0, builder.build())
            }

            val downloadId = PRDownloader.download(imageURL, "$dirPath/Eventerize/$eventName", fileName)
                .build()
                .setOnStartOrResumeListener {

                }
                .setOnPauseListener {

                }
                .setOnCancelListener {

                }
                .setOnProgressListener { progress ->
                    val PROGRESS_MAX = progress.totalBytes.toInt()

                    NotificationManagerCompat.from(context!!).apply {
                        // Issue the initial notification with zero progress
                        builder.setProgress(PROGRESS_MAX, progress.currentBytes.toInt(), false)
                        notify(0, builder.build())
                    }
                }
                .start(object : OnDownloadListener {
                    override fun onError(error: Error?) {
                        Toast.makeText(context, "Download error server : " + error!!.isServerError, Toast.LENGTH_SHORT)
                            .show()
                        Toast.makeText(
                            context,
                            "Download error connect : " + error!!.isConnectionError,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onDownloadComplete() {
                        NotificationManagerCompat.from(context!!).apply {
                            builder.setContentText("Download complete")
                                .setProgress(0, 0, false)
                            notify(0, builder.build())
                        }


                        Toast.makeText(context, "Download completed for name : $fileName", Toast.LENGTH_SHORT).show()
                        val fileCreated = File("$dirPath/Eventerize/$eventName/$fileName")


                        var arr = arrayOf(fileCreated.absolutePath)

                        var arr2 = arrayOf("images/*")
                        MediaScannerConnection.scanFile(context, arr, arr2) { s: String, uri: Uri ->

                        }

                    }
                })
        }
    }

}