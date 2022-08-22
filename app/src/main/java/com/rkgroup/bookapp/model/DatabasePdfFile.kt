package com.rkgroup.bookapp.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.util.*
@Keep
@Parcelize
data class DatabasePdfFile(
    val bookName: String,
    val bookSize: String,
    val bookThumbnail: String,
    val bookPages: String,
    val publishYear: String,
    val downloadURL: String,
    val id: String = UUID.randomUUID().toString()
) : Parcelable{
    constructor():this("","","","","","")
}


fun DatabasePdfFile.toFileInfo(): String =
    String.format("%s · %s · %s", bookPages, publishYear, bookSize)
