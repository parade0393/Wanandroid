package me.parade.lib_demo


data class DemoModel(
    val title:String,
    val itemViewType:Int,
    val targetClass: Class<*>? = null
){
    companion object{
        /** 分组头部 */
        val SECTION_HEADER = 1
        /** 分组内容 */
        val SECTION_CONTENT = 2
    }
}
