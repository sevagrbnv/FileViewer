package com.example.fileviewer.data

import com.example.fileviewer.data.db.FileHashDao
import com.example.fileviewer.domain.DBRepository
import com.example.fileviewer.domain.FileHash
import javax.inject.Inject

class DBRepositoryImpl @Inject constructor(
    private val dao: FileHashDao,
    private val mapper: FileHashMapper
) : DBRepository {

    override suspend fun addItem(item: FileHash) {
        mapper.mapItemToEntity(item)?.let { dao.addFileHash(it) }
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun getItemList(): List<FileHash> =
        mapper.mapListEntityToListItem(dao.getHashFileList())
}