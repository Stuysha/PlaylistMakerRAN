package com.example.sprint8.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sprint8.R
import com.example.sprint8.UI.viewmodel.FavoritesTracksViewModel
import org.koin.android.ext.android.inject

class FavoritesTracksFragment : Fragment() {

    private val viewModel: FavoritesTracksViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites_tracks, container, false)
    }

}