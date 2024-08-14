package me.parade.wanandroid.ui.dashboard

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import me.parade.lib_base.base.BaseFragment
import me.parade.wanandroid.TestMainViewModel
import me.parade.wanandroid.databinding.FragmentDashboardBinding
import kotlin.concurrent.thread

class DashboardFragment : BaseFragment<FragmentDashboardBinding, TestMainViewModel>() {

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]
        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        viewModel.count.observe(this ){
            binding.tvCount.text = "$it"
        }
    }

    override fun getCustomViewModelStore() = requireActivity().viewModelStore
}