package com.akshay.playground.cats.view.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.akshay.playground.cats.common.ScreenState

abstract class BaseViewModel<in Event, State> : ViewModel() {

    protected val _state: MutableLiveData<ScreenState<State>> = MutableLiveData()

    val state: LiveData<ScreenState<State>> = _state

    fun dispatchEvent(event: Event) {
        handleEvent(event)
    }

    protected fun setState(state: State) {

        _state.value = ScreenState.Render(state)
    }

    protected fun postState(state: State) {

        _state.postValue(ScreenState.Render(state))
    }

    protected abstract fun handleEvent(publishedEvent: Event)

}