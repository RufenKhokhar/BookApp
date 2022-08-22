package com.rkgroup.bookapp.utils

import android.graphics.Bitmap
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.rkgroup.bookapp.model.PdfFile

class ThumbnailBuilderFactory : ModelLoaderFactory<PdfFile, Bitmap> {
    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<PdfFile, Bitmap> {
        return ThumbnailBuilder()
    }

    override fun teardown() {
        // empty
    }

}