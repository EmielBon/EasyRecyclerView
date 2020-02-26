package com.github.emielbon.easyrecyclerview.util

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt

internal var View.layoutHeight
    get() = layoutParams.height
    set(value) {
        val params = layoutParams
        params.height = value
        layoutParams = params
    }

internal var View.layoutWidth
    get() = layoutParams.width
    set(value) {
        val params = layoutParams
        params.width = value
        layoutParams = params
    }

internal var View.marginParams: ViewGroup.MarginLayoutParams?
    get() = layoutParams as? ViewGroup.MarginLayoutParams
    set(value) {
        layoutParams = value
    }

internal var View.marginLeft
    get() = marginParams!!.leftMargin
    set(value) {
        val params = marginParams!!
        params.leftMargin = value
        layoutParams = params
    }

internal var View.marginTop
    get() = (layoutParams as ViewGroup.MarginLayoutParams).topMargin
    set(value) {
        val params = marginParams!!
        params.topMargin = value
        layoutParams = params
    }

internal var View.marginRight
    get() = (layoutParams as ViewGroup.MarginLayoutParams).rightMargin
    set(value) {
        val params = marginParams!!
        params.rightMargin = value
        layoutParams = params
    }

internal var View.marginBottom
    get() = (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin
    set(value) {
        val params = marginParams!!
        params.bottomMargin = value
        layoutParams = params
    }

internal var View.backgroundColor: Int?
    @ColorInt get() = (background as? ColorDrawable)?.color
    set(value) {
        background = if (value == null) null else ColorDrawable(value)
    }