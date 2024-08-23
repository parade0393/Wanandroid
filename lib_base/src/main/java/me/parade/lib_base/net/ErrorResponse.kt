package me.parade.lib_base.net

data class ErrorResponse(
    val requestUrl:String = "",
    val errorCode: Int = 0,
    val errorMsg: String = ""
)
