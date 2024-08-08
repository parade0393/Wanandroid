package me.parade.lib_base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<DB: ViewBinding,VM:BaseViewModel>:Fragment() {
    private val TAG = "BaseFragment"

    private var _binding: DB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding(container)
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
}