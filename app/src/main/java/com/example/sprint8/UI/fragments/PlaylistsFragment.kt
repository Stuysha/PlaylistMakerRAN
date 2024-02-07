package com.example.sprint8.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sprint8.R
import com.example.sprint8.UI.adapters.PlayListAdapter
import com.example.sprint8.UI.fragments.PlayListFragment.Companion.ID_PLAY_LIST
import com.example.sprint8.UI.viewmodel.PlaylistsViewModel
import org.koin.android.ext.android.inject

class PlaylistsFragment : Fragment() {

    companion object {

        fun newInstance() = PlaylistsFragment()
    }

    private val viewModel: PlaylistsViewModel by inject()
    val adapter = PlayListAdapter()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playlists, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val newButtonPlaylist = view.findViewById<Button>(R.id.button_apd)
        val playlists = view.findViewById<RecyclerView>(R.id.play_lists)
        val nocontent = view.findViewById<ConstraintLayout>(R.id.nocontent)
        newButtonPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_creatingNewPlaylist,
                //bundleOf(TRACK to Gson().toJson(track))
            )
        }


        playlists.adapter = adapter
        playlists.layoutManager = GridLayoutManager(context, 2)
        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            adapter.setItems(it)
            if (it.isEmpty()) nocontent.visibility = View.VISIBLE
            else {
                nocontent.visibility = View.GONE
            }

            adapter.click = {
                findNavController().navigate(
                    R.id.action_mediaLibraryFragment_to_playListFragment,
                    bundleOf(ID_PLAY_LIST to it.id)
                )
            }
        }

        viewModel.getNewPlaylist()
    }

}