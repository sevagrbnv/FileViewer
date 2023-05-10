package com.example.fileviewer.domain.useCases

import com.example.fileviewer.domain.DBRepository
import com.example.fileviewer.domain.FileHash
import javax.inject.Inject

class SaveHashesUseCase @Inject constructor(
    private val repository: DBRepository
) {
    suspend operator fun invoke(list: List<FileHash>) =
        list.forEach {
            repository.addItem(it)
        }
}