package com.example.topplaygroundcompose.data.api

import com.example.topplaygroundcompose.data.api.dto.MatchDto
import retrofit2.http.GET

interface OpenLigaApi {
    @GET("getmatchdata/bl1")
    suspend fun getLastMatches(): List<MatchDto>
}

