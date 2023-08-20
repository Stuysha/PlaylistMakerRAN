package com.example.sprint8.UI.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sprint8.R
import com.example.sprint8.UI.viewmodel.MediaLibraryViewModel

class MediaLibraryActivity : AppCompatActivity() {
    private lateinit var viewModel: MediaLibraryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_libraryactivity)
        viewModel = ViewModelProvider(
            this,
            MediaLibraryViewModel.getViewModelFactory()
        )[MediaLibraryViewModel::class.java]
    }
}