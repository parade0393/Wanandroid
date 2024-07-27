package me.parade.lib_base

import android.os.Bundle
import android.view.LayoutInflater
//import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<DB:ViewBinding,VM:BaseViewModel> :AppCompatActivity(){
    protected lateinit var binding: DB
    protected val viewModel:VM by lazy {
        @Suppress("UNCHECKED_CAST")
        val vmClass = (javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[1] as Class<VM>
        //这里 [vmClass] 实际上是调用了 ViewModelProvider 的 get() 方法 它等同于 ViewModelProvider(this, createViewModelFactory()).get(vmClass)
        ViewModelProvider(this, createViewModelFactory())[vmClass]
    }

    private fun createViewModelFactory():ViewModelProvider.Factory {
       return object :ViewModelProvider.Factory{
           override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
               @Suppress("UNCHECKED_CAST")
               return createViewModel(modelClass as Class<VM>, extras) as T
           }
       }
    }

    protected open fun createViewModel(
        modelClass: Class<VM>,
        extras: CreationExtras
    ): VM {
        return modelClass.getConstructor()
            .newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createBinding()
        // 只有在使用 ViewBinding 时才需要调用 setContentView
        if(binding !is ViewDataBinding){
            setContentView(binding.root)
        }
        // 如果是 Data Binding，设置 lifecycleOwner
        (binding as? ViewDataBinding)?.lifecycleOwner = this
        //初始化数据和逻辑
        initView(savedInstanceState)
    }

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
}