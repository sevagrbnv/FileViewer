package com.example.fileviewer.presenation.recView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import com.example.fileviewer.R
import com.example.fileviewer.databinding.ListItemBinding
import com.example.fileviewer.domain.FileHash
import com.example.fileviewer.domain.getReadableSize
import java.io.File

class FileListAdapter : ListAdapter<FileHash, FileViewHolder>(
    FileDiffCallback()
) {
    var onItemClickListener: ((File) -> Unit)? = null
    var onItemShareClickListener: ((File) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileViewHolder(binding)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val item = getItem(position)
        val binding = holder.binding as ListItemBinding

        if (item.isDirectory) binding.kebab.isVisible = false
        else {
            binding.kebab.isVisible = true
            binding.kebab.setOnClickListener {
                val popupMenu = PopupMenu(it.context, it)
                popupMenu.menuInflater.inflate(R.menu.item_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.share -> {
                            onItemShareClickListener?.invoke(item)
                            true
                        }
                        // Добавьте другие пункты меню здесь
                        else -> false
                    }
                }
                popupMenu.show()
            }
        }

        val pic = when (item.format) {
            "jpg" -> R.drawable.jpeg
            "png" -> R.drawable.png
            "pdf" -> R.drawable.png
            "txt" -> R.drawable.txt
            "doc" -> R.drawable.doc
            "" -> R.drawable.folder
            else -> R.drawable.document
        }
        with(binding) {
            formatImg.setImageResource(pic)
            name.text = item.name
            size.text = item.getReadableSize()
            date.text = item.date
            root.setOnClickListener {
                onItemClickListener?.invoke(item)
            }
            if (item.hash != null)
                root.setBackgroundResource(R.color.transparent)
            else root.setBackgroundResource(R.color.new_green)
        }

    }

}