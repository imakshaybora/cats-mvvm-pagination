package com.akshay.playground.catsclient.responsemodel


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    val message: String,
)