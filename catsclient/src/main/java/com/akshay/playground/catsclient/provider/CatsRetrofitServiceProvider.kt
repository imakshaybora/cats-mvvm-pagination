package com.akshay.playground.catsclient.provider

import com.akshay.playground.catsclient.BuildConfig
import com.akshay.playground.catsclient.service.CatsService
import com.akshay.playground.network_library.NetworkClientFactory
import com.akshay.playground.network_library.NetworkClientType
import com.akshay.playground.network_library.RetrofitClient

/**
 * Provides all the services which in turn meets different end points
 */
object RetrofitServiceProvider {

    // this needs to change
    private val networkClient =
        NetworkClientFactory.getNetworkClient(
            NetworkClientType.RETROFIT_CLIENT,
            if (BuildConfig.DEBUG) NetworkClientFactory.WmLogLevel.BODY else NetworkClientFactory.WmLogLevel.NONE
        )

    private fun createRetrofitService(retroFitServiceType: RetroFitServiceType): Any {
        networkClient as RetrofitClient
        return when (retroFitServiceType) {
            RetroFitServiceType.CATS_SERVICE -> {
                networkClient.getNetworkClient().create(
                    CatsService::class.java
                )
            }
            else ->
                throw RuntimeException("None of the service found")
        }
    }

    val catsService: CatsService by lazy {
        createRetrofitService(
            RetroFitServiceType.CATS_SERVICE
        ) as CatsService
    }
}
