package com.example.fileviewer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file")
data class FileHashEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val absoluteFile: String,
    val hash: Int
)