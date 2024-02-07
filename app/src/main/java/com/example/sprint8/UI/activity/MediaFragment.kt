package com.example.sprint8.UI.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.sprint8.R
import com.example.sprint8.UI.adapters.MediaPlayListAdapter
import com.example.sprint8.UI.fragments.SearchFragment.Companion.TRACK
import com.example.sprint8.UI.viewmodel.MediaViewModel
import com.example.sprint8.UI.viewmodel.StateMediaPlayer
import com.example.sprint8.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.getKoin

class MediaFragment : Fragment() {
    private lateinit var viewModel: MediaViewModel
    var cover: ImageView? = null
    var trackName: TextView? = null
    var artistName: TextView? = null
    var timeTrack: TextView? = null
    var collectionName: TextView? = null
    var releaseDate: TextView? = null
    var primaryGenreName: TextView? = null
    var country: TextView? = null
    var trackTimeMills: TextView? = null
    var addInPlaylist: ImageView? = null
    var playback: ImageView? = null
    var likeTrack: ImageView? = null
    val mediaPlayListAdapter = MediaPlayListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val trackJson = arguments?.getString(TRACK)
        val trackObject = Gson().fromJson(trackJson, Track::class.java)
        viewModel = getKoin().get(parameters = { parametersOf(trackObject) })
        val toolbar = view.findViewById<Toolbar>(R.id.arrow)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        cover = view.findViewById(R.id.imageCover)
        trackName = view.findViewById(R.id.trackName)
        artistName = view.findViewById(R.id.artistName)
        timeTrack = view.findViewById(R.id.timeTrack)
        collectionName = view.findViewById(R.id.collectionName)
        releaseDate = view.findViewById(R.id.releaseDate)
        primaryGenreName = view.findViewById(R.id.primaryGenreName)
        country = view.findViewById(R.id.country)
        trackTimeMills = view.findViewById(R.id.trackTimeMills)
        addInPlaylist = view.findViewById(R.id.addInPlaylist)
        playback = view.findViewById(R.id.playback)
        likeTrack = view.findViewById(R.id.likeTrack)
        val listPlyList = view.findViewById<RecyclerView>(R.id.list_ply_list)
        listPlyList.adapter = mediaPlayListAdapter
        listPlyList.layoutManager = LinearLayoutManager(context)

        timeTrack?.setText(R.string.null_time)
        playback?.isEnabled = false

        playback?.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.getStateLiveData().observe(viewLifecycleOwner) {
            when (it.playerState) {
                StateMediaPlayer.STATE_DEFAULT -> {
                }

                StateMediaPlayer.STATE_PREPARED -> {
                    playback?.isEnabled = true
                    setPlayView()
                }

                StateMediaPlayer.STATE_PLAYING -> startPlayer()
                StateMediaPlayer.STATE_PAUSED -> pausePlayer()
            }
        }
        viewModel.getTimeTrack().observe(viewLifecycleOwner) {
            timeTrack?.text = it.orEmpty()
        }
        viewModel.getStaticContentMedia().observe(viewLifecycleOwner) {
            releaseDate?.text = it.dateTrack ?: getString(R.string.is_unknown).lowercase()
            trackName?.text = it.trackName
            artistName?.text = it.artistName
            collectionName?.text = it.collectionName

            primaryGenreName?.text = it.primaryGenreName
            country?.text = it.country
            trackTimeMills?.text = it.trackTime
            if (it.isFavoriteTrack) setActiveFavoriteView()
            else setNoActiveFavoriteView()
            val round = this.resources.getDimensionPixelSize(R.dimen.round_image_media8)
            Glide.with(this)
                .load(it.artTrack)
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(round))
                .into(cover ?: return@observe)
        }

        likeTrack?.setOnClickListener {
            viewModel.controlFavoriteTrack()
        }
        val bottomSheetContainer = view.findViewById<LinearLayout>(R.id.standard_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        addInPlaylist?.setOnClickListener {
            viewModel.getListPlayList()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        viewModel.stateList.observe(viewLifecycleOwner) {
            mediaPlayListAdapter.setItems(it)
        }
        mediaPlayListAdapter.click = {
            viewModel.newTracksToPlayList(
                idPlaylist = it.id,
                idTrack = trackObject.trackId,
                playlistName = it.name ?: ""
            )
        }
        viewModel.stateMessageAddPlayList.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(context, it.first, Toast.LENGTH_LONG).show()
                if (it.second) bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                viewModel.clearMessageAddPlayList()
            }
        }
        val overlay = view.findViewById<View>(R.id.overlay)
        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {

                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            overlay.visibility = View.GONE
                        }

                        else -> {
                            overlay.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })

        view.findViewById<Button>(R.id.button_apd).setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_creatingNewPlaylist)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.mediaPlayerRelease()
    }

    fun startPlayer() {
        setViewPause()
    }

    fun pausePlayer() {
        setPlayView()
    }

    fun setPlayView() {
        playback?.setImageDrawable(
            context?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.play
                )
            }
        )
    }

    fun setViewPause() {
        playback?.setImageDrawable(
            context?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.pause
                )
            }
        )
    }

    fun setActiveFavoriteView() {
        likeTrack?.setImageDrawable(
            context?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.favorite_track
                )
            }
        )
    }

    fun setNoActiveFavoriteView() {
        likeTrack?.setImageDrawable(
            context?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.like
                )
            }
        )
    }


}