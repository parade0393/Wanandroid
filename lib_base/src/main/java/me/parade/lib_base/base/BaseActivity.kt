package me.parade.lib_base.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.viewbinding.ViewBinding
import me.parade.lib_base.R
import me.parade.lib_base.helper.ViewModelCreateHelper
import me.parade.lib_common.ext.logd
import me.parade.lib_common.ext.logw
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<DB:ViewBinding,VM: BaseViewModel> :AppCompatActivity(){
    protected lateinit var binding: DB
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeOnCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        binding = createBinding()

        viewModel = ViewModelCreateHelper.createViewModel(viewModelStore,javaClass,defaultViewModelCreationExtras,::provideParameter)
        // 只有在使用 ViewBinding 时才需要调用 setContentView
        if(binding !is ViewDataBinding){
            setContentView(binding.root)
        }
        // 如果是 Data Binding，设置 lifecycleOwner
        (binding as? ViewDataBinding)?.lifecycleOwner = this

        //初始化数据和逻辑
        initView(savedInstanceState)

        logd("资源id-${me.parade.lib_common.R.id.compatToolBar}","ViewDebug")
        printViewHierarchy(binding.root,0)
        //处理toolBar的逻辑
        val toolBar = findViewById<Toolbar>(me.parade.lib_common.R.id.compatToolBar)
        toolBar?.apply {
            setSupportActionBar(this)
            supportActionBar?.apply {
                //标题
                setDisplayShowTitleEnabled(false)
                //返回按钮
                setDisplayHomeAsUpEnabled(false)
            }
            findViewById<TextView>(me.parade.lib_common.R.id.toolbarBack)?.let {
                setOnClickListener {
                    //处理返回逻辑
                    handleNavigationClick()
                }
            }

            updateStatusBarAppearance(true)
        }

    }

    /**
     * 在执行onCreate之前执行一些逻辑
     */
    open fun beforeOnCreate(savedInstanceState: Bundle?) {

    }

    @Suppress("UNCHECKED_CAST")
    private fun createBinding(): DB {
        val bindingClass = (javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<DB>

        return when {
            ViewDataBinding::class.java.isAssignableFrom(bindingClass) -> {
                DataBindingUtil.setContentView(this, getLayoutResId()) as DB
            }
            ViewBinding::class.java.isAssignableFrom(bindingClass) -> {
                val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)
                inflateMethod.invoke(null, layoutInflater) as DB
            }
            else -> throw IllegalArgumentException("Unsupported ViewBinding type")
        }
    }


    /**
     * 绑定布局
     */
    abstract fun getLayoutResId(): Int
    /**
     * 做一些初始化操作
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * private fun provideParameter(paramName: String, paramType: Class<*>): Any? {
     *         return when (paramName) {
     *             "repository" -> MyRepository() // 提供 MyRepository 实例
     *             "someParameter" -> "这是一个参数" // 提供自定义字符串参数
     *             else -> null // 其他参数返回 null
     *         }
     *     }
     */
    protected open fun provideParameter(paramName: String, paramType: Class<*>): Any? {
        // 默认实现返回 null，子类应该重写这个方法来提供自定义参数
        logw(
            "provideParameter not overridden for parameter: $paramName of type $paramType",
            "BaseActivity"
        )
        return null
    }

    // 提供一个方法给子类重写返回逻辑
    protected open fun handleNavigationClick() {
        onBackPressedDispatcher.onBackPressed()
    }

    /**
     * 设置状态栏字体颜色
     * @param isDark true代表深色字体，false代表浅色字体
     */
    fun updateStatusBarAppearance(isDark:Boolean){
       window?.let { window: Window ->
            val insetsController = WindowCompat.getInsetsController(window,window.decorView)
            insetsController.isAppearanceLightStatusBars = isDark
        }
    }

    open fun getToolBar():Toolbar?{
        return null
    }

    // 用于调试的辅助方法
    private fun printViewHierarchy(view: View, depth: Int) {
        val indent = " ".repeat(depth * 2)
        logd("$indent${view.javaClass.simpleName} - id: ${view.id}","ViewDebug")
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                printViewHierarchy(view.getChildAt(i), depth + 1)
            }
        }
    }
}