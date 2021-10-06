package com.akshay.playground.catsclient.utils

import java.util.*


object ClientUtils {
    private var correlationId: String = UUID.randomUUID().toString()

    fun getCommonHeader(correlationId: String): MutableMap<String, String> {
        val headers = mutableMapOf<String, String>()
        headers[ClientConstants.HEADER_CORRELATION_ID] = correlationId
        return headers
    }

    fun getCorrelationId(refresh: Boolean = false): String {
        if (refresh) {
            correlationId = UUID.randomUUID().toString()
        }
        return correlationId
    }
}