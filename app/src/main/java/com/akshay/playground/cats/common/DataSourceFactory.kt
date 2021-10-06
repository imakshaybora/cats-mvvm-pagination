package com.akshay.playground.cats.common

import androidx.paging.DataSource


class DataSourceFactory<Key, Data>(
    private val load: (
        Key?,
        Int,
        (List<Data>, Key?) -> Unit
    ) -> Unit
) : DataSource.Factory<Key, Data>() {
    override fun create(): DataSource<Key, Data> {
        return CatsDataSource(load)
    }
}