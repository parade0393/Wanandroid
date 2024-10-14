package me.parade.lib_common.ext

/** 超链接正则 */
const val urlRegexStr:String = "(http|https)://[\\w.-]+(/[\\w./?%&=-]*)?"
val urlRegex = Regex("""(https?://)?([\w.-]+(?:\.[\w.-]+)+)(:\d+)?(/[\w./?%&=-]*)?""", RegexOption.IGNORE_CASE)