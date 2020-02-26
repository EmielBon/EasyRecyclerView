/**
 * MIT License
 *
 * Copyright (c) 2020 Emiel Bon
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.emielbon.easyrecyclerview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class EasyGridRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : EasyRecyclerView(context, attrs, defStyleAttr) {

    init {
        val defaultLayoutManager = UniformGridLayoutManager(this)
        val defaultSpacingItemDecoration = DefaultSpacingItemDecoration(interItemSpacing = 0)

        if (attrs != null) {
            val styledAttributes = context.theme.obtainStyledAttributes(attrs, R.styleable.EasyGridRecyclerView, 0, 0)
            defaultLayoutManager.numberOfColumns = styledAttributes.getInt(R.styleable.EasyGridRecyclerView_numberOfColumns, defaultLayoutManager.numberOfColumns)
            defaultSpacingItemDecoration.interItemSpacing = styledAttributes.getDimensionPixelOffset(R.styleable.EasyGridRecyclerView_interItemSpacing, 0)
            styledAttributes.recycle()
        }

        layoutManager = defaultLayoutManager
        addItemDecoration(defaultSpacingItemDecoration)
    }

    class UniformGridLayoutManager(val recyclerView: EasyGridRecyclerView) : GridLayoutManager(recyclerView.context, 3), LayoutManager {

        var numberOfColumns
            get() = spanCount
            set(value) {
                spanCount = value
            }

        init {
            // Section headers span the whole view width
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) = if (recyclerView.isSectionHeader(position)) numberOfColumns else 1
            }
        }

        override fun isAutoMeasureEnabled() = true

        override fun numberOfGridColumnsInSection(section: Int) = numberOfColumns

        override fun numberOfGridRowsInSection(section: Int) = (recyclerView.numberOfRowsInSection(section) - 1) / numberOfColumns + 1
    }

    class DefaultSpacingItemDecoration(var interItemSpacing: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(insets: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(insets, view, parent, state)

            if (parent !is EasyRecyclerView) {
                return
            }

            val layoutManager = parent.layoutManager as UniformGridLayoutManager

            val position = parent.getChildAdapterPosition(view)
            val isSectionHeader = parent.isSectionHeader(position)

            if (isSectionHeader) {
                return // Inter-item spacing does not apply to section headers
            }

            val numberOfColumns = layoutManager.numberOfColumns
            val totalInset = interItemSpacing * (numberOfColumns - 1)
            val insetPerItem = totalInset.toFloat() / numberOfColumns.toFloat()

            val indexPath = parent.indexPathForPosition(position)!!
            val column = layoutManager.gridColumnForIndexPath(indexPath)
            val row = layoutManager.gridRowForIndexPath(indexPath)
            val startInsetStep = insetPerItem / (numberOfColumns - 1)

            insets.set(
                (column * startInsetStep).roundToInt(),
                (row * startInsetStep).roundToInt(),
                (insetPerItem - column * startInsetStep).roundToInt(),
                (insetPerItem - row * startInsetStep).roundToInt()
            )
        }
    }

    interface LayoutManager {

        fun numberOfGridColumnsInSection(section: Int): Int

        fun numberOfGridRowsInSection(section: Int): Int

        fun isInFirstColumn(indexPath: IndexPath) = gridColumnForIndexPath(indexPath) == 0

        fun isInLastColumn(indexPath: IndexPath) = gridColumnForIndexPath(indexPath) == numberOfGridColumnsInSection(indexPath.section) - 1

        fun gridColumnForIndexPath(indexPath: IndexPath) = indexPath.row % numberOfGridColumnsInSection(indexPath.section)

        fun isInFirstRow(indexPath: IndexPath) = gridRowForIndexPath(indexPath) == 0

        fun isInLastRow(indexPath: IndexPath) = gridRowForIndexPath(indexPath) == numberOfGridRowsInSection(indexPath.section) - 1

        fun gridRowForIndexPath(indexPath: IndexPath) = indexPath.row / numberOfGridColumnsInSection(indexPath.section)

    }
}
