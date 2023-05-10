package com.example.fileviewer.domain

import android.annotation.SuppressLint
import android.webkit.MimeTypeMap
import com.example.fileviewer.domain.FileHash.Companion.format
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import kotlin.math.log10
import kotlin.math.pow

data class FileHash(
    val id: Int = DEFAULT_ID,
    val absoluteFilePath: String,
    val format: String = "",
    val size: String = "0B",
    val date: String = "",
    val hash: Int?
) : File(absoluteFilePath) {

    companion object {
        const val DEFAULT_ID = 0
        @SuppressLint("SimpleDateFormat")
        val format = SimpleDateFormat("dd-MM-yyyy")
    }
}

fun File.formatDate(): String = format.format(this.lastModified())

fun File.getFormat(): String = MimeTypeMap.getFileExtensionFromUrl(this.toURI().toString())

fun File.getReadableSize(): String {
    val size = this.length()
    if (size <= 0) return "0"
    val units = arrayOf("B", "kB", "MB", "GB", "TB")
    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
}
