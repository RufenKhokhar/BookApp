package com.rkgroup.bookapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rkgroup.bookapp.R
import com.rkgroup.bookapp.databinding.PdfItemBinding
import com.rkgroup.bookapp.model.DatabasePdfFile
import com.rkgroup.bookapp.model.toFileInfo


class DatabaseFilesViewAdapter(private val fileClickListener:(databaseFile:DatabasePdfFile)->Unit) :
    RecyclerView.Adapter<DatabaseFilesViewAdapter.FileViewHolder>() {
    private val pdfFiles: ArrayList<DatabasePdfFile> = ArrayList()

    fun updateAdapter(pdfFiles: List<DatabasePdfFile>) {
        val adapterUtils = AdapterUtils(this.pdfFiles, pdfFiles)
        val calculateDiff = DiffUtil.calculateDiff(adapterUtils)
        this.pdfFiles.clear()
        this.pdfFiles.addAll(pdfFiles)
        calculateDiff.dispatchUpdatesTo(this)
    }


    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PdfItemBinding.bind(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pdf_item, parent, false)
        return FileViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val pdfFile = pdfFiles[position]
        Glide.with(holder.itemView).load(pdfFile.bookThumbnail)
            .into(holder.binding.ivThumbnail)
        holder.binding.tvFileName.isSelected = true
        holder.binding.tvFileName.text = pdfFile.bookName
        holder.binding.tvFileInfo.text = pdfFile.toFileInfo()
        holder.itemView.setOnClickListener {
            fileClickListener.invoke(pdfFile)

        }
    }

    override fun getItemCount(): Int = pdfFiles.size
}