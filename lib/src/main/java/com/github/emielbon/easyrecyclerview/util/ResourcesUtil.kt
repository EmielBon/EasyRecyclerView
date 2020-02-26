package com.github.emielbon.easyrecyclerview.util

import android.content.res.Resources
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP

internal fun Resources.dp(value: Float): Float =
    TypedValue.applyDimension(COMPLEX_UNIT_DIP, value, displayMetrics)

internal fun Resources.dp(value: Int): Int = dp(value.toFloat()).toInt()