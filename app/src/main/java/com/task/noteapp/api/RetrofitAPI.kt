package com.task.noteapp.api

import com.task.noteapp.model.ImageResponse
import com.task.noteapp.util.Util
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {

    @GET("/api/")
    suspend fun imageSearch(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = Util.API_KEY
    ): Response<ImageResponse>
}