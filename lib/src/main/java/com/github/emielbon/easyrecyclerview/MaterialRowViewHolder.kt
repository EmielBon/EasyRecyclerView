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

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.github.emielbon.easyrecyclerview.MaterialRowViewHolder.ImageStyle.*
import com.github.emielbon.easyrecyclerview.MaterialRowViewHolder.MaterialStyle.*
import com.github.emielbon.easyrecyclerview.util.*
import kotlinx.android.synthetic.main.erv_row_material_list_item.view.*


open class MaterialRowViewHolder(itemView: View) : EasyRecyclerView.ViewHolder(itemView) {


    /*---------------------------------------- Properties ----------------------------------------*/


    val imageView
        get() = itemView.iconImageView!!

    val titleTextView
        get() = itemView.titleTextView!!

    val subtitleTextView
        get() = itemView.subtitleTextView!!

    val accessoryTextView
        get() = itemView.accessoryTextView!!

    val accessoryImageView
        get() = itemView.accessoryImageView!!

    var title: String?
        get() = titleTextView.text.toString()
        set(title) {
            titleTextView.text = title
        }

    var subtitle: String?
        get() = subtitleTextView.text.toString()
        set(subtitle) {
            subtitleTextView.text = subtitle
            updateUI()
        }

    var accessoryText: String?
        get() = accessoryTextView.text.toString()
        set(accessoryText) {
            accessoryTextView.text = accessoryText
            accessoryTextView.isVisible = true
        }

    var style: MaterialStyle = SINGLE_LINE
        set(value) {
            field = value
            updateUI()
        }

    private var imageStyle = NONE
        set(value) {
            field = value
            updateUI()
        }

    private val titleTextViewStartMargin
        get() = when {
            style == THREE_LINES && imageStyle == RECTANGLE_LARGE -> dp(20)
            imageStyle == SQUARE_SMALL -> dp(32)
            else -> dp(16)
        }

    private val imageViewTopMargin
        get() = when {
            style == SINGLE_LINE && imageStyle in listOf(CIRCLE_MEDIUM, SQUARE_LARGE, RECTANGLE_LARGE) -> dp(8)
            style == TWO_LINES && imageStyle == RECTANGLE_LARGE -> dp(8)
            else -> dp(16)
        }

    private val imageViewHeight
        get() = when (imageStyle) {
            NONE -> dp(0)
            SQUARE_SMALL -> dp(24)
            CIRCLE_MEDIUM -> dp(40)
            SQUARE_LARGE -> dp(56)
            RECTANGLE_LARGE -> dp(56)
        }

    private val imageViewWidth
        get() = if (imageStyle == RECTANGLE_LARGE) dp(100) else imageViewHeight

    private val rowHeight
        get() = when (style) {
            SINGLE_LINE -> when (imageStyle) {
                SQUARE_SMALL, CIRCLE_MEDIUM -> dp(56)
                SQUARE_LARGE, RECTANGLE_LARGE -> dp(72)
                else -> dp(48)
            }
            TWO_LINES -> when (imageStyle) {
                NONE -> dp(72)
                else -> dp(64)
            }
            THREE_LINES -> dp(88)
        }

    private val viewPaddingStart
        get() = if (imageStyle == RECTANGLE_LARGE) dp(0) else dp(16)


    /*------------------------------------------ Methods -----------------------------------------*/


    fun setImage(@DrawableRes iconRes: Int, imageStyle: ImageStyle) {
        this.imageStyle = imageStyle
        imageView.setImageResource(iconRes)
        imageView.isVisible = true
    }

    fun setTitle(@StringRes titleRes: Int) {
        titleTextView.setText(titleRes)
    }

    fun setSubtitle(@StringRes subtitleRes: Int) {
        subtitleTextView.setText(subtitleRes)
        subtitleTextView.isVisible = true
    }

    fun setAccessoryIcon(@DrawableRes iconRes: Int) {
        accessoryImageView.setImageResource(iconRes)
        accessoryImageView.isVisible = true
    }


    /*-------------------------------------- Private methods -------------------------------------*/


    private fun updateUI() {

        itemView.layoutHeight = rowHeight

        imageView.marginLeft = viewPaddingStart
        imageView.marginTop = imageViewTopMargin
        imageView.layoutHeight = imageViewHeight
        imageView.layoutWidth = imageViewWidth
        imageView.isVisible = imageStyle != NONE

        titleTextView.marginLeft = titleTextViewStartMargin

        subtitleTextView.isVisible = !subtitle.isNullOrEmpty()
        subtitleTextView.setLines(if (style == THREE_LINES) 2 else 1)
    }


    /*------------------------------------------- Enums ------------------------------------------*/


    enum class MaterialStyle {
        SINGLE_LINE,
        TWO_LINES,
        THREE_LINES
    }

    enum class ImageStyle {
        NONE,
        SQUARE_SMALL,
        CIRCLE_MEDIUM,
        SQUARE_LARGE,
        RECTANGLE_LARGE,
    }

}
