package com.akshay.playground.network_library

object NetworkClientFactory {

    fun getNetworkClient(
        networkClientType: NetworkClientType,
        logLevel: WmLogLevel = WmLogLevel.NONE,
        readWriteTimeout: Long = NetworkConstants.READ_WRITE_TIMEOUT,
        connectionTimeout: Long = NetworkConstants.CONNECTION_TIMEOUT
    ): NetworkClientStrategy<Any> {
        return when (networkClientType) {
            NetworkClientType.RETROFIT_CLIENT -> {
                RetrofitClient(logLevel, readWriteTimeout, connectionTimeout)
            }
        }
    }

    enum class WmLogLevel {
        /**
         * Refer HttpLogginInterceptor Levels
         */
        NONE, BASIC, HEADERS, BODY
    }
}