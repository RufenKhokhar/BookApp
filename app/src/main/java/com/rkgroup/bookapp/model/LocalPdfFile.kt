package com.rkgroup.bookapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class PdfFile(val bookName: String, val filePath: String) : Parcelable


fun PdfFile.toFileInfo(): String {
    val file = File(filePath)
    val size = file.length()
    val date = file.lastModified()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
    val formattedDate = dateFormat.format(Date(date))
    val formattedSize = String.format("%.2fMB", size / 1024f / 1024f)
    return String.format("%s | %s", formattedDate, formattedSize)

}
