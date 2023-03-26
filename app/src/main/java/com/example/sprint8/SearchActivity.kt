package com.example.sprint8

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
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

class SearchActivity : AppCompatActivity() {
    var inputEditText: TextInputEditText? = null
    var inputSearchText = ""

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
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText?.windowToken, 0)
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
        mediaList.adapter = SearchMediaAdapter(arrayTrack)
        mediaList.layoutManager = LinearLayoutManager(this)

        RestProvider().api.search("misery").enqueue(object : Callback<TunesResult> {

            override fun onResponse(call: Call<TunesResult>, response: Response<TunesResult>) {

                if (response.isSuccessful) {
                    val hamsters = response.body()
                    hamsters
                } else {
                    val errorJson = response.errorBody()?.string()
                }
            }

            override fun onFailure(call: Call<TunesResult>, t: Throwable) {
                t.printStackTrace()
            }
        })
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

val arrayTrack = listOf(
    Track(
        //Трек 1:
        trackName = "Smells Like Teen Spirit",
        artistName = "Nirvana",
        trackTime = "5:01",
        artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
    ),
    Track(
        //Трек 2:
        trackName = "Billie Jean",
        artistName = "Michael Jackson",
        trackTime = "4:35",
        artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
    ),
    Track(
        //Трек 3:
        trackName = "Stayin' Alive",
        artistName = "Bee Gees",
        trackTime = "4:10",
        artworkUrl100 = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
    ),
    Track(
        //Трек 4:
        trackName = "Whole Lotta Love",
        artistName = "Led Zeppelin",
        trackTime = "5:33",
        artworkUrl100 = "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
    ),
    Track(
        //Трек 5:
        trackName = "Sweet Child O'Mine",
        artistName = "Guns N' Roses",
        trackTime = "5:03",
        artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
    ),
    Track(
        //Трек 6:
        trackName = "Sweet Child O'Mine Sweet Child O'Mine Sweet Child O'Mine Sweet Child O'Mine",
        artistName = "Guns N' Roses Sweet Child O'Mine Sweet Child O'Mine Sweet Child O'Mine Sweet Child O'Mine",
        trackTime = "5:03",
        artworkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
    ),
)