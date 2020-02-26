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
import android.view.ViewGroup
import kotlinx.android.synthetic.main.erv_row_section.view.*

class SectionRowViewHolder(itemView: View) : EasyRecyclerView.ViewHolder(itemView) {

    var title: String? = null
        set(value) {
            field = value
            itemView.sectionTitle.text = value
            // For some reason when the first row has a height of 0, it is not possible to pull
            // down the SwipeRefreshLayout. Therefore we give it a height of 1 -_-
            // TODO: We need to figure out why this happens and find a proper solution
            val params = itemView.layoutParams
            params.height = if (value == null) 1 else ViewGroup.LayoutParams.WRAP_CONTENT
            itemView.layoutParams = params
        }
}
