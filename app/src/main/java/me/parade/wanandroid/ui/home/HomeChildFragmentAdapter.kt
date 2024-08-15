package me.parade.wanandroid.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.parade.wanandroid.ui.home.child.answer.AnswerFragment
import me.parade.wanandroid.ui.home.child.explore.ExploreFragment
import me.parade.wanandroid.ui.home.child.square.SquareFragment

class HomeChildFragmentAdapter(
    private var items: List<String>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount() = items.size

    override fun createFragment(position: Int): Fragment {
       return when (position){
           0 -> ExploreFragment.newInstance()
           1 -> SquareFragment.newInstance()
           2 -> AnswerFragment.newInstance()
           else -> ExploreFragment.newInstance()
       }
    }

}