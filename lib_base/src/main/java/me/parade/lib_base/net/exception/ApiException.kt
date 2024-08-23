package me.parade.lib_base.net.exception

class ApiException( var requestUrl:String,  var code: Int,  var msg: String, e: Throwable? = null) : Exception(msg,e) {

}