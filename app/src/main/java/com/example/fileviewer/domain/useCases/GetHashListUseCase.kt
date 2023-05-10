package com.example.fileviewer.domain.useCases

import com.example.fileviewer.domain.DBRepository
import com.example.fileviewer.domain.FileHash
import javax.inject.Inject

class GetHashListUseCase @Inject constructor(
    private val repository: DBRepository
) {
    suspend operator fun invoke(): List<FileHash> {
        return repository.getItemList()
    }
}