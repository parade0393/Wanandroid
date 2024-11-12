package me.parade.lib_common.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.parade.lib_common.R
import me.parade.lib_common.recy.CustomItemDecoration

/**
 * 选择性的dialog
 */
class SelectDialogFragment : BaseDialogFragment() {

    private var items: MutableList<String> = mutableListOf()

    private var onItemClick: ((Int) -> Unit)? = null

    init {
        animStyle = DialogAnimation.BOTTOM
    }


    override fun getDialogWidth() = WindowManager.LayoutParams.MATCH_PARENT
    override fun getDialogHeight() = WindowManager.LayoutParams.WRAP_CONTENT

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.public_dialog_select, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            findViewById<RecyclerView>(R.id.rvItems).apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = SelectDialogAdapter(requireContext(),items).apply {
                    addItemDecoration(
                        CustomItemDecoration(
                            requireContext(),
                            dividerColor = Color.parseColor("#f4f4f4"),
                            skipLastDivider = true
                        )
                    )
                    setOnItemClickListener { _, i ->
                        onItemClick?.invoke(i)
                        dismiss()
                    }
                }
            }
        }
    }

    class Builder : BaseDialogBuilder<Builder>() {
        private var items: MutableList<String> = mutableListOf()

        private var onItemClick: ((Int) -> Unit)? = null

        fun setItems(items: MutableList<String>) = apply { this.items = items }

        fun setOnItemClickListener(listener: (Int) -> Unit) = apply { this.onItemClick = listener }

        override fun build(): SelectDialogFragment {
            return SelectDialogFragment().apply {
                    items = this@Builder.items
                    onItemClick = this@Builder.onItemClick
                    animStyle = this@Builder.animStyle

            }
        }
    }
}