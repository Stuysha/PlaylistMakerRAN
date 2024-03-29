package com.example.sprint8.UI.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sprint8.R
import com.example.sprint8.UI.adapters.SearchMediaAdapter
import com.example.sprint8.UI.viewmodel.SearchViewModel
import com.example.sprint8.UI.viewmodel.StateVeiw
import com.example.sprint8.domain.models.Track
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by inject()
    var inputEditText: TextInputEditText? = null
    var inputSearchText = ""
    val adapter: SearchMediaAdapter = SearchMediaAdapter()
    var noContentBox: FrameLayout? = null
    var noInternet: FrameLayout? = null
    var mediaList: RecyclerView? = null
    var clearHistory: NestedScrollView? = null
    var historyList: RecyclerView? = null
    var progressBar: FrameLayout? = null
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputEditText = view.findViewById(R.id.search)
        val clearButton = view.findViewById<ImageView>(R.id.close)
        val clearHistory = view.findViewById<Button>(R.id.clearHistory)
        noContentBox = view.findViewById(R.id.nocontent)
        noInternet = view.findViewById(R.id.nointernet)
        mediaList = view.findViewById(R.id.media_list)
        this.clearHistory = view.findViewById(R.id.searchHistory)
        historyList = view.findViewById(R.id.histiry)
        historyList?.layoutManager = LinearLayoutManager(context)
        progressBar = view.findViewById(R.id.progressBar)

        inputEditText?.setText(inputSearchText)

        clearHistory.setOnClickListener {
            viewModel.historyDelete()
            setStatusMediaList()
        }

        clearButton.setOnClickListener {
            adapter.setItems(listOf())
            inputEditText?.setText("")

            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText?.windowToken, 0)
        }
        val buttonApd = view.findViewById<Button>(R.id.buttonApd)
        buttonApd.setOnClickListener {
            setStatusMediaList()
            viewModel.loadSearch(inputEditText?.text?.toString() ?: "")
        }

        clearButton.visibility = View.GONE
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputSearchText = s?.toString() ?: ""
                if (s.isNullOrEmpty()) {
                    job?.cancel()
                    clearButton.visibility = View.GONE
                } else {
                    searchDebounce()
                    clearButton.visibility = View.VISIBLE
                }
                if (inputEditText?.hasFocus() == true && s.isNullOrEmpty()) {
                    viewModel.setHistory()
                } else setStatusMediaList()


            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }

        inputEditText?.addTextChangedListener(simpleTextWatcher)
        inputEditText?.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputEditText?.text.isNullOrEmpty()) {
                viewModel.setHistory()
            } else setStatusMediaList()
        }

        val mediaList = view.findViewById<RecyclerView>(R.id.media_list)
        mediaList.adapter = adapter
        mediaList.layoutManager = LinearLayoutManager(context)

        inputEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.loadSearch(inputEditText?.text?.toString() ?: "")
                true
            }
            false
        }
        viewModel.getStateLiveData().observe(viewLifecycleOwner) { stateView ->
            stateView.stateVeiw

            when (stateView.stateVeiw) {
                StateVeiw.IN_PROGRESS -> setStatusProgressBar()
                StateVeiw.NO_INTERNET -> setStatusNoInternet()
                StateVeiw.NO_CONTENT -> setStatusNoContent()
                StateVeiw.SHOW_CONTENT -> {
                    setStatusMediaList()
                    adapter.setItems(stateView.listTrack ?: listOf())
                }

                StateVeiw.SHOW_HISTORY -> {
                    setStatusHistory()
                    adapter.setItems(stateView.listTrack ?: listOf())
                }

                StateVeiw.EMPTY_VIEW -> setStatusEmptyContent()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.click = ::clickToItem
    }

    fun clickToItem(track: Track) {
        adapter.click = null
        if (clickDebounce()) {
            viewModel.saveHistoryTrack(track)
            findNavController().navigate(
                R.id.action_searchFragment_to_mediaFragment,
                bundleOf(TRACK to Gson().toJson(track))
            )
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch() {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }

        }
        return current
    }

    fun setStatusEmptyContent() {
        noContentBox?.visibility = View.GONE
        noInternet?.visibility = View.GONE
        mediaList?.visibility = View.GONE
        clearHistory?.visibility = View.GONE
        progressBar?.visibility = View.GONE
    }

    fun setStatusNoContent() {

        noContentBox?.visibility = View.VISIBLE
        noInternet?.visibility = View.GONE
        mediaList?.visibility = View.GONE
        clearHistory?.visibility = View.GONE
        progressBar?.visibility = View.GONE
    }

    fun setStatusHistory() {
        clearHistory?.visibility = View.VISIBLE
        noContentBox?.visibility = View.GONE
        noInternet?.visibility = View.GONE
        mediaList?.visibility = View.GONE
        progressBar?.visibility = View.GONE
        mediaList?.adapter = null
        historyList?.adapter = adapter
    }

    fun setStatusNoInternet() {
        noContentBox?.visibility = View.GONE
        noInternet?.visibility = View.VISIBLE
        mediaList?.visibility = View.GONE
        clearHistory?.visibility = View.GONE
        progressBar?.visibility = View.GONE
    }

    fun setStatusMediaList() {
        adapter.setItems(listOf())
        noContentBox?.visibility = View.GONE
        noInternet?.visibility = View.GONE
        mediaList?.visibility = View.VISIBLE
        clearHistory?.visibility = View.GONE
        progressBar?.visibility = View.GONE
        mediaList?.adapter = adapter
        historyList?.adapter = null
    }

    fun setStatusProgressBar() {
        noContentBox?.visibility = View.GONE
        noInternet?.visibility = View.GONE
        mediaList?.visibility = View.GONE
        clearHistory?.visibility = View.GONE
        progressBar?.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, inputSearchText)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        inputSearchText = savedInstanceState?.getString(SEARCH_TEXT) ?: ""
    }


    var job: Job? = null
    private fun searchDebounce() {
        job?.cancel()
        job = lifecycleScope.launch() {
            delay(2000)
            viewModel.loadSearch(inputEditText?.text?.toString() ?: "")

        }

    }


    companion object {
        const val SEARCH_TEXT = "searchText"
        const val TRACK = "track"
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}

