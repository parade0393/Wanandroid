package me.parade.wanandroid.ui.home.child.explore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import me.parade.lib_image.ImageLoaderManager
import me.parade.wanandroid.R
import me.parade.wanandroid.net.model.Banner

class ImageAdapter( private var arrayList: List<Banner>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_banner_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        ImageLoaderManager.loadImage(arrayList[position].imagePath,holder.imageView)
//        holder.itemView.setOnClickListener { view: View? ->
//            onItemClickListener!!.onClick(
//                holder.imageView,
//                arrayList[position].url
//            )
//        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.list_item_image)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    fun setData(banners:List<Banner>){
        arrayList = banners
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(imageView: ImageView?, path: String?)
    }
}