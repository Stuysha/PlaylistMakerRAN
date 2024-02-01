package com.example.sprint8.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sprint8.R
import com.example.sprint8.UI.viewmodel.PlaylistsViewModel
import org.koin.android.ext.android.inject

class PlaylistsFragment : Fragment() {

    companion object {

        fun newInstance() = PlaylistsFragment()
    }

    private val viewModel: PlaylistsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playlists, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newButtonPlaylist = view.findViewById<Button>(R.id.button_apd)
        newButtonPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_creatingNewPlaylist,
                //bundleOf(TRACK to Gson().toJson(track))
            )
        }
    }
}