package com.example.sprint8.UI.activity


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.sprint8.App
import com.example.sprint8.R
import com.example.sprint8.UI.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_screen)
        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]
        val toolbar = findViewById<Toolbar>(R.id.arrow)
        toolbar.setOnClickListener {
            onBackPressed()
        }
        val buttonShareApp = findViewById<FrameLayout>(R.id.share_app)
        buttonShareApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT, getString(R.string.share_app_step2)
            )
            startActivity(intent)
        }
        val buttonWriteSupport = findViewById<FrameLayout>(R.id.write_support)
        buttonWriteSupport.setOnClickListener {
            val message = getString(R.string.text_message)
            val topic = getString(R.string.topic_message)
            val email = getString(R.string.e_mail)
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, topic)
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(intent)
        }

        val buttonUserAgreement = findViewById<FrameLayout>(R.id.uUser_agreement)
        buttonUserAgreement.setOnClickListener {
            val webesite = getString(R.string.webesite)
            val webpage: Uri = Uri.parse(webesite)
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(intent)
        }
        val themeSwitcher = findViewById<SwitchCompat>(R.id.switch1)
        themeSwitcher.isChecked = (applicationContext as App).darkTheme
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.editEnableDarkThemeSetting(checked)
        }

        viewModel.getActiveDarkTheme().observe(this) {
            if (it != null) {
                (applicationContext as App).switchTheme(it)
            }
        }
    }

}

