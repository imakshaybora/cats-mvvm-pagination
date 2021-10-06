package com.akshay.playground.cats.common

import androidx.paging.PageKeyedDataSource


class CatsDataSource<Key, Data>(
    private val load: (
        Key?,
        Int,
        (List<Data>, Key?) -> Unit
    ) -> Unit
) : PageKeyedDataSource<Key, Data>() {

    override fun loadInitial(
        params: LoadInitialParams<Key>,
        callback: LoadInitialCallback<Key, Data>
    ) {
        load.invoke(null, params.requestedLoadSize) { data: List<Data>, nextPageKey: Key? ->
            callback.onResult(data, null, nextPageKey)
        }
    }

    override fun loadBefore(
        params: LoadParams<Key>,
        callback: LoadCallback<Key, Data>
    ) {
    }

    override fun loadAfter(
        params: LoadParams<Key>,
        callback: LoadCallback<Key, Data>
    ) {
        load.invoke(params.key, params.requestedLoadSize) { data: List<Data>, nextPageKey: Key? ->
            callback.onResult(data, nextPageKey)
        }
    }
}