package com.akshay.playground.catsclient.repository

import com.akshay.playground.catsclient.dto.BreedDto
import com.akshay.playground.catsclient.utils.ResponseWrapper

interface CatsRepository {

    suspend fun getCatsBreedListData(
        pageNumber: Int,
        pageSize: Int
    ): ResponseWrapper<MutableList<BreedDto>>
}
