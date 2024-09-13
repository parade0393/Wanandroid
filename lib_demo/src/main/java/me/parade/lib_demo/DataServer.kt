package me.parade.lib_demo

object DataServer {
    fun getDemoData():List<DemoModel>{
        return mutableListOf<DemoModel>().apply {
            add(DemoModel(title = "DiffUtil", itemViewType = DemoModel.SECTION_HEADER,))
        }
    }
}