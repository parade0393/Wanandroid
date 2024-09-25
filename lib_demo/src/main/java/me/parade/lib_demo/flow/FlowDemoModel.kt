package me.parade.lib_demo.flow

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import me.parade.lib_base.base.BaseViewModel
import java.util.Timer
import java.util.TimerTask

class FlowDemoModel : BaseViewModel() {
    private val _stateFlow = MutableSharedFlow<Int>(0)

    val stateFlow = _stateFlow.asSharedFlow()

    fun startTimer() {
//        viewModelScope.launch {
//            delay(5000)
//            "send value".logd()
//            _stateFlow.emit(10)
//        }

        viewModelScope.launch {
            val timer = Timer()
            var time = 0
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    viewModelScope.launch(Dispatchers.Main) {
                        time++
                        _stateFlow.emit(time)
                    }
                }
            }, 0, 1000)
        }

    }
}