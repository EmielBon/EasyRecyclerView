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
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class EasyRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr) {


    /*--------------------------------------- Properties -----------------------------------------*/


    var dataSource: DataSource? = null
    var delegate: Delegate? = null

    var numberOfSections = 1
        private set

    private var numberOfRowsBySection: MutableMap<Int, Int> = mutableMapOf()
    private var dividerItemDecoration: DividerItemDecoration? = null

    var dividerLeftInset = 0
        set(value) {
            field = value
            dividerItemDecoration?.dividerLeftInset = value
        }

    var dividerRightInset = 0
        set(value) {
            field = value
            dividerItemDecoration?.dividerRightInset = value
        }


    /*---------------------------------------- Lifecycle -----------------------------------------*/


    init {

        layoutManager = LinearLayoutManager(context)
        itemAnimator = null

        val adapter = Adapter()

        if (attrs != null) {
            val styledAttributes = context.theme.obtainStyledAttributes(attrs, R.styleable.EasyRecyclerView, 0, 0)
            val dividerStyle = styledAttributes.getInt(R.styleable.EasyRecyclerView_dividerStyle, DIVIDER_STYLE_BASIC)
            if (dividerStyle == DIVIDER_STYLE_BASIC) {
                dividerItemDecoration = DividerItemDecoration(context)
                dividerLeftInset = styledAttributes.getDimensionPixelOffset(R.styleable.EasyRecyclerView_dividerLeftInset, 0)
                dividerRightInset = styledAttributes.getDimensionPixelOffset(R.styleable.EasyRecyclerView_dividerRightInset, 0)
                addItemDecoration(dividerItemDecoration!!)
            }
            val enableAggressiveRecycling = styledAttributes.getBoolean(R.styleable.EasyRecyclerView_aggressiveRecycling, true)
            adapter.setHasStableIds(!enableAggressiveRecycling)
            layoutManager = object : LinearLayoutManager(context) {
                override fun supportsPredictiveItemAnimations() = !enableAggressiveRecycling
            }

            styledAttributes.recycle()
        }

        this.adapter = adapter
    }


    /*-------------------------------------- RecyclerView ----------------------------------------*/


    override fun onInterceptTouchEvent(e: MotionEvent?) = if (isEnabled) super.onInterceptTouchEvent(e) else true

    override fun onTouchEvent(e: MotionEvent?) = if (isEnabled) super.onTouchEvent(e) else false


    /*------------------------------------- Member methods ---------------------------------------*/


    fun notifyDataSetChanged() {
        numberOfSections = dataSource?.numberOfSections(this) ?: 1
        numberOfRowsBySection = mutableMapOf()
        adapter!!.notifyDataSetChanged()
    }

    fun numberOfRowsInSection(section: Int): Int {
        val numberOfRows = numberOfRowsBySection[section] ?: dataSource?.numberOfRowsInSection(this, section) ?: 0
        numberOfRowsBySection[section] = numberOfRows
        return numberOfRows
    }

    fun isSectionHeader(position: Int): Boolean {
        var currentPos = 0
        for (section in 0 until numberOfSections) {
            if (currentPos == position) {
                return true
            }
            currentPos += (1 + numberOfRowsInSection(section))
        }
        return false
    }

    fun positionForHeaderInSection(section: Int): Int {
        var position = 0
        (0 until section).forEach {
            position += numberOfRowsInSection(it) + 1
        }
        return position
    }

    fun sectionForPosition(position: Int): Int {
        (0 until numberOfSections).forEach { section ->
            val sectionHeaderPosition = positionForHeaderInSection(section)
            val numberOfRowsInSection = numberOfRowsInSection(section)
            val lastPositionInSection = sectionHeaderPosition + numberOfRowsInSection
            if (position in sectionHeaderPosition..lastPositionInSection) {
                return section
            }
        }
        return 0
    }

    fun indexPathForPosition(position: Int): IndexPath? {

        if (position == NO_POSITION) {
            return null
        }

        var pos = position
        for (section in 0 until numberOfSections) {
            val numberOfRowsInSection = numberOfRowsInSection(section)
            pos -= 1 // Section
            if (pos >= numberOfRowsInSection) {
                pos -= numberOfRowsInSection
                continue
            }
            return if (pos >= 0 && section >= 0) IndexPath(row = pos, section = section) else null
        }
        return null
    }

    fun positionForIndexPath(indexPath: IndexPath): Int {
        return positionOfFirstRowInSection(indexPath.section) + indexPath.row
    }

    fun indexPathForViewHolder(holder: ViewHolder): IndexPath? {
        return indexPathForPosition(holder.adapterPosition)
    }

    fun viewHolderAtIndexPath(indexPath: IndexPath): ViewHolder? {
        val position = positionForIndexPath(indexPath)
        return findViewHolderForAdapterPosition(position) as ViewHolder
    }

    fun scrollTo(indexPath: IndexPath) {
        val position = positionForIndexPath(indexPath)
        smoothScrollToPosition(position)
    }

    fun scrollToTop() {
        smoothScrollToPosition(0)
    }


    /*------------------------------------ Private methods ---------------------------------------*/


    private fun positionOfFirstRowInSection(section: Int): Int {
        return positionForHeaderInSection(section) + 1
    }


    /*---------------------------------------- Classes -------------------------------------------*/


    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var indexPath: IndexPath? = null
            internal set

        open fun prepareForDisplay() {

        }

        open fun prepareForReuse() {

        }
    }

    private inner class Adapter : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(context)
            if (viewType == SECTION_TYPE) {
                val itemView = dataSource?.sectionView(this@EasyRecyclerView, inflater, parent) ?: inflater.inflate(R.layout.erv_row_section, parent, false)
                return dataSource?.viewHolderForSectionView(this@EasyRecyclerView, itemView) ?: SectionRowViewHolder(itemView)
            } else {
                val itemView = dataSource!!.itemViewForViewType(this@EasyRecyclerView, viewType, inflater, parent)
                val viewHolder = dataSource!!.viewHolderForItemView(this@EasyRecyclerView, itemView, viewType)
                viewHolder.itemView.setOnClickListener {
                    val position = viewHolder.adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val indexPath = indexPathForPosition(position)!!
                        delegate!!.onRowSelected(this@EasyRecyclerView, viewHolder, indexPath)
                    }
                }
                return viewHolder
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (isSectionHeader(position)) {
                val section = sectionForPosition(position)
                if (holder is SectionRowViewHolder) {
                    holder.title = if (numberOfRowsInSection(section) > 0) dataSource?.titleForHeaderInSection(section) else null
                }
                delegate?.prepareSectionViewForDisplay(this@EasyRecyclerView, holder, section)
            } else {
                val indexPath = indexPathForPosition(position)!!
                holder.indexPath = indexPath
                delegate?.prepareViewHolderForDisplay(this@EasyRecyclerView, holder, indexPath)
                holder.prepareForDisplay()
            }
        }

        override fun getItemCount(): Int {
            return (0 until numberOfSections).sumBy { section -> 1 + numberOfRowsInSection(section) }
        }

        override fun getItemViewType(position: Int): Int {
            if (isSectionHeader(position)) {
                return SECTION_TYPE
            } else {
                val indexPath = indexPathForPosition(position)!!
                return dataSource?.viewTypeForRow(this@EasyRecyclerView, indexPath) ?: 0
            }
        }

        override fun onViewRecycled(holder: ViewHolder) {
            delegate?.prepareViewHolderForReuse(this@EasyRecyclerView, holder)
            holder.prepareForReuse()
        }

        override fun getItemId(position: Int): Long {
            val indexPath = indexPathForPosition(position)
            return if (indexPath == null) { // section
                dataSource?.stableIdForSectionHeader(this@EasyRecyclerView, sectionForPosition(position)) ?: super.getItemId(position)
            } else { // regular row
                dataSource?.stableIdForRow(this@EasyRecyclerView, indexPath) ?: super.getItemId(position)
            }
        }
    }

    interface DataSource {

        fun numberOfSections(recyclerView: EasyRecyclerView): Int {
            return 1
        }

        fun sectionView(recyclerView: EasyRecyclerView, inflater: LayoutInflater, parent: ViewGroup): View {
            return inflater.inflate(R.layout.erv_row_section, parent, false)
        }

        fun viewHolderForSectionView(recyclerView: EasyRecyclerView, itemView: View): ViewHolder {
            return SectionRowViewHolder(itemView)
        }

        fun titleForHeaderInSection(section: Int): String? {
            return null
        }

        fun numberOfRowsInSection(recyclerView: EasyRecyclerView, section: Int): Int {
            return 0
        }

        fun viewTypeForRow(recyclerView: EasyRecyclerView, indexPath: IndexPath): Int {
            return R.layout.erv_row_material_list_item
        }

        fun itemViewForViewType(recyclerView: EasyRecyclerView, viewType: Int, inflater: LayoutInflater, parent: ViewGroup): View {
            return inflater.inflate(viewType, parent, false)
        }

        fun viewHolderForItemView(recyclerView: EasyRecyclerView, itemView: View, viewType: Int): ViewHolder {
            return MaterialRowViewHolder(itemView)
        }

        fun stableIdForRow(recyclerView: EasyRecyclerView, indexPath: IndexPath): Long {
            // https://stackoverflow.com/questions/27300811/recyclerview-adapter-notifydatasetchanged-stops-fancy-animation
            throw NotImplementedError("stableIdForRow not implemented.")
        }

        fun stableIdForSectionHeader(recyclerView: EasyRecyclerView, section: Int): Long {
            return RecyclerView.NO_ID - 1 - section
        }
    }

    interface Delegate {

        fun prepareSectionViewForDisplay(recyclerView: EasyRecyclerView, viewHolder: ViewHolder, section: Int) {

        }

        fun prepareViewHolderForDisplay(recyclerView: EasyRecyclerView, viewHolder: ViewHolder, indexPath: IndexPath) {

        }

        fun onRowSelected(recyclerView: EasyRecyclerView, viewHolder: ViewHolder, indexPath: IndexPath) {

        }

        fun prepareViewHolderForReuse(recyclerView: EasyRecyclerView, viewHolder: ViewHolder) {

        }

    }


    /*----------------------------------------- Static -------------------------------------------*/


    companion object {
        const val DIVIDER_STYLE_BASIC = 0
        const val DIVIDER_STYLE_NONE = 1

        const val SECTION_TYPE = 100
    }
}
