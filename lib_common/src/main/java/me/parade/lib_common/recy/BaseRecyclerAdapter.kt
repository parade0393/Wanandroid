package me.parade.lib_common.recy

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView通用适配器
 * @param T 数据类型
 */
internal abstract class BaseRecyclerAdapter<T>(
    protected var context: Context,
    private var dataList: MutableList<T> = mutableListOf()
) : RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder>() {

    private var onItemClickListener: ((View, Int) -> Unit)? = null
    private var onItemLongClickListener: ((View, Int) -> Boolean)? = null

    @LayoutRes
    protected abstract fun getItemLayoutRes(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView = LayoutInflater.from(context).inflate(getItemLayoutRes(), parent, false)
        return BaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        // 设置点击事件
        holder.itemView.setOnClickListener { view ->
            onItemClickListener?.invoke(view, position)
        }
        holder.itemView.setOnLongClickListener { view ->
            onItemLongClickListener?.invoke(view, position) ?: false
        }

        convert(holder, dataList[position], position)
    }

    override fun getItemCount(): Int = dataList.size

    /**
     * 子类实现此方法，处理item数据绑定
     */
    protected abstract fun convert(holder: BaseViewHolder, item: T, position: Int)

    /**
     * 重置数据
     */
    fun resetData(data: List<T>) {
        this.dataList.clear()
        this.dataList.addAll(data)
        notifyDataSetChanged()
    }

    /**
     * 追加数据
     */
    fun appendData(data: List<T>) {
        val startPosition = this.dataList.size
        this.dataList.addAll(data)
        notifyItemRangeInserted(startPosition, data.size)
    }

    /**
     * 设置Item点击事件
     */
    fun setOnItemClickListener(listener: (View, Int) -> Unit) {
        this.onItemClickListener = listener
    }

    /**
     * 设置Item长按事件
     */
    fun setOnItemLongClickListener(listener: (View, Int) -> Boolean) {
        this.onItemLongClickListener = listener
    }

    /**
     * ViewHolder基类，提供便捷的findViewById方法
     */
    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val views: SparseArray<View> = SparseArray()

        fun <V : View> getView(viewId: Int): V {
            var view = views.get(viewId)
            if (view == null) {
                view = itemView.findViewById(viewId)
                views.put(viewId, view)
            }
            @Suppress("UNCHECKED_CAST")
            return view as V
        }

        fun setText(viewId: Int, text: CharSequence) {
            val textView = getView<android.widget.TextView>(viewId)
            textView.text = text
        }

        fun setImageResource(viewId: Int, resId: Int) {
            val imageView = getView<android.widget.ImageView>(viewId)
            imageView.setImageResource(resId)
        }
    }
}