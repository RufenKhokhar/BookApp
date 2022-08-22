package com.rkgroup.bookapp.utils

import android.content.Context
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.Glide
import com.rkgroup.bookapp.model.PdfFile
import android.graphics.Bitmap
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.rkgroup.bookapp.utils.ThumbnailBuilderFactory

@GlideModule
class ThumbnailAppModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // register your Builder in Module
        // String.class is input and Bitmap.class is the output of ThumbnailBuilder
        registry.prepend(PdfFile::class.java, Bitmap::class.java, ThumbnailBuilderFactory())
    }
}