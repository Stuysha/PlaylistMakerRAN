package com.example.sprint8

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sprint8.adapters.SearchMediaAdapter
import com.example.sprint8.internet.RestProvider
import com.example.sprint8.models.Track
import com.example.sprint8.models.TunesResult
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SearchActivity : AppCompatActivity() {

    var inputEditText: TextInputEditText? = null
    var inputSearchText = ""
    val adapter: SearchMediaAdapter = SearchMediaAdapter()

    companion object {
        const val SEARCH_TEXT = "searchText"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.tool_search)
        inputEditText = findViewById(R.id.search)
        val clearButton = findViewById<ImageView>(R.id.close)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        inputEditText?.setText(inputSearchText)

        clearButton.setOnClickListener {
            inputEditText?.setText("")
            adapter.setItems(listOf())
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
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        }

        inputEditText?.addTextChangedListener(simpleTextWatcher)

        val mediaList = findViewById<RecyclerView>(R.id.media_list)
        mediaList.adapter = adapter
        mediaList.layoutManager = LinearLayoutManager(this)

        inputEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadSearch()
                true
            }
            false
        }
    }

    fun loadSearch() {
        RestProvider().api.search(inputEditText?.text?.toString() ?: return).enqueue(
            object : Callback<TunesResult> {

                override fun onResponse(call: Call<TunesResult>, response: Response<TunesResult>) {

                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result== null || result?.results.isNullOrEmpty()) {
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

    fun setStatusNoContent (){
        val noContentBox = findViewById<FrameLayout>(R.id.nocontent)
        noContentBox.visibility = View.VISIBLE
        val noInternet = findViewById<FrameLayout>(R.id.nointernet)
        noInternet.visibility = View.GONE
        val mediaList = findViewById<RecyclerView>(R.id.media_list)
        mediaList.visibility = View.GONE
    }

    fun setStatusNoInternet(){
        val noContentBox = findViewById<FrameLayout>(R.id.nocontent)
        noContentBox.visibility = View.GONE
        val noInternet = findViewById<FrameLayout>(R.id.nointernet)
        noInternet.visibility = View.VISIBLE
        val mediaList = findViewById<RecyclerView>(R.id.media_list)
        mediaList.visibility = View.GONE
    }
    fun setStatusMediaList (){
        val noContentBox = findViewById<FrameLayout>(R.id.nocontent)
        noContentBox.visibility = View.GONE
        val noInternet = findViewById<FrameLayout>(R.id.nointernet)
        noInternet.visibility = View.GONE
        val mediaList = findViewById<RecyclerView>(R.id.media_list)
        mediaList.visibility = View.VISIBLE
    }

    fun convertToTracks(tunes: TunesResult): MutableList<Track> {
        val tracList = mutableListOf<Track>()
        tunes.results?.forEach {
            tracList.add(
                Track(
                    trackName = it?.trackName ?: "",
                    artistName = it?.artistName ?: "",
                    trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(it?.trackTimeMillis),
                    artworkUrl100 = it?.artworkUrl100 ?: ""
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
}

