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
