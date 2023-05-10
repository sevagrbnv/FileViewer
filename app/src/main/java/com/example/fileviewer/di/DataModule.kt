package com.example.fileviewer.di

import android.content.Context
import androidx.room.Room
import com.example.fileviewer.data.db.AppDatabase
import com.example.fileviewer.data.DBRepositoryImpl
import com.example.fileviewer.data.db.FileHashDao
import com.example.fileviewer.data.FileHashMapper
import com.example.fileviewer.domain.DBRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRepository(
        dao: FileHashDao,
        mapper: FileHashMapper
    ): DBRepository {
        return DBRepositoryImpl(
            dao = dao,
            mapper = mapper
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app.db"
        ).build()

    @Provides
    fun provideDao(appDatabase: AppDatabase): FileHashDao {
        return appDatabase.fileHashDao()
    }
}