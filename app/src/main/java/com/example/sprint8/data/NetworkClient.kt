package com.example.sprint8.data

import com.example.sprint8.data.dto.Response
import com.example.sprint8.data.dto.TunesResult

interface NetworkClient {
    fun search(text: String): Response <TunesResult?>
}