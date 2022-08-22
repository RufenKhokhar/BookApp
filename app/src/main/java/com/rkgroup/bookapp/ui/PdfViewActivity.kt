package com.rkgroup.bookapp.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rkgroup.bookapp.R
import com.rkgroup.bookapp.databinding.ActivityPdfViewBinding
import com.rkgroup.bookapp.model.DatabasePdfFile
import com.rkgroup.bookapp.model.PdfFile
import com.rkgroup.bookapp.utils.Constants
import com.rkgroup.bookapp.utils.purify
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.Downloader
import java.io.File
import java.util.*


class PdfViewActivity : AppCompatActivity() {
    private val pdfFile: Parcelable by lazy { intent.getParcelableExtra(Constants.KEY_SOURCE_FILE)!! }
    private lateinit var binding: ActivityPdfViewBinding

    private val targetFile by lazy { File(cacheDir, "${title.purify()}.pdf") }
    private val downloader by lazy {
        val fetchConfiguration: FetchConfiguration = FetchConfiguration.Builder(this)
            .setGlobalNetworkType(NetworkType.ALL)
            .setHttpDownloader(HttpUrlConnectionDownloader(Downloader.FileDownloaderType.PARALLEL))
            .build()
        Fetch.getInstance(fetchConfiguration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initUI()
    }

    private fun initUI() {
        if (pdfFile is PdfFile) {
            title = (pdfFile as PdfFile).bookName
            binding.pdfView.fromFile(File((pdfFile as PdfFile).filePath)).load()
            binding.tvProgress.visibility = View.GONE
            binding.progress.visibility = View.GONE
        } else {
            title = (pdfFile as DatabasePdfFile).bookName
            downloadAndOpen()
        }
    }

    private fun downloadAndOpen() {
        downloader.addListener(downloadListener)
        val request = Request((pdfFile as DatabasePdfFile).downloadURL, targetFile.absolutePath)
        request.priority = Priority.HIGH
        downloader.enqueue(request)
    }

    private val downloadListener = object : FetchListener {
        override fun onAdded(download: Download) {
            binding.tvProgress.setText(R.string.loading)
        }

        override fun onCancelled(download: Download) {}
        override fun onCompleted(download: Download) {
            runOnUiThread {
                binding.tvProgress.visibility = View.GONE
                binding.progress.visibility = View.GONE
                binding.pdfView.fromFile(File(download.file)).load()
            }

        }

        override fun onDeleted(download: Download) {}
        override fun onDownloadBlockUpdated(
            download: Download,
            downloadBlock: DownloadBlock,
            totalBlocks: Int
        ) {
        }

        override fun onError(download: Download, error: Error, throwable: Throwable?) {
            if (error == Error.REQUEST_WITH_FILE_PATH_ALREADY_EXIST) {
                binding.tvProgress.visibility = View.GONE
                binding.progress.visibility = View.GONE
                binding.pdfView.fromFile(File(download.file)).load()
            }

        }

        override fun onPaused(download: Download) {}
        override fun onProgress(
            download: Download,
            etaInMilliSeconds: Long,
            downloadedBytesPerSecond: Long
        ) {
            binding.tvProgress.text =
                String.format(Locale.ENGLISH, "Loading... %d%%", download.progress)
        }

        override fun onQueued(download: Download, waitingOnNetwork: Boolean) {}
        override fun onRemoved(download: Download) {}
        override fun onResumed(download: Download) {}
        override fun onStarted(
            download: Download,
            downloadBlocks: List<DownloadBlock>,
            totalBlocks: Int
        ) {
        }

        override fun onWaitingNetwork(download: Download) {}
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        downloader.removeListener(downloadListener)
        super.onDestroy()
    }
}