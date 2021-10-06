package com.akshay.playground.catsclient.utils

import retrofit2.Response

sealed class ResponseWrapper<out T : Any> {
    data class Success<T : Any>(
        val data: T? = null,
        val apiError: Response<T>? = null,
        val httpCode: Int? = null,
        val responseHeaders: HashMap<String, String>? = null
    ) : ResponseWrapper<T>()

    data class Error<out T : Any>(val errorWrapper: ErrorWrapper, val httpCode: Int? = null) :
        ResponseWrapper<T>()
}

data class ErrorWrapper(
    val errorMessage: String?,
    val exceptionStackTrace: String = "",
    val errorCode: String = "",
    val errorTitle: String = "",
    val moduleCode: Int
)

//Localized string values based on the error code
data class LocalizedErrorWrapper(val title: String = "", val message: String = "")

class InternetNotAvailableException : Exception()