package com.akshay.playground.catsclient.service

import com.akshay.playground.catsclient.dto.BreedDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Url

interface CatsService {

    @GET
    suspend fun getCatsBreedListData(
        @HeaderMap commonHeader: Map<String, String>,
        @Url url: String
    ): Response<MutableList<BreedDto>>
}
