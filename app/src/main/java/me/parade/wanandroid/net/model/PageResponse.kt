package me.parade.wanandroid.net.model

data class PageResponse<T>(
    val curPage: Int ? = 0,
    val datas: List<T>? = mutableListOf(),
    val offset: Int? = 0,
    val pageCount: Int? = 0,
    val size: Int? = 0,
    val total: Int? = 0
)