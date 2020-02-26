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

package com.github.emielbon.easyrecyclerview.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.emielbon.easyrecyclerview.EasyRecyclerView
import com.github.emielbon.easyrecyclerview.IndexPath
import com.github.emielbon.easyrecyclerview.MaterialRowViewHolder
import com.github.emielbon.easyrecyclerview.MaterialRowViewHolder.ImageStyle.SQUARE_SMALL
import com.github.emielbon.easyrecyclerview.MaterialRowViewHolder.MaterialStyle.TWO_LINES
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), EasyRecyclerView.DataSource, EasyRecyclerView.Delegate {

    private val items = (0..100).map { index ->
        Item(title = "Title $index", subtitle = "Subtitle $index")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        easyRecyclerView.dataSource = this
        easyRecyclerView.delegate = this
    }

    override fun numberOfRowsInSection(recyclerView: EasyRecyclerView, section: Int) = items.count()

    override fun prepareViewHolderForDisplay(recyclerView: EasyRecyclerView, viewHolder: EasyRecyclerView.ViewHolder, indexPath: IndexPath) {
        viewHolder as MaterialRowViewHolder

        val item = items[indexPath.row]

        with(viewHolder) {
            title = item.title
            subtitle = item.subtitle
            accessoryText = "15 min"
            style = TWO_LINES
            setImage(R.drawable.ic_person_black_24dp, SQUARE_SMALL)
        }
    }

    data class Item(
        val title: String,
        val subtitle: String
    )
}
