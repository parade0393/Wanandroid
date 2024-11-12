package me.parade.lib_common.dialog

import android.content.Context
import me.parade.lib_common.recy.BaseRecyclerAdapter
import me.parade.lib_common.R

internal class SelectDialogAdapter(context: Context, data: MutableList<String> = mutableListOf()) :
    BaseRecyclerAdapter<String>(context, data) {
    override fun getItemLayoutRes() = R.layout.public_dialog_select_item

    override fun convert(holder: BaseViewHolder, item: String, position: Int) {
        holder.setText(R.id.tvItem,item)
    }
}