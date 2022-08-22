package com.rkgroup.bookapp.utils;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import com.rkgroup.bookapp.model.PdfFile;

import java.io.File;


public class ThumbnailBuilder implements ModelLoader<PdfFile, Bitmap> {


    @Nullable
    @Override
    public LoadData<Bitmap> buildLoadData(@NonNull PdfFile input, int width, int height, @NonNull Options options) {
        return new LoadData<>(new ObjectKey(input), new ThumbnailCreator(input));
    }

    @Override
    public boolean handles(@NonNull PdfFile input) {
        // handles only pdf file
        return input.getFilePath().endsWith(".pdf");
    }


    private static class ThumbnailCreator implements DataFetcher<Bitmap> {
        private final PdfFile input;

        public ThumbnailCreator(PdfFile input) {
            this.input = input;
        }

        @Override
        public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super Bitmap> callback) {
            try {
                File file = new File(input.getFilePath());
                ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);
                if (pdfRenderer.getPageCount() != 0) {
                    PdfRenderer.Page page = pdfRenderer.openPage(0);
                    Bitmap output = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    page.render(output, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    page.close();
                    callback.onDataReady(output);
                }
                pdfRenderer.close();
                parcelFileDescriptor.close();

            } catch (Exception e) {
                callback.onLoadFailed(e);
            }

        }

        @Override
        public void cleanup() {
            // empty

        }

        @Override
        public void cancel() {
            // empty

        }

        @NonNull
        @Override
        public Class<Bitmap> getDataClass() {
            // output data class
            return Bitmap.class;
        }

        @NonNull
        @Override
        public DataSource getDataSource() {
            // data source local or network base
            return DataSource.LOCAL;
        }

    }
}
