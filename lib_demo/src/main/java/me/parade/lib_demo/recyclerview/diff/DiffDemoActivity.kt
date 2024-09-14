package me.parade.lib_demo.recyclerview.diff

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import me.parade.lib_base.base.BaseActivity
import me.parade.lib_base.base.EmptyViewModel
import me.parade.lib_demo.R
import me.parade.lib_demo.databinding.ActivityDiffDemoBinding

class DiffDemoActivity : BaseActivity<ActivityDiffDemoBinding,EmptyViewModel>() {

    private var items = listOf<Item>()
    private lateinit var listAdapter: ItemAdapter

    private val updateItemLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if (result.resultCode == RESULT_OK){
            val newCount = result.data?.getIntExtra("newCount", 0) ?: 0
            updateItem(15,newCount)
        }
    }

    private val deleteItemLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if (result.resultCode == RESULT_OK){
            deleteItem(29)
        }
    }

    override fun getLayoutResId() = R.layout.activity_diff_demo

    override fun initView(savedInstanceState: Bundle?) {
        loadInitialData()
        listAdapter = ItemAdapter(items){ position->
            when(position){
                15 -> updateItemLauncher.launch(Intent(this,DiffSecondActivity::class.java))
                29 -> deleteItemLauncher.launch(Intent(this,DiffThirdActivity::class.java))
            }
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@DiffDemoActivity)
            adapter = listAdapter
        }
        listAdapter.updateItems(items)
    }

    private fun loadInitialData() {
        items = List(30) { i -> Item("Title ${i + 1}", (1..100).random()) }
    }



    private fun updateItem(position: Int, newCount: Int) {
        val updatedItems = items.toMutableList()
        updatedItems[position] = updatedItems[position].copy(count = newCount)
        items = updatedItems.toList()
        listAdapter.updateItems(items)
    }

    private fun deleteItem(position: Int) {
        val updatedItems = items.toMutableList()
        updatedItems.removeAt(position)
        items = updatedItems.toList()
        listAdapter.updateItems(items)
    }


}