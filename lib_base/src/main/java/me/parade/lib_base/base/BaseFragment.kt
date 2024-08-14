package me.parade.lib_base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStore
import androidx.viewbinding.ViewBinding
import me.parade.lib_base.ext.logw
import me.parade.lib_base.helper.ViewModelCreateHelper
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<DB: ViewBinding,VM: BaseViewModel>:Fragment() {

    private var _binding: DB? = null
    protected val binding get() = _binding!!

    protected lateinit var viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding(container)
        viewModel = ViewModelCreateHelper.createViewModel(getCustomViewModelStore(),javaClass,defaultViewModelCreationExtras,::provideParameter)
        (_binding as? ViewDataBinding)?.lifecycleOwner = this
        return binding.root
    }

    @Suppress("UNCHECKED_CAST")
    private fun createBinding(container: ViewGroup?): DB {
        val bindingClass = (javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<DB>
            val method = bindingClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
            return method.invoke(null,layoutInflater,container,false) as DB
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
    }
    /** 初始化试图的一些操作，比如RecyclerView的初始化等 */
    open fun initView(savedInstanceState: Bundle?) {}

    protected open fun getCustomViewModelStore(): ViewModelStore {
        return viewModelStore
    }

    protected open fun provideParameter(paramName: String, paramType: Class<*>): Any? {
        // 默认实现返回 null，子类应该重写这个方法来提供自定义参数
        logw( "provideParameter not overridden for parameter: $paramName of type $paramType","BaseFragment")
        return null
    }
}