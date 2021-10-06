package com.akshay.playground.cats.common.utils

import android.content.Context
import android.graphics.drawable.Drawable
import com.akshay.playground.cats.R
import com.akshay.playground.catsclient.utils.ClientConstants.ERROR_TIMEOUT
import com.akshay.playground.catsclient.utils.ClientConstants.ERROR_UNKNOWN_HOST
import com.akshay.playground.catsclient.utils.ClientConstants.GENERIC_ERROR
import com.akshay.playground.catsclient.utils.ErrorWrapper
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun RequestBuilder<Drawable>.withOptions(
    width: Int = 128,
    height: Int = 128
): RequestBuilder<Drawable> {
    return transition(DrawableTransitionOptions.withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .override(width, height)
        .placeholder(R.drawable.ic_placeholder_cats_image)
        .error(R.drawable.ic_placeholder_cats_image) // error image can be different
        .fitCenter()
}


fun String?.loadImageFromUrl(context: Context): RequestBuilder<Drawable> {
    return this?.let {
        Glide.with(context).load(it).withOptions()
    } ?: Glide.with(context).load(R.drawable.ic_placeholder_cats_image)
        .withOptions()
}

fun Context?.formatError(errorWrapper: ErrorWrapper?): String {
    return this?.let {
        when (errorWrapper?.errorMessage) {
            ERROR_UNKNOWN_HOST -> it.getString(R.string.unknown_host)
            ERROR_TIMEOUT -> it.getString(R.string.socket_timeout_exception)
            else -> errorWrapper?.errorMessage ?: GENERIC_ERROR
        }
    } ?: run {
        GENERIC_ERROR
    }
}