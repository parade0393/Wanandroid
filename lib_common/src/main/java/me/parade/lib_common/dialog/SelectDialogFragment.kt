package me.parade.lib_common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.parade.lib_common.R

class SelectDialogFragment:BaseDialogFragment() {

    private var items: List<String> = emptyList()

    private var onItemClick: ((Int) -> Unit)? = null


    override fun getDialogWidth() = WindowManager.LayoutParams.MATCH_PARENT
    override fun getDialogHeight() = WindowManager.LayoutParams.WRAP_CONTENT

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.public_dialog_select,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            findViewById<RecyclerView>(R.id.rvItems).apply {
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }
}