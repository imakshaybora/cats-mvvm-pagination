package com.akshay.playground.network_library

import android.util.Log
import com.akshay.playground.network_library.adapters.BigIntegerAdapter
import com.akshay.playground.network_library.adapters.MissingFieldListObjectsAdapter
import com.akshay.playground.network_library.adapters.NullToEmptyStringAdapter
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

abstract class BaseClient(
    private val logLevel: NetworkClientFactory.WmLogLevel = NetworkClientFactory.WmLogLevel.NONE,
    private val readWriteTimeout: Long = NetworkConstants.READ_WRITE_TIMEOUT,
    private val connectionTimeout: Long = NetworkConstants.CONNECTION_TIMEOUT
) {
    private val interceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(
            when (logLevel) {
                NetworkClientFactory.WmLogLevel.NONE -> HttpLoggingInterceptor.Level.NONE
                NetworkClientFactory.WmLogLevel.HEADERS -> HttpLoggingInterceptor.Level.HEADERS
                NetworkClientFactory.WmLogLevel.BASIC -> HttpLoggingInterceptor.Level.BASIC
                NetworkClientFactory.WmLogLevel.BODY -> HttpLoggingInterceptor.Level.BODY
            }
        )

    protected val mHttpClient by lazy { getUnSafeOkHttpClient() }
    private val retryInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest: Request = chain.request()
            return retryInterceptorCall(chain = chain, originalRequest = originalRequest)
        }

    }

    private fun retryInterceptorCall(
        retryCount: Int = 1,
        retryMaxCount: Int = 3,
        chain: Interceptor.Chain,
        originalRequest: Request,
        exceptionVal: Exception = Exception()
    ): Response {
        if (retryCount > retryMaxCount) {
            Log.d("API_EXCEPTION_RETRY_CNT", retryCount.toString())
            throw exceptionVal
        }
        return try {
            chain.proceed(originalRequest)
        } catch (exception: IOException) {
            Log.d("API_EXCEPTION_RETRY_CNT", retryCount.toString())
            retryInterceptorCall(
                retryCount + 1,
                chain = chain,
                originalRequest = originalRequest,
                exceptionVal = exception
            )
        }
    }

    private fun addHttpClientTimeout(
        httpClient: OkHttpClient.Builder,
        connectTimeout: Long = connectionTimeout,
        readTimeout: Long = readWriteTimeout,
        writeTimeout: Long = readWriteTimeout
    ) = httpClient.let {
        it.addInterceptor(interceptor)
        if (!BuildConfig.DEBUG) {
            it.addInterceptor(retryInterceptor)
        }
        it.connectTimeout(connectTimeout, TimeUnit.SECONDS)
        it.readTimeout(readTimeout, TimeUnit.SECONDS)
        it.writeTimeout(writeTimeout, TimeUnit.SECONDS)
    }

    protected val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(NullToEmptyStringAdapter)
            .add(MissingFieldListObjectsAdapter())
            .add(BigIntegerAdapter)
            .build()
    }

    private fun getUnSafeOkHttpClient(): OkHttpClient.Builder {
        try {
            /* val trustAllCerts: Array<TrustManager> = arrayOf(CustomX509TrustManager())
             val sslContext = SSLContext.getInstance("TLSv1.2")
             sslContext.init(null, trustAllCerts, java.security.SecureRandom())
             val sslSocketFactory = sslContext.socketFactory*/
            val builder = OkHttpClient.Builder()
            addHttpClientTimeout(builder)
            /* builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
             builder.hostnameVerifier(HostnameVerifier { _, _ -> true })*/
            return builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}