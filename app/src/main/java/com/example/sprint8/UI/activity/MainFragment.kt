package com.example.sprint8.UI.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.sprint8.R
import com.example.sprint8.UI.viewmodel.MainViewModel
import org.koin.android.ext.android.inject


class MainFragment : Fragment() {
    val viewModel: MainViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonSearch = view.findViewById<Button>(R.id.search)

        buttonSearch.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main_box, SearchFragment())
                .addToBackStack(null).commit()
        }

        val buttonMedia = view.findViewById<Button>(R.id.media_library)
        buttonMedia.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main_box, MediaLibraryFragment())
                .addToBackStack(null).commit()
        }

        val buttonSetting = view.findViewById<Button>(R.id.setting)
        buttonSetting.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main_box, SettingsFragment())
                .addToBackStack(null).commit()
        }
    }
}
