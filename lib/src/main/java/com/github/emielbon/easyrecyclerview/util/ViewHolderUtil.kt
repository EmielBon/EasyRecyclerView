package com.github.emielbon.easyrecyclerview.util

import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

internal val RecyclerView.ViewHolder.resources
    get() = itemView.resources!!

internal val RecyclerView.ViewHolder.context
    get() = itemView.context!!

internal fun RecyclerView.ViewHolder.getString(@StringRes resId: Int?, vararg parameters: Any) =
    if (resId == null) null else resources.getString(resId, *parameters)

internal fun RecyclerView.ViewHolder.dp(value: Int) = resources.dp(value)