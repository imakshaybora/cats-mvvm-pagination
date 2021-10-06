package com.akshay.playground.network_library

import okhttp3.ResponseBody

sealed class NetworkResponse<out T : Any> {
    class Success<T : Any>(
        val data: T? = null,
        val httpCode: Int? = null
    ) : NetworkResponse<T>()

    class Error<out T : Any>(val exception: Exception) : NetworkResponse<T>()

    class Failure<out T : Any>(
        val httpCode: Int? = null,
        val errorBody: ResponseBody? = null,
        val errorMessage: String? = null,
        val customAttributes: List<Map<String, Any>>? = null
    ) : NetworkResponse<T>()
}