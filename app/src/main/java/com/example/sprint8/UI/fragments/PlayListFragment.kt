package com.example.sprint8.UI.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sprint8.R
import com.example.sprint8.UI.adapters.SearchMediaAdapter
import com.example.sprint8.UI.fragments.SearchFragment.Companion.TRACK
import com.example.sprint8.UI.viewmodel.PlayListViewModel
import com.example.sprint8.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class PlayListFragment : Fragment() {
    //    private lateinit var viewModel: PlayListViewModel
    private val viewModel: PlayListViewModel by viewModel {
        parametersOf(arguments?.getLong(ID_PLAY_LIST) ?: Long.MIN_VALUE)
    }

    companion object {
        const val ID_PLAY_LIST = "ID_PLAY_LIST"
    }

    private val adapter = SearchMediaAdapter()

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
//        viewModel = KoinJavaComponent.getKoin().get(parameters = { parametersOf(idPlaylist) })

        val image = view.findViewById<ImageView>(R.id.imageCover)
        val playlistName = view.findViewById<TextView>(R.id.playlist_name)
        val info = view.findViewById<TextView>(R.id.info)
        val tracksInfo = view.findViewById<TextView>(R.id.time)
        val countTrack = view.findViewById<TextView>(R.id.count_track)
        val share = view.findViewById<ImageView>(R.id.share)
        val menu = view.findViewById<ImageView>(R.id.menu)
        val toolbar = view.findViewById<ImageView>(R.id.arrow)
        val listTracks = view.findViewById<RecyclerView>(R.id.track_list)
        val noTracks = view.findViewById<TextView>(R.id.no_tracks)
        val menuShare = view.findViewById<TextView>(R.id.menu_share)
        val menuEdit = view.findViewById<TextView>(R.id.menu_edit)
        val menuDelete = view.findViewById<TextView>(R.id.menu_delete)
        val bottomSheetMenu = view.findViewById<LinearLayout>(R.id.bottom_sheet_menu)
        val overlay = view.findViewById<View>(R.id.overlay)


        val viewHolder = view.findViewById<ConstraintLayout>(R.id.view_holder)
        val imageViewHolder = viewHolder.findViewById<ImageView>(R.id.image)
        val titleViewHolder = viewHolder.findViewById<TextView>(R.id.title)
        val infoViewHolder = viewHolder.findViewById<TextView>(R.id.size_tracks)


        val bottomSheetMenuBehavior = BottomSheetBehavior.from(bottomSheetMenu)
        bottomSheetMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetMenuBehavior.addBottomSheetCallback(
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

        (imageViewHolder.layoutParams as? ConstraintLayout.LayoutParams)?.leftMargin = 0

        toolbar.setOnClickListener {
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

                Glide.with(this)
                    .load(File(it))
                    .placeholder(R.drawable.placeholder)
                    .into(imageViewHolder)
            }

            playlistName.text = it.name
            titleViewHolder.text = it.name

            if (it.description.isNullOrEmpty()) info.visibility = View.GONE
            else info.visibility = View.VISIBLE
            info.text = it.description
            tracksInfo.text = "${state.second} минут"
            countTrack.text = "${it.trackSize} треков"
            infoViewHolder.text = "${it.trackSize} треков"
            if ((it.trackSize ?: 0) == 0) noTracks.visibility = View.VISIBLE
            else noTracks.visibility = View.GONE
        }

        viewModel.stateTracks.observe(viewLifecycleOwner) {
            adapter.setItems(it)
        }

        adapter.click = {
            findNavController().navigate(
                R.id.action_playListFragment_to_mediaFragment,
                bundleOf(TRACK to Gson().toJson(it))
            )
        }

        adapter.longClick = {
            showDeleteTrackDialog(it)
        }

        share.setOnClickListener { share(bottomSheetMenuBehavior) }

        menu.setOnClickListener {
            bottomSheetMenuBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        menuShare.setOnClickListener { share(bottomSheetMenuBehavior) }
        menuEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_playListFragment_to_creatingNewPlaylist,
                bundleOf(ID_PLAY_LIST to idPlaylist)
            )
        }
        menuDelete.setOnClickListener {
            bottomSheetMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            showDeletePlayListDialog()
        }
    }

    fun showDeleteTrackDialog(track: Track) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Удалить трек")
                .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
                .setNegativeButton("Отмена") { dialog, which ->
                }
                .setPositiveButton("Удалить") { dialog, which ->
                    viewModel.deleteTrackFromPlaylist(track.trackId)
                }
                .show()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadData()
    }

    fun showDeletePlayListDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Удалить плейлист")
                .setMessage("Хотите удалить плейлист?")
                .setNegativeButton("Нет") { dialog, which ->
                }
                .setPositiveButton("Да") { dialog, which ->
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.deletePlayList()
                        withContext(Dispatchers.Main) {
                            findNavController().popBackStack()
                        }
                    }
                }
                .show()
        }
    }

    fun share(bottomSheetMenuBehavior: BottomSheetBehavior<LinearLayout>) {
        bottomSheetMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        if (viewModel.isNotEmptyTracks()) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                viewModel.createStringShared()
            )
            startActivity(intent)
        } else {
            Toast.makeText(
                context,
                "В этом плейлисте нет списка треков, которым можно поделиться",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}