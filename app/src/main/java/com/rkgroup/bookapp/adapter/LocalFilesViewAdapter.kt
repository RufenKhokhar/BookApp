package com.rkgroup.bookapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rkgroup.bookapp.R
import com.rkgroup.bookapp.databinding.PdfItemBinding
import com.rkgroup.bookapp.model.PdfFile
import com.rkgroup.bookapp.model.toFileInfo
import com.rkgroup.bookapp.ui.PdfViewActivity
import com.rkgroup.bookapp.utils.Constants

class LocalFilesViewAdapter(private val pdfFiles: ArrayList<PdfFile>) :
    RecyclerView.Adapter<LocalFilesViewAdapter.FileViewHolder>() {


    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PdfItemBinding.bind(itemView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pdf_item, parent, false)
        return FileViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val pdfFile = pdfFiles[position]
        Glide.with(holder.itemView).load(pdfFile)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(holder.binding.ivThumbnail)
        holder.binding.tvFileName.text = pdfFile.bookName
        holder.binding.tvFileName.isSelected = true
        holder.binding.tvFileInfo.text = pdfFile.toFileInfo()
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PdfViewActivity::class.java)
            intent.putExtra(Constants.KEY_SOURCE_FILE, pdfFile)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = pdfFiles.size
}