package com.rkgroup.bookapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.rkgroup.bookapp.R
import com.rkgroup.bookapp.adapter.LocalFilesViewAdapter
import com.rkgroup.bookapp.databinding.ActivityLocalFilesBinding
import com.rkgroup.bookapp.model.PdfFile
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File


class LocalFilesActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var binding: ActivityLocalFilesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalFilesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.local_files)
        initViews()
    }

    private fun initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                loadFiles()
            } else {
                showPermissionDialog()
            }
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            loadFiles()
        } else {
            storagePermissions.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }


    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun showPermissionDialog() {
        AlertDialog.Builder(this).setTitle("Permission Needed!")
            .setCancelable(false)
            .setMessage("Due to a change of policy in the Android 11 and above , App needs All file access permission to manage all PDFs.")
            .setPositiveButton("Grant") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                try {
                    manageFileStorage.launch(intent)
                } catch (e: Exception) {

                }
            }.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "Need Storage Permission to work properly.", Toast.LENGTH_SHORT).show()
                finish()
            }.show()
    }

    private fun loadFiles() {
        binding.rvInternalPDF.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE
        val pdfFiles = ArrayList<PdfFile>()
        val disposable = Single.create {
            try {
                searchFiles(Environment.getExternalStorageDirectory(), pdfFiles)
                it.onSuccess(Unit)
            } catch (ex: Exception) {
                it.onError(ex)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.rvInternalPDF.visibility = View.VISIBLE
                binding.progress.visibility = View.GONE
                binding.rvInternalPDF.adapter = LocalFilesViewAdapter(pdfFiles)
            }, {
                Toast.makeText(this, "no file found", Toast.LENGTH_SHORT).show()
            })
        compositeDisposable.add(disposable)

    }


    private fun searchFiles(dir: File, pdfFiles: ArrayList<PdfFile>) {
        val suffix = ".pdf"
        val files = dir.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    searchFiles(file, pdfFiles)
                } else {
                    if (file.name.endsWith(suffix) && file.length() != 0L) {
                        pdfFiles.add(PdfFile(file.name, file.absolutePath))
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private val manageFileStorage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            loadFiles()
        }

    private val storagePermissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                loadFiles()
            }else{
                Toast.makeText(this, "Permission not Granted!", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }


}