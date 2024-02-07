package com.example.sprint8.UI.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sprint8.R
import com.example.sprint8.UI.adapters.SearchMediaAdapter
import com.example.sprint8.UI.fragments.SearchFragment.Companion.TRACK
import com.example.sprint8.UI.viewmodel.FavoritesTracksViewModel
import com.example.sprint8.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FavoritesTracksFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesTracksFragment()
    }

    private val viewModel: FavoritesTracksViewModel by inject()
    var mediaList: RecyclerView? = null
    val adapter: SearchMediaAdapter = SearchMediaAdapter()
    var noContentBox: FrameLayout? = null
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites_tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaList = view.findViewById(R.id.media_list)
        noContentBox = view.findViewById(R.id.nocontent)
        mediaList?.adapter = adapter
        mediaList?.layoutManager = LinearLayoutManager(context)

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
            if (!it.isLoadData) return@observe
            if (it.listTrack.isNullOrEmpty()) {
                noContentBox?.visibility = View.VISIBLE
                mediaList?.visibility = View.GONE
            } else {
                noContentBox?.visibility = View.GONE
                mediaList?.visibility = View.VISIBLE
                adapter.setItems(it.listTrack)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.click = ::clickToItem
        viewModel.updateFavoriteTracks()
    }

    fun clickToItem(track: Track) {
        adapter.click = null
        if (clickDebounce()) {
            val intent = Intent(context, MediaFragment::class.java)
            intent.putExtra(TRACK, Gson().toJson(track))
            startActivity(intent)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch(Dispatchers.IO) {
                delay(SearchFragment.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }

        }
        return current
    }
}