package com.example.fileviewer.presenation

import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fileviewer.domain.*
import com.example.fileviewer.domain.useCases.CleanDBUseCase
import com.example.fileviewer.domain.useCases.GetHashListUseCase
import com.example.fileviewer.domain.useCases.SaveHashesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveHashesUseCase: SaveHashesUseCase,
    private val cleanDBUseCase: CleanDBUseCase,
    private val getHashListUseCase: GetHashListUseCase
) : ViewModel() {

    private val _filesList = MutableLiveData<List<FileHash>?>()
    val filesList: LiveData<List<FileHash>?> = _filesList

    private val _isRootDir = MutableLiveData(true)
    val isRootDir: LiveData<Boolean> = _isRootDir

    private lateinit var hashes: List<Int?>

    private var stack = Stack<File>()

    init {
        stack.add(ROOT_DIR)
        viewModelScope.launch {
            launch (Dispatchers.Default){
                hashes = getHashListUseCase().map { it.hash }
                loadFiles()
            }.join()

            launch (Dispatchers.IO) {
                updateDB()
            }
        }
    }

    fun pushOnStack(file: File) {
        stack.push(file)
        _isRootDir.value = false
        loadFiles()
    }

    fun popStack() {
        stack.pop()
        if (stack.peek() == ROOT_DIR)
            _isRootDir.value = true
        loadFiles()
    }

    fun getCurrentDir() = stack.peek().absoluteFile.toString()

    private fun loadFiles() = viewModelScope.launch {
        val list = getFilesList()

        val fileHashList = list.map {
            FileHash(
                size = it.getReadableSize(),
                absoluteFilePath = it.absolutePath,
                format = it.getFormat(),
                date = it.formatDate(),
                hash = hashes.find { fileHash -> fileHash == it.hashCode() }
            )
        }
        filterList(filesList = fileHashList)
    }

    fun filterList(
        filesList: List<FileHash>? = _filesList.value,
        filterType: FILTER = FILTER.NAME,
        orderByDesc: Boolean = false
    ) {
        val files = filesList
        var filterFiles = when (filterType) {
            FILTER.NAME -> {
                files?.sortedBy { it.name }
            }
            FILTER.DATE -> {
                files?.sortedBy { it.date }
            }
            FILTER.SIZE -> {
                files?.sortedBy { it.length() }
            }
            FILTER.FORMAT -> {
                files?.sortedBy { it.format }
            }
        }

        if (orderByDesc) filterFiles = filterFiles?.reversed()

        _filesList.value = filterFiles
    }

    private suspend fun getFilesList(): List<File> {
        return withContext(Dispatchers.IO) {
            val root = stack.peek()
            val fileList = mutableListOf<File>()
            getFileList(root, fileList)
            fileList
        }
    }

    private suspend fun updateDB() {
        cleanDBUseCase()
        val list = getAllFilesAndFolders(ROOT_DIR)
        saveHashesUseCase(list.map {
            FileHash(
                absoluteFilePath = it.absoluteFile.toString(),
                hash = it.hashCode()
            )
        })
    }

    private fun getFileList(dir: File, fileList: MutableList<File>) =
        dir.listFiles()?.forEach { fileList.add(it) }

    private fun getAllFilesAndFolders(directory: File): List<File> {
        val fileList = mutableListOf<File>()
        directory.listFiles()?.forEach {
            fileList.add(it)
            if (it.isDirectory)
                fileList.addAll(getAllFilesAndFolders(it))
        }
        return fileList
    }

    companion object {
        private val ROOT_DIR = Environment.getExternalStorageDirectory()
    }
}

enum class FILTER {
    NAME,
    DATE,
    SIZE,
    FORMAT
}