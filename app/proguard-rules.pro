#########通用规则#######################

-dontusemixedcaseclassnames

-verbose
-printmapping priguardMapping.txt

-optimizations !code/simplification/artithmetic,!field/*,!class/merging/*

# 保护泛型
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod
# 保护泛型参数
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod,AnnotationDefault,RuntimeVisible*Annotations
# 保护注解
-keepattributes *Annotation*

# 保留四大组件，自定义的Application等这些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Service
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.preference.Preference

#kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}
-keep class kotlinx.coroutines.android.** {*;}
# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

#AndroidX混淆开始
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**
-keep class * extends androidx.lifecycle.ViewModel.**
#AndroidX混淆结束

# 避免混淆自定义控件类的 get/set 方法和构造函数
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
# 避免资源混淆
-keep class **.R$* {*;}

# 避免Serializable接口的子类中指定的某些成员变量和方法混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# WebView混淆配置
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
}
-keepattributes JavascriptInterface

# 避免混淆枚举类
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# Natvie 方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
# 避免Parcelable混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

##Gson
-keep class sun.misc.Unsafe { *; }
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
## Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * implements com.bumptech.glide.module.LibraryGlideModule
-keep class com.bumptech.glide.** { *; }
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

#Okhttp
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn com.squareup.okhttp.**
#Rxjava RxAndroid
-keep class com.zhy.autolayout.** { *; }
-keep interface com.zhy.autolayout.** { *; }

# 保护所有ViewBinding类
-keep class * implements androidx.viewbinding.ViewBinding {
    public static *** inflate(android.view.LayoutInflater);
    public static *** inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
    public static *** bind(android.view.View);
}

# 保护反射用到的方法
-keepclasseswithmembers class * {
    public static ** inflate(android.view.LayoutInflater);
    public static ** inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
    public static ** bind(android.view.View);
}

# 保护反射需要用到的信息
-keepclasseswithmembers class * {
    public <init>(...);
}
-keepclassmembers class * {
    *** get*();
    void set*(***);
}

# 保护ViewBinding和DataBinding相关类
-keep class androidx.viewbinding.** { *; }
-keep class androidx.databinding.** { *; }

# 如果使用了DataBinding，还需要添加
-keepclassmembers class * implements androidx.databinding.ViewDataBinding {
    public static ** inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
    public static ** inflate(android.view.LayoutInflater);
    public static ** bind(android.view.View);
}

# 保护所有 ViewModel 的构造函数和参数
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# 保护 ViewModelStore 相关
-keep class androidx.lifecycle.ViewModelStore { *; }
-keep class androidx.lifecycle.ViewModelProvider { *; }
-keep class androidx.lifecycle.ViewModelProvider$Factory { *; }

# 保护 CreationExtras 相关
-keep class androidx.lifecycle.viewmodel.CreationExtras { *; }
-keep class androidx.lifecycle.viewmodel.MutableCreationExtras { *; }

# 保护基本数据类型和包装类型
-keepclassmembers class * {
    primitive <fields>;
    private primitive <fields>;
    public primitive <fields>;
    protected primitive <fields>;
}

# 保护 Application 相关
-keep class android.app.Application { *; }
-keep class * extends android.app.Application { *; }

#Banner
-keep class com.youth.banner.** {*;}

# Retrofit + Gson
-keepattributes Exceptions
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

#########通用规则#######################


#保证实体类不被混淆
-keep class me.parade.wanandroid.dsl.**{*;}
-keep class me.parade.wanandroid.net.model.**{*;}
-keepclassmembers class me.parade.wanandroid.net.model.** { *; }
-keep class me.parade.lib_base.net.BaseResponse{*;}
-keep class me.parade.lib_base.net.BaseResponse$* { *; }
-keepclassmembers class me.parade.lib_base.net.BaseResponse { *; }
#-keep class me.parade.wanandroid.net.service.**{*;}
#keep interface me.parade.wanandroid.net.service.**{*;}
-keep class me.parade.wanandroid.net.model.PageResponse$* { *; }
-keepclassmembers class me.parade.wanandroid.net.model.PageResponse { *; }


# 避免回调函数 onXXEvent 混淆
-keepclassmembers class * {
    void *(*Event);
}

-keep class me.parade.wanandroid.databinding.**{
*;
}
#保护基类
-keep class me.parade.lib_base.base.BaseActivity{
    *;
}
-keep class me.parade.lib_base.base.BaseFragment {
 *;
}
# 保护 BaseViewModel 及其子类
-keep class me.parade.lib_base.base.BaseViewModel { *; }
-keep class * extends me.parade.lib_base.base.BaseViewModel { *; }

# 保护 ViewModelCreateHelper
-keep class me.parade.lib_base.helper.ViewModelCreateHelper { *; }

