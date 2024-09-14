package me.parade.lib_demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DemoAdapter(private val list: List<DemoModel>, private val onItemClick: (DemoModel) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DemoModel.SECTION_HEADER) {
            SectionHeaderVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_section_header, parent, false)
            )
        } else {
            SectionContentVH(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_section_content, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SectionHeaderVH) {
            holder.tvSectionHeader.text = list[position].title
        } else if (holder is SectionContentVH) {
            holder.tvSectionContent.text = list[position].title
        }
        holder.itemView.setOnClickListener {
            onItemClick(list[position])
        }
    }

    override fun getItemViewType(position: Int) = list[position].itemViewType


    class SectionHeaderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSectionHeader: TextView = itemView.findViewById(R.id.tv_section_header)
    }

    class SectionContentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSectionContent: TextView = itemView.findViewById(R.id.tv_section_content)
    }
}