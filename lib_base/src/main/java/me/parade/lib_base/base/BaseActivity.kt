package me.parade.lib_base.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import me.parade.lib_base.helper.ViewModelCreateHelper
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
    }

    /**
     * 在执行onCreate之前执行一些逻辑
     */
    open fun beforeOnCreate(savedInstanceState: Bundle?) {

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

    protected open fun provideParameter(paramName: String, paramType: Class<*>): Any? {
        // 默认实现返回 null，子类应该重写这个方法来提供自定义参数
        logw(
            "provideParameter not overridden for parameter: $paramName of type $paramType",
            "BaseActivity"
        )
        return null
    }
}