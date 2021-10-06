package com.akshay.playground.catsclient.repository

import com.akshay.playground.catsclient.dto.BreedDto
import com.akshay.playground.catsclient.provider.RetrofitServiceProvider
import com.akshay.playground.catsclient.responsemodel.ErrorResponse
import com.akshay.playground.catsclient.utils.ClientUtils
import com.akshay.playground.catsclient.utils.ResponseWrapper
import com.walmart.move.nim.unified.mobile.gnfrreceivingclient.repository.CatsConfiguration

class CatsRetrofitRepository : BaseRepository(), CatsRepository {

    override suspend fun getCatsBreedListData(
        pageNumber: Int,
        pageSize: Int
    ): ResponseWrapper<MutableList<BreedDto>> {
        return safeApiCall(
            call = {
                RetrofitServiceProvider.catsService.getCatsBreedListData(
                    ClientUtils.getCommonHeader(
                        ClientUtils.getCorrelationId(true)
                    ), CatsConfiguration.getCatsBreedListAPIUrl(pageNumber, pageSize)
                )
            },
            errorClass = ErrorResponse::class
        )
    }
}
