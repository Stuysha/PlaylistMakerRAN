package com.example.sprint8.UI.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sprint8.R
import com.example.sprint8.UI.viewmodel.MediaLibraryViewModel
import org.koin.android.ext.android.inject

class MediaLibraryActivity : AppCompatActivity() {
    private val viewModel: MediaLibraryViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_libraryactivity)

    }
}