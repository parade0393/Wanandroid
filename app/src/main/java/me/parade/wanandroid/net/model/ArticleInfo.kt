package me.parade.wanandroid.net.model


data class ArticleInfo(
    val id: Int,
    val userId: Int,
    val courseId: Int?,
    val originId: Int?,
    var collect: Boolean = false,
    val title: String?,
    val desc: String?,
    val link: String?,
    val zan: Int?,
    val niceShareDate: String?,
    val niceDate: String?,
    val publishTime: Long,
    val shareUser: String,
    val author: String,
    val superChapterName: String? = "",
    val chapterName: String?,
    val tags: MutableList<Tag>? = arrayListOf(),
    var type: Int,
    var fresh: Boolean,


){
    /**
     * 获取文章作者
     */
    val articleAuthor
        get() =  author.ifEmpty { shareUser }
}