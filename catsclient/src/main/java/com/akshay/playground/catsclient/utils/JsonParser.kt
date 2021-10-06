package com.akshay.playground.catsclient.utils

import com.akshay.playground.network_library.NetworkClientFactory
import com.akshay.playground.network_library.NetworkClientType
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types

object JsonParser {
    // use the on from network module
    inline fun <reified T> parseFromJson(jsonString: String): T? {
        val adapter: JsonAdapter<T> =
            NetworkClientFactory.getNetworkClient(
                NetworkClientType.RETROFIT_CLIENT
            )
                .getMoshiClient().adapter(
                    T::class.java
                )
        return adapter.fromJson(jsonString)
    }


    inline fun <reified T> parseListFromJson(jsonString: String): List<T>? {
        val type = Types.newParameterizedType(List::class.java, T::class.java)
        val adapter: JsonAdapter<List<T>> =
            NetworkClientFactory.getNetworkClient(
                NetworkClientType.RETROFIT_CLIENT
            )
                .getMoshiClient().adapter(type)
        return adapter.fromJson(jsonString)
    }

    inline fun <reified K, reified V> parseMapFromJson(jsonString: String): Map<K, V>? {
        val type =
            Types.newParameterizedType(Map::class.java, K::class.java, V::class.java)
        val adapter: JsonAdapter<Map<K, V>> =
            NetworkClientFactory.getNetworkClient(
                NetworkClientType.RETROFIT_CLIENT
            )
                .getMoshiClient().adapter(type)
        return adapter.fromJson(jsonString)
    }

    inline fun <reified T> parseToJson(clzz: T): String {
        val adapter: JsonAdapter<T> =
            NetworkClientFactory.getNetworkClient(
                NetworkClientType.RETROFIT_CLIENT
            )
                .getMoshiClient().adapter(
                    T::class.java
                )
        return adapter.toJson(clzz)
    }

}
