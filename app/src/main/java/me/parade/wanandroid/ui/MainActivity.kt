package me.parade.wanandroid.ui

import android.os.Bundle
import android.view.MenuItem
import me.parade.lib_base.base.BaseActivity
import me.parade.wanandroid.R
import me.parade.wanandroid.TestMainViewModel
import me.parade.wanandroid.databinding.ActivityMainBinding
import me.parade.wanandroid.ui.group.GroupFragment
import me.parade.wanandroid.ui.home.HomeFragment
import me.parade.wanandroid.ui.navigator.NavigatorFragment
import me.parade.wanandroid.ui.profile.ProfileFragment
import me.parade.wanandroid.ui.project.ProjectFragment

class MainActivity : BaseActivity<ActivityMainBinding, TestMainViewModel>() {


    private var activeFragmentIndex = -1

    private var fragmentList = listOf(
        HomeFragment(),
        ProjectFragment(),
        NavigatorFragment(),
        GroupFragment(),
        ProfileFragment()
    )

    override fun getLayoutResId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        switchFragment(0)
        binding.navView.setOnItemSelectedListener(NavBottomViewDoubleClickListener(this::onBottomItemSelect,this::onBottomDoubleClick))
    }

    private fun onBottomItemSelect(item: MenuItem): Boolean {
        switchFragment(getFragmentIndexFromItemId(item.itemId))
        return true
    }

    private fun onBottomDoubleClick(item: MenuItem) {

    }

    private fun switchFragment(fragmentIndex: Int) {
        if (fragmentIndex != activeFragmentIndex) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val fragment = fragmentList[fragmentIndex]
            fragmentList.getOrNull(activeFragmentIndex)?.apply(fragmentTransaction::hide)
            if (!fragment.isAdded) {
                fragmentTransaction
                    .add(
                        R.id.nav_host_fragment_container,
                        fragment,
                        fragment.javaClass.simpleName
                    )
                    .show(fragment)
            } else {
                fragmentTransaction.show(fragment)
            }
            fragmentTransaction.commitAllowingStateLoss()
            activeFragmentIndex = fragmentIndex
        }
    }

    private fun getTagFromItemId(itemId: Int) = fragmentList[getFragmentIndexFromItemId(itemId)].tag
        ?: HomeFragment::class.java.simpleName

    private fun getFragmentIndexFromItemId(itemId: Int): Int {
        return when (itemId) {
            R.id.navigation_home -> 0
            R.id.navigation_project -> 1
            R.id.navigation_navigator -> 2
            R.id.navigation_we_chat_group -> 3
            R.id.navigation_profile -> 4
            else -> 0
        }
    }
}