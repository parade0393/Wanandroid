package me.parade.lib_base.net.exception

class ApiException(code: Int, msg: String, e: Throwable? = null) : Exception(e) {
    var errCode: Int = code
    var errMsg: String = msg

}