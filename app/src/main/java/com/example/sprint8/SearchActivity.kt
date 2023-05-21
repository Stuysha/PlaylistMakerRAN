package com.example.sprint8

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sprint8.adapters.SearchMediaAdapter
import com.example.sprint8.internet.RestProvider
import com.example.sprint8.models.Track
import com.example.sprint8.models.TunesResult
import com.example.sprint8.viewholders.HistoryControl
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SearchActivity : AppCompatActivity() {

    var inputEditText: TextInputEditText? = null
    var inputSearchText = ""
    val adapter: SearchMediaAdapter = SearchMediaAdapter()
    var noContentBox: FrameLayout? = null
    var noInternet: FrameLayout? = null
    var mediaList: RecyclerView? = null
    var clearHistiry: NestedScrollView? = null
    var historyList : RecyclerView? = null
    var progressBar : FrameLayout? = null


    companion object {
        const val SEARCH_TEXT = "searchText"
        const val TRACK = "track"

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.tool_search)
        inputEditText = findViewById(R.id.search)
        val clearButton = findViewById<ImageView>(R.id.close)
        val clearHistory = findViewById<Button>(R.id.clearHistory)
        noContentBox = findViewById<FrameLayout>(R.id.nocontent)
        noInternet = findViewById<FrameLayout>(R.id.nointernet)
        mediaList = findViewById<RecyclerView>(R.id.media_list)
        clearHistiry = findViewById<NestedScrollView>(R.id.searchHistory)
        historyList = findViewById<RecyclerView>(R.id.histiry)
        historyList?.layoutManager = LinearLayoutManager(this)
        progressBar = findViewById<FrameLayout>(R.id.progressBar)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        inputEditText?.setText(inputSearchText)

        clearHistory.setOnClickListener {
            HistoryControl.historyDelete(this)
            setStatusMediaList()
        }

        clearButton.setOnClickListener {
            adapter.setItems(listOf())
            inputEditText?.setText("")

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText?.windowToken, 0)
        }
        val buttonApd = findViewById<Button>(R.id.buttonApd)
        buttonApd.setOnClickListener {
            setStatusMediaList()
            loadSearch()
        }

        clearButton.visibility = View.GONE
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputSearchText = s?.toString() ?: ""
                if (s.isNullOrEmpty()) {
                    handler.removeCallbacks(searchRunnable)
                    clearButton.visibility = View.GONE
                } else {
                    searchDebounce()
                    clearButton.visibility = View.VISIBLE
                }
                if (inputEditText?.hasFocus() == true && s.isNullOrEmpty()) {
                    setHistory()
                } else setStatusMediaList()


            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }

        inputEditText?.addTextChangedListener(simpleTextWatcher)
        inputEditText?.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus == true && inputEditText?.text.isNullOrEmpty()) {
                setHistory()
            } else setStatusMediaList()
        }

        val mediaList = findViewById<RecyclerView>(R.id.media_list)
        mediaList.adapter = adapter
        mediaList.layoutManager = LinearLayoutManager(this)

        adapter.click = {
            HistoryControl.addTrack(it, this)
            val intent = Intent(this, MediaActivity::class.java)
            intent.putExtra(TRACK, Gson().toJson(it))
            startActivity(intent)

        }


        inputEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadSearch()
                true
            }
            false
        }
    }

    fun setHistory() {
        val hihistory = HistoryControl.getHistori(this)
        if (!hihistory.isNullOrEmpty()) {
            setStatusHistory()
            adapter.setItems(hihistory.toList())
        }
    }

    fun loadSearch() {
        setStatusProgressBar()
        RestProvider().api.search(inputEditText?.text?.toString() ?: return).enqueue(
            object : Callback<TunesResult> {

                override fun onResponse(call: Call<TunesResult>, response: Response<TunesResult>) {

                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result == null || result?.results.isNullOrEmpty()) {
                            setStatusNoContent()
                        } else {
                            setStatusMediaList()
                            val tracks = convertToTracks(result)
                            adapter.setItems(tracks)
                        }
                    } else {
                        setStatusNoInternet()
                        val errorJson = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<TunesResult>, t: Throwable) {
                    t.printStackTrace()
                    setStatusNoInternet()
                }
            })
    }

    fun setStatusNoContent() {

        noContentBox?.visibility = View.VISIBLE
        noInternet?.visibility = View.GONE
        mediaList?.visibility = View.GONE
        clearHistiry?.visibility = View.GONE
        progressBar?.visibility = View.GONE
    }

    fun setStatusHistory() {
        clearHistiry?.visibility = View.VISIBLE
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
        clearHistiry?.visibility = View.GONE
        progressBar?.visibility = View.GONE
    }

    fun setStatusMediaList() {
        adapter.setItems(listOf())
        noContentBox?.visibility = View.GONE
        noInternet?.visibility = View.GONE
        mediaList?.visibility = View.VISIBLE
        clearHistiry?.visibility = View.GONE
        progressBar?.visibility = View.GONE
        mediaList?.adapter = adapter
        historyList?.adapter = null
    }
    fun setStatusProgressBar () {
        noContentBox?.visibility = View.GONE
        noInternet?.visibility = View.GONE
        mediaList?.visibility = View.GONE
        clearHistiry?.visibility = View.GONE
        progressBar?.visibility = View.VISIBLE

    }

    fun convertToTracks(tunes: TunesResult): MutableList<Track> {
        val tracList = mutableListOf<Track>()
        tunes.results?.forEach {
            tracList.add(
                Track(
                    trackId = it?.trackId ?: 0L,
                    trackName = it?.trackName ?: "",
                    artistName = it?.artistName ?: "",
                    trackTime = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(it?.trackTimeMillis),
                    artworkUrl100 = it?.artworkUrl100 ?: "" ,
                    collectionName = it?.collectionName ?: "",
                    releaseDate = it?.releaseDate ?: "",
                    primaryGenreName = it?.primaryGenreName ?: "",
                    country = it?.country ?: "",
                    previewUrl = it?.previewUrl ?: "",
                )
            )
        }
        return tracList
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, inputSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchText = savedInstanceState.getString(SEARCH_TEXT) ?: ""
    }
    private val searchRunnable = Runnable { loadSearch() }
    private val handler = Handler(Looper.getMainLooper())
    private fun searchDebounce() {

        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, 2000L)
    }

}

