package com.example.sprint8.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sprint8.R
import com.example.sprint8.UI.adapters.SearchMediaAdapter
import com.example.sprint8.UI.viewmodel.PlayListViewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import java.io.File

class PlayListFragment : Fragment() {
    private lateinit var viewModel: PlayListViewModel

    companion object {
        const val ID_PLAY_LIST = "ID_PLAY_LIST"
    }

    val adapter = SearchMediaAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idPlaylist = arguments?.getLong(ID_PLAY_LIST)
        viewModel = KoinJavaComponent.getKoin().get(parameters = { parametersOf(idPlaylist) })
        val image = view.findViewById<ImageView>(R.id.imageCover)
        val playlistName = view.findViewById<TextView>(R.id.playlist_name)
        val info = view.findViewById<TextView>(R.id.info)
        val tracksInfo = view.findViewById<TextView>(R.id.time)
        val countTrack = view.findViewById<TextView>(R.id.count_track)
        val share = view.findViewById<ImageView>(R.id.share)
        val menu = view.findViewById<ImageView>(R.id.menu)
        val toolbar = view.findViewById<Toolbar>(R.id.arrow)
        val listTracks = view.findViewById<RecyclerView>(R.id.track_list)

        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        listTracks.adapter = adapter
        listTracks.layoutManager = LinearLayoutManager(context)

        viewModel.statePlayList.observe(viewLifecycleOwner) { state ->
            val it = state.first
            it.picture?.let {
                Glide.with(this)
                    .load(File(it))
                    .placeholder(R.drawable.placeholder)
                    .into(image)
            }

            playlistName.text = it.name
            info.text = it.description
            tracksInfo.text = "${state.second} минут"
            countTrack.text = "${it.trackSize} треков"
        }

        viewModel.stateTracks.observe(viewLifecycleOwner) {
            adapter.setItems(it)
        }
    }
}