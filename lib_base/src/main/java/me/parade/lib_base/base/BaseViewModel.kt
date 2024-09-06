package me.parade.lib_base.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.parade.lib_base.net.BaseResponse

abstract  class BaseViewModel:ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    protected fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }


    fun<T> launchUI(request: suspend () -> BaseResponse<T>):T{
        viewModelScope.launch {  }
    }
}