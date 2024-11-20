package me.parade.lib_base.net.service

// 1. 定义一个自定义注解,用来编辑是否是下载文件
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class FileDownload