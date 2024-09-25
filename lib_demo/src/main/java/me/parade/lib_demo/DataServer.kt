package me.parade.lib_demo

import me.parade.lib_demo.flow.FlowDemoActivity
import me.parade.lib_demo.recyclerview.diff.DiffDemoActivity

object DataServer {
    fun getDemoData():List<DemoModel>{
        return mutableListOf<DemoModel>().apply {
            add(DemoModel(title = "DiffUtil", itemViewType = DemoModel.SECTION_HEADER,))
            add(DemoModel(title = "diff1", targetClass = DiffDemoActivity::class.java, itemViewType = DemoModel.SECTION_CONTENT))
            add(DemoModel(title = "flow", itemViewType = DemoModel.SECTION_HEADER))
            add(DemoModel(title = "flow生命周期", itemViewType = DemoModel.SECTION_CONTENT, targetClass = FlowDemoActivity::class.java))
        }
    }
}