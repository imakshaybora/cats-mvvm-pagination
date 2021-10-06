package com.akshay.playground.catsclient.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BreedDto(
    val id: String,
    val name: String,
    val description: String,
    val image: Image?
)

@JsonClass(generateAdapter = true)
data class Image(
    val id: String?,
    val width: Int?,
    val height: Int?,
    val url: String?
)