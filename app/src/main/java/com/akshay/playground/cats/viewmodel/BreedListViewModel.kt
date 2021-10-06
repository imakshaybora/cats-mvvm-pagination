package com.akshay.playground.cats.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.akshay.playground.cats.common.ScreenState
import com.akshay.playground.cats.view.breedlist.BreedListEvent
import com.akshay.playground.cats.view.breedlist.BreedListState
import com.akshay.playground.cats.view.common.BaseListViewModel
import com.akshay.playground.catsclient.dto.BreedDto
import com.akshay.playground.catsclient.repository.CatsRepository
import com.akshay.playground.catsclient.utils.ResponseWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BreedListViewModel(
    private val repo: CatsRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseListViewModel<BreedListEvent, BreedListState, Int, BreedDto>() {

    companion object {
        private const val STARTING_PAGE_NUMBER = 0
    }

    override fun handleEvent(publishedEvent: BreedListEvent) {
        when (publishedEvent) {
            is BreedListEvent.OnScreenLoaded -> {
                if (dataList.value?.isEmpty() == true) {
                    dataList.value?.dataSource?.invalidate()
                }
            }
        }
    }

    override fun getListCallback(): (Int?, Int, (List<BreedDto>, Int?) -> Unit) -> Unit {
        return { pageNumber, pageSize, callback ->
            getCatsBreedListData(
                pageNumber,
                pageSize,
                callback
            )
        }
    }

    private fun getCatsBreedListData(
        pageNumber: Int?,
        pageSize: Int,
        callback: (List<BreedDto>, Int?) -> Unit
    ) {
        if (dataList.value.isNullOrEmpty()) {
            _state.postValue(ScreenState.Loading)
        } else {
            postState(BreedListState.LoadingMore)
        }

        viewModelScope.launch(dispatcher) {
            when (val getBreedListData =
                repo.getCatsBreedListData(
                    pageNumber ?: STARTING_PAGE_NUMBER,
                    pageSize
                )
            ) {
                is ResponseWrapper.Success -> {
                    getBreedListData.data?.let { listResponseLookUpItem ->
                        callback.invoke(
                            getBreedListData.data ?: emptyList(),
                            pageNumber?.let { it + 1 } ?: STARTING_PAGE_NUMBER + 1
                        )
                        if (listResponseLookUpItem.isNullOrEmpty())
                            postState(BreedListState.EndOfList)
                        else
                            postState(
                                BreedListState.UpdateData
                            )
                    } ?: run {
                        if (pageNumber != null) {
                            postState(BreedListState.EndOfList)
                        } else {
                            postState(BreedListState.EmptyData)
                        }
                    }
                }
                is ResponseWrapper.Error -> {
                    if (dataList.value?.isEmpty() == true) {
                        postState(
                            BreedListState.ApiErrorState(
                                getBreedListData.errorWrapper
                            )
                        )
                    } else {
                        postState(
                            BreedListState.ApiErrorStateWhenDataAvailable(
                                getBreedListData.errorWrapper
                            )
                        )
                    }
                }
            }
        }
    }

}

class BreedListViewModelFactory(
    private val repository: CatsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BreedListViewModel(
            repository
        ) as T
    }
}
