package com.example.fileviewer.presenation

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.example.fileviewer.BuildConfig
import com.example.fileviewer.R
import com.example.fileviewer.databinding.ActivityMainBinding
import com.example.fileviewer.presenation.recView.FileListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var fileListAdapter: FileListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!hasPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(READ_EXTERNAL_STORAGE),
                PERMISSION_CODE
            )
        } else {
            setRcViewAdapter()
            observeViewModel()
        }
    }

    private fun hasPermission() = ContextCompat.checkSelfPermission(
        this,
        READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun observeViewModel() {
        viewModel.filesList.observe(this) {
            binding.currentDir.text = viewModel.getCurrentDir()
            fileListAdapter.submitList(it)
            binding.progressBar.isVisible = false
            binding.recView.isVisible = true
            setStartPosition()
        }

        viewModel.isRootDir.observe(this) {
            supportActionBar?.setDisplayHomeAsUpEnabled(!it)
        }
    }

    private fun setRcViewAdapter() {
        fileListAdapter = FileListAdapter()
        binding.recView.adapter = fileListAdapter
        fileListAdapter.onItemClickListener = {
            if (it.isDirectory)
                viewModel.pushOnStack(it.absoluteFile)
            else {
                val intent = Intent(Intent.ACTION_VIEW)
                val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, it)
                intent.setDataAndType(uri, "*/*")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val pm: PackageManager = this.packageManager
                if (intent.resolveActivity(pm) != null)
                    startActivity(intent)
            }
        }
        fileListAdapter.onItemShareClickListener = {
            val intent = Intent(Intent.ACTION_SEND)
            val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, it)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(intent, "Share file"))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_name_asc -> {
                viewModel.filterList(filterType = FILTER.NAME)
                true
            }
            R.id.sort_name_desc -> {
                viewModel.filterList(filterType = FILTER.NAME, orderByDesc = true)
                true
            }
            R.id.sort_date_asc -> {
                viewModel.filterList(filterType = FILTER.DATE)
                true
            }
            R.id.sort_date_desc -> {
                viewModel.filterList(filterType = FILTER.DATE, orderByDesc = true)
                true
            }

            R.id.sort_size_asc -> {
                viewModel.filterList(filterType = FILTER.SIZE)
                true
            }
            R.id.sort_size_desc -> {
                viewModel.filterList(filterType = FILTER.SIZE, orderByDesc = true)
                true
            }

            R.id.sort_format_asc -> {
                viewModel.filterList(filterType = FILTER.FORMAT)
                true
            }
            R.id.sort_format_desc -> {
                viewModel.filterList(filterType = FILTER.FORMAT, orderByDesc = true)
                true
            }
            android.R.id.home -> {
                viewModel.popStack()
                true
            }

            else -> super.onOptionsItemSelected(item)

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setRcViewAdapter()
                    observeViewModel()
                }
                return
            }
        }
    }

    private fun setStartPosition() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.recView.scrollToPosition(0)
        }, 300)
    }

    override fun onBackPressed() = if (viewModel.isRootDir.value == true)
        finish()
    else viewModel.popStack()

    companion object {
        private const val PERMISSION_CODE = 200
    }
}