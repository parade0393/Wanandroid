package me.parade.lib_base.helper

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.VIEW_MODEL_KEY
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import me.parade.lib_base.base.BaseViewModel
import me.parade.lib_common.ext.loge
import java.lang.reflect.ParameterizedType

/**
 * ViewModel创建，包括构造函数里有参数的情况
 * 有SavedStateHandle和Application的参数会自动传值，其他参数需要使用者通过重写Base类的provideParameter方法自行赋值，
 * [viewModelClass]这种语法相当于get
 */
object ViewModelCreateHelper {
    fun <VM : BaseViewModel> createViewModel(
        owner: ViewModelStore,
        fragmentOrActivityClass: Class<*>,
        defaultExtras: CreationExtras,
        provideParameter: (paramName: String, paramType: Class<*>) -> Any?
    ): VM {
        val viewModelClass = getViewModelClass<VM>(fragmentOrActivityClass)
        return ViewModelProvider(
            owner,
            createViewModelFactory<VM>(defaultExtras,provideParameter)
        )[viewModelClass]
    }

    @Suppress("UNCHECKED_CAST")
    private fun <VM : BaseViewModel> getViewModelClass(fragmentOrActivityClass: Class<*>): Class<VM> {
        val type = (fragmentOrActivityClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments.find { BaseViewModel::class.java.isAssignableFrom(it as Class<*>) }
        return type as Class<VM>
    }

    private fun <VM : BaseViewModel> createViewModelFactory(
        defaultExtras: CreationExtras,
        provideParameter: (paramName: String, paramType: Class<*>) -> Any?
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val customExtras = MutableCreationExtras(defaultExtras)
                extras[VIEW_MODEL_KEY]?.let { value->
                    customExtras[VIEW_MODEL_KEY] = value
                }
                @Suppress("UNCHECKED_CAST")
                return createViewModelInstance(
                    modelClass as Class<VM>,
                    customExtras,
                    provideParameter
                ) as T
            }
        }
    }

    private fun <VM : BaseViewModel> createViewModelInstance(
        modelClass: Class<VM>,
        extras: CreationExtras,
        provideParameter: (paramName: String, paramType: Class<*>) -> Any?
    ): VM {
        try {
            // 尝试使用默认构造函数
            return modelClass.getConstructor().newInstance()
        } catch (e: Exception) {
            // 如果没有默认构造函数，尝试找到合适的构造函数
            val constructors = modelClass.constructors
            for (constructor in constructors) {
                try {
                    val params = mutableListOf<Any?>()
                    val parameterNames = constructor.parameterTypes.map { it.name }
                    for (i in constructor.parameterTypes.indices) {
                        val paramType = constructor.parameterTypes[i]
                        val paramName = parameterNames[i]
                        when {
                            SavedStateHandle::class.java.isAssignableFrom(paramType) -> {
                                params.add(extras.createSavedStateHandle())
                            }

                            Application::class.java.isAssignableFrom(paramType) -> {
                                var application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]

                                if(application == null){
                                    //如果获取不到，则让使用方提供
                                    application = provideParameter(paramName, paramType) as? Application
                                }
                                if(application == null){
                                    throw IllegalArgumentException("please provide the Application by override provideParameter")
                                }
                                params.add(application)
                            }

                            else -> {
                                val param = provideParameter(paramName, paramType)
                                if (param == null && !paramType.isPrimitive) {
                                    //对于有其他非基本类型参数的 ViewModel，这些参数会被设置为 null
                                    params.add(null)
                                } else if (param == null) {
                                    // 对于基本类型，如果提供的值为null，抛出异常
                                    throw IllegalArgumentException("Cannot provide null for primitive type $paramType for parameter $paramName")
                                } else {
                                    params.add(param)
                                }
                            }
                        }
                    }
                    @Suppress("UNCHECKED_CAST")
                    return constructor.newInstance(*params.toTypedArray()) as VM
                } catch (e: Exception) {
                    // 记录异常信息，但继续尝试下一个构造函数
                    loge(

                        "Failed to create ViewModel with constructor: ${constructor.toGenericString()}",
                        "ViewModelCreateHelper"
                    )
                    continue
                }
            }
            throw IllegalArgumentException("Unable to create ViewModel: ${modelClass.name}. Make sure to override provideParameter for custom parameters.")
        }
    }

}