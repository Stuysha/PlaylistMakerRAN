package com.example.sprint8.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        val navController = findNavController()

        buttonSearch.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_searchFragment)
        }

        val buttonMedia = view.findViewById<Button>(R.id.media_library)
        buttonMedia.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_mediaLibraryFragment)
        }

        val buttonSetting = view.findViewById<Button>(R.id.setting)
        buttonSetting.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_settingsFragment)
        }
    }
}
