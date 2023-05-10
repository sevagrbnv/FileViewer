package com.example.fileviewer.data

import com.example.fileviewer.domain.FileHash
import javax.inject.Inject

class FileHashMapper @Inject constructor() {

    fun mapItemToEntity(fileHash: FileHash) = fileHash.hash?.let {
        FileHashEntity(
            id = fileHash.id,
            absoluteFile = fileHash.toString(),
            hash = fileHash.hash
        )
    }

    private fun mapEntityToItem(fileHashEntity: FileHashEntity) = FileHash(
        id = fileHashEntity.id,
        absoluteFilePath = fileHashEntity.toString(),
        hash = fileHashEntity.hash
    )

    fun mapListEntityToListItem(list: List<FileHashEntity>) = list.map {
        mapEntityToItem(it)
    }
}