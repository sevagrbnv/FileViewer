package com.example.fileviewer.domain

interface DBRepository {

    suspend fun addItem(item: FileHash)

    suspend fun deleteAll()

    suspend fun getItemList(): List<FileHash>
}