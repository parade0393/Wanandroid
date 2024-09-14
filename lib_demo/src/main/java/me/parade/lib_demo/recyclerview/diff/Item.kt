package me.parade.lib_demo.recyclerview.diff

data class Item(val title:String,val count:Int)

sealed class PayloadType {
    data class CountUpdate(val newCount: Int) : PayloadType()
}

