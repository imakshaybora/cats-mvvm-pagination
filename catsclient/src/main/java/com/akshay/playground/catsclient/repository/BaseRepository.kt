package com.akshay.playground.catsclient.repository


import com.akshay.playground.catsclient.BuildConfig
import com.akshay.playground.catsclient.responsemodel.ErrorResponse
import com.akshay.playground.catsclient.utils.ClientConstants
import com.akshay.playground.catsclient.utils.ClientConstants.ErrorSource.ERROR_FROM_API
import com.akshay.playground.catsclient.utils.ClientConstants.ErrorSource.HTTP_EXCEPTION
import com.akshay.playground.catsclient.utils.ClientConstants.ErrorSource.IO_EXCEPTION
import com.akshay.playground.catsclient.utils.ClientConstants.ErrorSource.JSON_DATA_EXCEPTION
import com.akshay.playground.catsclient.utils.ClientConstants.ErrorSource.NONE
import com.akshay.playground.catsclient.utils.ClientConstants.ErrorSource.NO_INTERNET
import com.akshay.playground.catsclient.utils.ClientConstants.ErrorSource.SOCKET_TIMEOUT
import com.akshay.playground.catsclient.utils.ClientConstants.ErrorSource.UNKNOWN_HOST
import com.akshay.playground.catsclient.utils.ErrorWrapper
import com.akshay.playground.catsclient.utils.InternetNotAvailableException
import com.akshay.playground.catsclient.utils.ResponseWrapper
import com.akshay.playground.network_library.NetworkClientFactory
import com.akshay.playground.network_library.NetworkClientType
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.reflect.KClass

open class BaseRepository {

    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>,
        errorClass: KClass<*> = ErrorResponse::class,
        parseError: ((String) -> ErrorWrapper)? = null,
    ): ResponseWrapper<T> {
        var responseWrapper: ResponseWrapper<T>
        try {
            val response: Response<T> = call.invoke()
            responseWrapper = if (response.isSuccessful) {


                ResponseWrapper.Success(
                    data = response.body(),
                    httpCode = response.code()
                )
            } else {
                ResponseWrapper.Error(
                    parseApiError(response.errorBody(), errorClass, parseError), response.code()
                )
            }
            return responseWrapper
        } catch (exception: Exception) {
            responseWrapper = getError(exception)
        }
        return responseWrapper
    }

    private fun <T : Any> getError(exception: Exception): ResponseWrapper<T> {
        val stackTrace = StringBuilder()

        try {
            exception.stackTrace.forEach {
                stackTrace.append("\tat ${it ?: ""}")
            }
        } catch (e: Exception) {
            stackTrace.append("Could not get Stacktrace!")
        }

        return when (exception) {
            is InternetNotAvailableException -> ResponseWrapper.Error(
                ErrorWrapper(
                    errorMessage = ClientConstants.INTERNET_ERROR,
                    exceptionStackTrace = stackTrace.toString(),
                    moduleCode = NO_INTERNET
                )
            )
            is UnknownHostException -> ResponseWrapper.Error(
                ErrorWrapper(
                    errorMessage = ClientConstants.ERROR_UNKNOWN_HOST,
                    exceptionStackTrace = stackTrace.toString(),
                    moduleCode = UNKNOWN_HOST
                )
            )
            is JsonDataException -> ResponseWrapper.Error(
                ErrorWrapper(
                    errorMessage = exception.message.toString(),
                    exceptionStackTrace = stackTrace.toString(),
                    moduleCode = JSON_DATA_EXCEPTION
                )
            )
            is SocketTimeoutException -> ResponseWrapper.Error(
                ErrorWrapper(
                    errorMessage = ClientConstants.ERROR_TIMEOUT,
                    exceptionStackTrace = stackTrace.toString(),
                    moduleCode = SOCKET_TIMEOUT
                )
            )
            is HttpException -> {
                ResponseWrapper.Error(
                    ErrorWrapper(
                        errorMessage = exception.message.toString(),
                        exceptionStackTrace = stackTrace.toString(),
                        moduleCode = HTTP_EXCEPTION
                    )
                )
            }
            is IOException -> {
                ResponseWrapper.Error(
                    ErrorWrapper(
                        errorMessage = exception.message.toString(),
                        exceptionStackTrace = stackTrace.toString(),
                        moduleCode = IO_EXCEPTION
                    )
                )
            }
            else -> {
                ResponseWrapper.Error(
                    ErrorWrapper(
                        errorMessage = exception.message.toString(),
                        exceptionStackTrace = stackTrace.toString(),
                        moduleCode = NONE
                    )
                )
            }
        }
    }

    private fun parseApiError(
        error: ResponseBody?,
        clazz: KClass<*>,
        parseError: ((String) -> ErrorWrapper)?
    ): ErrorWrapper {
        val networkClient = NetworkClientFactory.getNetworkClient(
            NetworkClientType.RETROFIT_CLIENT,
            if (BuildConfig.DEBUG) NetworkClientFactory.WmLogLevel.BODY else NetworkClientFactory.WmLogLevel.NONE
        )
        return error?.string()?.let {

            if (it.isEmpty()) {
                return genericErrorMessage()
            }

            when (clazz) {
                ErrorResponse::class -> {
                    val adapter: JsonAdapter<ErrorResponse> =
                        networkClient.getMoshiClient()
                            .adapter(ErrorResponse::class.java)
                    val parsedData = adapter.fromJson(it)
                    ErrorWrapper(
                        errorMessage = parsedData?.message,
                        errorCode = "",
                        moduleCode = ERROR_FROM_API
                    )
                }

                else -> {
                    parseError?.invoke(it) ?: genericErrorMessage()
                }
            }
        } ?: genericErrorMessage()
    }

    protected fun genericErrorMessage(): ErrorWrapper {
        return ErrorWrapper(
            errorMessage = ClientConstants.GENERIC_ERROR,
            moduleCode = NONE
        )
    }
}