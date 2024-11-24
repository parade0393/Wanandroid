package me.parade.lib_common.utils

object FileSizeFormatter {
    private const val BYTES_PER_KB = 1024L
    private const val BYTES_PER_MB = BYTES_PER_KB * 1024L
    private const val BYTES_PER_GB = BYTES_PER_MB * 1024L
    private const val BYTES_PER_TB = BYTES_PER_GB * 1024L

    /**
     * 将字节大小转换为易读的中文格式
     * @param bytes 字节数
     * @param decimals 小数位数，默认1位
     * @return 格式化后的字符串，如：1.5MB、834KB、1.2GB等
     */
    fun formatSize(bytes: Long, decimals: Int = 1): String {
        if (bytes <= 0) return "0B"
        
        return when {
            bytes < BYTES_PER_KB -> "${bytes}B"
            bytes < BYTES_PER_MB -> "${formatDecimal(bytes.toDouble() / BYTES_PER_KB, decimals)}KB"
            bytes < BYTES_PER_GB -> "${formatDecimal(bytes.toDouble() / BYTES_PER_MB, decimals)}MB"
            bytes < BYTES_PER_TB -> "${formatDecimal(bytes.toDouble() / BYTES_PER_GB, decimals)}GB"
            else -> "${formatDecimal(bytes.toDouble() / BYTES_PER_TB, decimals)}TB"
        }
    }

    /**
     * 格式化小数
     */
    private fun formatDecimal(number: Double, decimals: Int): String {
        return "%.${decimals}f".format(number)
    }
}