package com.example.fileviewer.domain.useCases

import com.example.fileviewer.domain.DBRepository
import javax.inject.Inject

class CleanDBUseCase @Inject constructor(
    private val repository: DBRepository
) {
    suspend operator fun invoke() = repository.deleteAll()
}