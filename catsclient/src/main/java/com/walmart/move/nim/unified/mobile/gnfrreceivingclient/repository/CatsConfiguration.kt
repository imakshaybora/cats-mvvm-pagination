package com.walmart.move.nim.unified.mobile.gnfrreceivingclient.repository

object CatsConfiguration {
    const val PAGE_NUMBER = "page="
    const val PAGE_LIMIT = "&limit="
    private const val catsBaseUrl = "https://api.thecatapi.com"
    private const val catsBreedListEndPoint = "/v1/breeds?"

    fun getCatsBreedListAPIUrl(pageNumber: Int, pageSize: Int): String =
        "${catsBaseUrl}$catsBreedListEndPoint$PAGE_NUMBER$pageNumber$PAGE_LIMIT$pageSize"


}
