package me.parade.wanandroid

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import me.parade.lib_base.BaseActivity
import me.parade.lib_base.NoViewModel
import me.parade.wanandroid.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding,TestMainViewModel>() {



    override fun getLayoutResId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.count.observe(this){count->
            binding.tvTool.text = count.toString()
        }

        binding.btnAdd.setOnClickListener {
            viewModel.increment()
        }
        binding.btnSub.setOnClickListener{
            viewModel.decrement()
        }
    }

    override fun createViewModel(
        modelClass: Class<TestMainViewModel>,
        extras: CreationExtras
    ): TestMainViewModel {
        val savedStateHandle = extras.createSavedStateHandle()
        val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
        return TestMainViewModel(application,savedStateHandle)
    }
}