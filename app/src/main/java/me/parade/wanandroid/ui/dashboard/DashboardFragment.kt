package me.parade.wanandroid.ui.dashboard

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import me.parade.lib_base.BaseFragment
import me.parade.lib_base.NoViewModel
import me.parade.wanandroid.databinding.FragmentDashboardBinding

class DashboardFragment : BaseFragment<FragmentDashboardBinding,NoViewModel>() {

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }
}