package com.rkgroup.bookapp.adapter

import androidx.recyclerview.widget.DiffUtil

class AdapterUtils(private val oldFiles: List<Any>, private val newFiles: List<Any>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFiles.size

    override fun getNewListSize(): Int = newFiles.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItemPosition == newItemPosition

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldFiles[oldItemPosition] == newFiles[newItemPosition]
}