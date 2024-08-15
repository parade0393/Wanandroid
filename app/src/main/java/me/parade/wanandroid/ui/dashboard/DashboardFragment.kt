package me.parade.wanandroid.ui.dashboard

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import me.parade.lib_base.base.BaseFragment
import me.parade.wanandroid.TestMainViewModel
import me.parade.wanandroid.databinding.FragmentDashboardBinding

class DashboardFragment : BaseFragment<FragmentDashboardBinding, TestMainViewModel>() {

    override fun initView(savedInstanceState: Bundle?) {

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