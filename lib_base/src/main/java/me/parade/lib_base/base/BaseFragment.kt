package me.parade.lib_base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.viewbinding.ViewBinding
import me.parade.lib_base.ext.logd
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
        initData()
        super.onViewCreated(view, savedInstanceState)
    }
    /** 初始化试图的一些操作，比如RecyclerView的初始化等 */
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun  initData()

    protected open fun getCustomViewModelStore(): ViewModelStore {
        return viewModelStore
    }

    protected open fun provideParameter(paramName: String, paramType: Class<*>): Any? {
        // 默认实现返回 null，子类应该重写这个方法来提供自定义参数
        logw( "provideParameter not overridden for parameter: $paramName of type $paramType","BaseFragment")
        return null
    }

    override fun onStart() {
        super.onStart()
        logd("onStart--${javaClass.simpleName}")
    }

    override fun onStop() {
        super.onStop()
        logd("onStop--${javaClass.simpleName}")
    }

    override fun onResume() {
        super.onResume()
        onVisible("onResume")
    }

    /**
     * 总是较先调用，比onStart和onResume靠前
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        onVisible("onHiddenChanged",hidden)
    }

    private fun onVisible(tag:String,hidden: Boolean = false){
        if(tag == "onResume"){
            if (isVisible){
                // onResume调用且可见时
                lazyLoad(tag)
            }
        }else{
            if(!hidden){
                if(lifecycle.currentState == Lifecycle.State.RESUMED){
                    //可见且处于RESUME状态时
                    lazyLoad(tag)
                }
            }
        }
    }
    open fun lazyLoad(tag: String){
        logd("${javaClass.simpleName}-loaded--$tag")
    }

    /**
     * 设置状态栏字体颜色
     * @param isDark true代表深色字体，false代表浅色字体
     */
    fun updateStatusBarAppearance(isDark:Boolean){
        requireActivity().window?.let { window: Window ->
            val insetsController = WindowCompat.getInsetsController(window,window.decorView)
            insetsController.isAppearanceLightStatusBars = isDark
        }
    }
}