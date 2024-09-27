package me.parade.lib_common.ext

import me.parade.lib_common.BuildConfig

fun debugMode(action:() -> Unit){
    if (BuildConfig.DEBUG){
        action.invoke()
    }
}