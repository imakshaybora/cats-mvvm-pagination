package com.akshay.playground.network_library


import kotlinx.coroutines.delay
import retrofit2.Response

class NetworkCall {

    suspend fun <T : Any> retrofitCall(call: suspend () -> Response<T>): NetworkResponse<T> {
        var networkResponse: NetworkResponse<T>
        try {
            val response: Response<T> = call.invoke()
            networkResponse = if (response.isSuccessful)
                NetworkResponse.Success(data = response.body(), httpCode = response.code())
            else
                NetworkResponse.Failure(response.code(), response.errorBody())
            return networkResponse
        } catch (exception: Exception) {
            networkResponse = NetworkResponse.Error(exception = exception)
        }
        return networkResponse
    }

    suspend fun <T : Any> retrofitCallWithRetry(
        times: Int = 3, // retry 3 times
        initialDelay: Long = 100, // 0.1 second
        maxDelay: Long = 1000, // 1 second
        factor: Double = 2.0,
        call: suspend () -> Response<T>
    ): NetworkResponse<T> {

        var networkResponse: NetworkResponse<T>
        try {
            val response: Response<T> = call.invoke()
            networkResponse = if (response.isSuccessful)
                NetworkResponse.Success(data = response.body())
            else
                NetworkResponse.Failure(response.code(), response.errorBody())
            return networkResponse
        } catch (exception: Exception) {
            networkResponse = NetworkResponse.Error(exception)
            if (times == 1) return networkResponse
            delay(initialDelay)
            retrofitCallWithRetry(
                times = times - 1,
                call = call,
                initialDelay = (initialDelay * factor).toLong().coerceAtMost(maxDelay)
            )
        }
        return networkResponse
    }
}