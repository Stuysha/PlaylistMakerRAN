package com.example.sprint8.UI.activity


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.sprint8.App
import com.example.sprint8.R
import com.example.sprint8.UI.viewmodel.SettingsViewModel
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {
    private val viewModel: SettingsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<Toolbar>(R.id.arrow)
        toolbar.setOnClickListener {
            activity?.onBackPressed()
        }
        val buttonShareApp = view.findViewById<FrameLayout>(R.id.share_app)
        buttonShareApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT, getString(R.string.share_app_step2)
            )
            startActivity(intent)
        }
        val buttonWriteSupport = view.findViewById<FrameLayout>(R.id.write_support)
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

        val buttonUserAgreement = view.findViewById<FrameLayout>(R.id.uUser_agreement)
        buttonUserAgreement.setOnClickListener {
            val webesite = getString(R.string.webesite)
            val webpage: Uri = Uri.parse(webesite)
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(intent)
        }
        val themeSwitcher = view.findViewById<SwitchCompat>(R.id.switch1)
        themeSwitcher.isChecked = (activity?.applicationContext as App).darkTheme
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.editEnableDarkThemeSetting(checked)
        }
        //TODO проверить
        viewModel.getActiveDarkTheme().observe(this.viewLifecycleOwner) {
            if (it != null) {
                (activity?.applicationContext as App).switchTheme(it)
            }
        }
    }

}

