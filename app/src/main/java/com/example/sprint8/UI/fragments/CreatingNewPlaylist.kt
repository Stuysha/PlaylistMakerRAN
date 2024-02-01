package com.example.sprint8.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sprint8.R
import com.example.sprint8.UI.viewmodel.PlaylistsViewModel
import org.koin.android.ext.android.inject

class CreatingNewPlaylist : Fragment() {

    companion object {

        fun newInstance() = CreatingNewPlaylist()
    }

    private val viewModel: PlaylistsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.creating_new_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}