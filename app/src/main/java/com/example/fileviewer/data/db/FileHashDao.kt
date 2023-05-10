package com.example.fileviewer.data.db

import androidx.room.*
import com.example.fileviewer.data.FileHashEntity

@Dao
interface FileHashDao {

    @Insert(entity = FileHashEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFileHash(fileHash: FileHashEntity)

    @Query("DELETE FROM file")
    suspend fun deleteAll()

    @Query("SELECT * FROM file")
    suspend fun getHashFileList(): List<FileHashEntity>
}