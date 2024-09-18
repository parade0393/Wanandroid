package me.parade.lib_demo.recyclerview.diff

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.parade.lib_common.ext.logd
import me.parade.lib_demo.R

class ItemAdapter(private var items: List<Item>, private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        "onCreateViewHolder:".logd()
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_diff_layout, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        bindItem(holder, position)
    }

    private fun bindItem(holder: RecyclerView.ViewHolder, position: Int){
        items[position].let { item ->
            (holder as ViewHolder).apply {
                titleTextView.text = item.title
                countTextView.text = item.count.toString()
                indexTextView.text = position.toString()
                itemView.setOnClickListener {
                    onItemClick(position)
                }
            }
        }
    }
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        "onBindViewHolder$position".logd()
        if (payloads.isEmpty()){
            bindItem(holder, position)
        }else{
            when(val payload = payloads[0] as PayloadType){
                is PayloadType.CountUpdate -> {
                    (holder as ViewHolder).countTextView.text = "Count:${payload.newCount}"
                }
            }
        }
    }

    fun updateItems(newItems: List<Item>) {
        val diffCallback = ItemDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = newItems.toList()
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.tvTitle)
        val countTextView: TextView = view.findViewById(R.id.tvCount)
        val indexTextView:TextView = view.findViewById(R.id.tvIndex)
    }

    class ItemDiffCallback(
        private val oldList: List<Item>,
        private val newList: List<Item>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].title == newList[newItemPosition].title

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return if (oldItem.count != newItem.count) {
                PayloadType.CountUpdate(newItem.count)
            } else {
                null
            }
        }

    }
}