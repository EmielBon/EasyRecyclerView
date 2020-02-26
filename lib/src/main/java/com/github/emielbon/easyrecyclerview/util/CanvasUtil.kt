package com.github.emielbon.easyrecyclerview.util

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.annotation.ColorInt

internal fun Canvas.fillRect(rect: Rect, @ColorInt color: Int) {
    val paint = Paint()
    paint.style = Paint.Style.FILL
    paint.color = color
    drawRect(rect, paint)
}