1. dataExtractionRules和fullBackupContent对于我们一般的应用来说没啥用,是用来指定备份数据的,前者使用Android12以后的版本，后者使用Android6-Android11的版本
2. DataBinding依赖kapt，所以使用DataBinding需要添加kapt插件，而ViewBinding不需要
3. agp8.0以上默认关闭了BuildConfig，需要再buildFeature里开启
4. BottomNavigation改造-使用两套图
    * 代码设置itemIconTintList为null
    * xml设置itemTextColor的资源为StateColor
    * xml设置itemActiveIndicatorStyle为transparent
    * menu里设置icon为StateDrawable
    * 当然也可以使用tint达到两套图的效果，此时代码无需设置itemIconTintList，xml需要设置itemIconTint和itemTextColor
5. 保存相册
大致流程就是这样子，只是不同的系统版本有一些细微的差距；
Android 10 之前的版本需要申请存储权限，Android 10及以后版本是不需要读写权限的
Android 10 之前是通过File路径打开流的，所以需要判断文件是否已经存在，否者的话会将以存在的图片给覆盖；Android10之后先使用MediaStore将图片记录插入媒体库，获得插入的Uri； 然后通过插入Uri打开输出流将文件写入；
Android 10 及以后版本添加了IS_PENDING状态标识，为0时其他应用才可见，所以在图片保存过后需要更新这个标识。

6. flow
collect 是一个挂起函数，它会一直收集流，直到流被关闭或者协程被取消
StateFlow 总是有一个当前值。当您重新订阅它时，它会立即发出最新的值 
当你使用 Flow 时，所有 emit 的调用必须在同一个协程上下文中进行
callbackFlow 可以在协程外部发射值，同时保证 Flow 的透明性。callbackFlow 本质上是一个安全的 Channel
flow在ac或者frag里的不同使用
```kotlin
//stateFlow
lifecycleScope.launch {
   repeatOnLifecycle(Lifecycle.State.STARTED){
      viewModel.banners.collect{
         //针对一次性数据(常见的UI场景,接口返回数据然后更新UI)重新回到前台后会重新执行collect,会造成UI浪费
          //针对流式数据，比如定时器或者一直定位的数据，重新回到前台后collect到最新的数据，在后台不会更新UI
         binding.banner.setDatas(it)
      }
   }
}
viewModel.stateFlow.collect {
   //退到后台后flow会产生新数据，但是重新回到前台后这里收不到
   if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
      binding.tvTime.text = it.toString()
      "update：$it in UI".logd()
   }
}
//使用LiveData没有上述两个问题，感觉挺完美，目前还没遇到粘性带来的坑
//SharedFlow sharedFlow可用作EventBus来用? 并且sharedFlow无法获取其值
lifecycleScope.launch {
   repeatOnLifecycle(Lifecycle.State.STARTED){
      viewModel.banners.collect{
         //针对一次性数据(常见的UI场景,接口返回数据然后更新UI)后台更新数据后重新回到前台不会更新UI
         //针对流式数据，比如定时器或者一直定位的数据，重新回到前台后collect到最新的数据，在后台不会更新UI
         binding.banner.setDatas(it)
      }
   }
}
```
7. LiveData
   LiveData的粘性机制--说的通俗一点，就是先发送数据，后订阅，也可以接收到数据。
   使用LiveData，可以有初始值，也可以没有初始值，如果有初始值，那么mVersion会加1，那么还没有setValue，观察者就可以收到值
8. 主题
   ?colorOnSurface--这种写法直接引用了当前主题中定义的属性
   ?android:attr/selectableItemBackground--这种写法引用了Android框架主题中定义的属性。android:前缀表示这是来自Android系统的属性，而不是来自你的应用或第三方库
   ?attr/colorPrimary--这种写法引用了当前主题中定义的属性。attr/前缀是可选的，所以这等同于 ?
   ?[package]:attr/attributeName--这种写法允许你引用特定包中定义的属性。例如?com.google.android.material:attr/colorSurface：引用Material Design库中定义的属性
9. gradle 8.0
   配置maven仓库需要这样
   ```properties
   maven {
   url = uri("https://jitpack.io")
   }
   ```
10. lambda
    Lambda 表达式的完整语法形式如下：
   val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
   lambda 表达式总是括在花括号中。
   完整语法形式的参数声明放在花括号内，并有可选的类型标注。
   函数体跟在一个 -> 之后。
   如果推断出的该 lambda 的返回类型不是 Unit，那么该 lambda 主体中的最后一个（或可能是单个）表达式会视为返回值。
   如果将所有可选标注都留下，看起来如下： val sum = { x: Int, y: Int -> x + y }
11. minSdkVersion 为 21 或更高版本,系统会默认启用 MultiDex，并且您不需要 MultiDex 库
12. 依赖声明
   * 避免使用 compileOnly 来处理 UI 相关的依赖，否则在xml里会报错，无法预览等其他问题
13. ConstrainLayout的充分使用

在 Android 中，@+id 和 @id 的区别是非常重要的：

@+id/name：


"+" 号表示定义一个新的资源 ID
在 R.java 文件中创建一个新的整型常量
通常在第一次声明某个 View 的 ID 时使用
相当于是声明和使用的结合


@id/name：


表示引用一个已经存在的资源 ID
必须确保这个 ID 已经在某处被定义过（使用 @+id 定义或在 ids.xml 中定义）
相当于仅仅是使用已存在的 ID
14. ConstrainLayout不常用的一些功能
   * GuildLine和Barrier，app:layout_constraintWidth_percent，layout_constraintDimensionRatio(要求是宽或高至少一个设为0dp)
     
res/anim/
用于存放视图动画（View Animation）文件
包括补间动画（Tween Animation）和帧动画（Frame Animation）
只能处理四种简单属性：平移（translate）、缩放（scale）、旋转（rotate）、透明度（alpha）
只改变视图的绘制效果，不会真正改变视图的属性
性能较好，占用资源少
使用 Animation 类
XML 文件的根元素通常是 <set>, <alpha>, <scale>, <translate>, <rotate>


res/animator/
用于存放属性动画（Property Animation）文件
可以处理任何对象的任何属性
真实改变对象的属性值
性能消耗相对较大，但功能更强大
使用 Animator 类
XML 文件的根元素通常是 <objectAnimator>, <valueAnimator>, <set>

使用建议：

使用 res/anim/ 的场景：
简单的视图动画效果（平移、旋转、缩放、透明度）
对性能要求较高的场景
不需要改变视图实际属性的场景
例如：对话框进入退出动画、列表项动画等


使用 res/animator/ 的场景：


需要动画改变视图实际属性的场景
需要动画自定义属性的场景
需要更复杂动画效果的场景
例如：

按钮点击反馈动画
复杂的交互动画
需要记住最终状态的动画
