package me.parade.wanandroid

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import me.parade.lib_base.base.BaseViewModel

class TestViewModel(private val userId:Int,private val app:Application?, private val savedStateHandle:SavedStateHandle):
    BaseViewModel() {
    private val _count: MutableLiveData<Int> = savedStateHandle.getLiveData("count", 0)
    val count: LiveData<Int> = _count
    init {
        if (!savedStateHandle.contains("count")) {
            savedStateHandle["count"] = 0
        }
    }


    fun increment() {
        Log.d("parade0393", "increment: ${app?.cacheDir}")
        val currentCount = _count.value ?: 0
        _count.value = currentCount + 1
    }

    fun decrement() {
        val currentCount = _count.value ?: 0
        _count.value = currentCount - 1
    }

    override fun onCleared() {
        savedStateHandle["count"] = _count.value
        super.onCleared()
    }
}