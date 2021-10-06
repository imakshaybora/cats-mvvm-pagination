package com.akshay.playground.cats.view.common

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.akshay.playground.cats.common.DataSourceFactory

abstract class BaseListViewModel<Event, State, Key, Data> : BaseViewModel<Event, State>() {

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
    }

    val dataList: LiveData<PagedList<Data>>

    private val dataSourceFactory: DataSourceFactory<Key, Data> by lazy {
        DataSourceFactory(getListCallback())
    }

    abstract fun getListCallback(): (Key?, Int, (List<Data>, Key?) -> Unit) -> Unit

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(getPageSize())
            .setInitialLoadSizeHint(getInitialLoadSizeHint())
            .setEnablePlaceholders(false)
            .build()
        dataList = LivePagedListBuilder(dataSourceFactory, config).build()
    }

    protected open fun getPageSize() = DEFAULT_PAGE_SIZE

    protected open fun getInitialLoadSizeHint() = DEFAULT_PAGE_SIZE
}
