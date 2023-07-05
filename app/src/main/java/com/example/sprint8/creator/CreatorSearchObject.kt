package com.example.sprint8.creator

import android.content.Context
import com.example.sprint8.data.internet.RestProvider
import com.example.sprint8.data.preferences.HistoryControl
import com.example.sprint8.data.search.SearchRepository
import com.example.sprint8.domain.search.SearchInteractor

object CreatorSearchObject {

    fun createSearchRepository(context: Context): SearchRepository {
        return SearchRepository(
            RestProvider().api,
            HistoryControl(context)
        )
    }



    fun createSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractor(
            createSearchRepository(context)
        )
    }
}