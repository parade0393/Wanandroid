package me.parade.lib_common.ext

import android.content.res.Resources
import android.util.TypedValue

inline val Float.spF: Float
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            Resources.getSystem().displayMetrics
        )
    }

inline val Int.sp: Int
    get() {
        return this.toFloat().sp
    }

inline val Float.sp: Int
    get() {
        return spF.toInt()
    }

inline val Float.pxF:Float
    get() {
        val density = Resources.getSystem().displayMetrics.density
        return this.times(density)+0.5f
    }
inline val Int.px:Int
    get() {
       return toFloat().pxF.toInt()
    }