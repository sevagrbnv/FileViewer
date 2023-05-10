package com.example.fileviewer.presenation.recView

import androidx.recyclerview.widget.DiffUtil
import com.example.fileviewer.domain.FileHash

class FileDiffCallback : DiffUtil.ItemCallback<FileHash>() {

    override fun areItemsTheSame(oldItem: FileHash, newItem: FileHash): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: FileHash, newItem: FileHash): Boolean {
        return oldItem == newItem
    }
}