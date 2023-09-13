package com.example.sprint8.UI.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.sprint8.R
import com.example.sprint8.UI.viewmodel.MainViewModel
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {
     val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch = findViewById<Button>(R.id.search)
        val imageClickListener: View.OnClickListener = View.OnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }
        buttonSearch.setOnClickListener(imageClickListener)

        val buttonMedia = findViewById<Button>(R.id.media_library)

        buttonMedia.setOnClickListener {
            val intent = Intent(this@MainActivity, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
        val buttonSetting = findViewById<Button>(R.id.setting)

        buttonSetting.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }


    }


}
