package me.parade.wanandroid.ui.home

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import me.parade.lib_base.base.BaseFragment
import me.parade.lib_base.base.EmptyViewModel
import me.parade.wanandroid.R
import me.parade.wanandroid.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding,EmptyViewModel>() {



    private lateinit var childAdapter: HomeChildFragmentAdapter

    override fun initView(savedInstanceState: Bundle?) {
        val items = listOf(
            getString(R.string.title_home),
            getString(R.string.tab_home_square),
            getString(R.string.tab_home_answer)
        )
        childAdapter = HomeChildFragmentAdapter(items,childFragmentManager,lifecycle)
        binding.apply {
            with(homeVp2){
                adapter = childAdapter
            }
            TabLayoutMediator(homeTab,homeVp2){ tab: TabLayout.Tab, position: Int ->
                tab.text = items[position]
            }.also { it.attach() }
        }
    }
}