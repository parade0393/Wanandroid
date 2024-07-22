package me.parade.lib_base

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<VM:ViewBinding> :AppCompatActivity(){

    protected lateinit var binding: VM
    private val TAG = "parade0393"

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

    private fun createBinding(): VM {
        val bindingClass = (javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<VM>

        return when {
            ViewDataBinding::class.java.isAssignableFrom(bindingClass) -> {
                DataBindingUtil.setContentView(this, getLayoutResId()) as VM
            }
            ViewBinding::class.java.isAssignableFrom(bindingClass) -> {
                val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)
                inflateMethod.invoke(null, layoutInflater) as VM
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