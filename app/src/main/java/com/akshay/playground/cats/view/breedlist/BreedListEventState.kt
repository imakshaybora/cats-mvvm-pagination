package com.akshay.playground.cats.view.breedlist

import com.akshay.playground.catsclient.utils.ErrorWrapper

sealed class BreedListState {
    class ApiErrorState(val errorWrapper: ErrorWrapper) : BreedListState()
    class ApiErrorStateWhenDataAvailable(val errorWrapper: ErrorWrapper) : BreedListState()

    object LoadingMore : BreedListState()
    object EndOfList : BreedListState()
    object UpdateData : BreedListState()
    object EmptyData : BreedListState()

}

sealed class BreedListEvent {
    object OnScreenLoaded : BreedListEvent()
}