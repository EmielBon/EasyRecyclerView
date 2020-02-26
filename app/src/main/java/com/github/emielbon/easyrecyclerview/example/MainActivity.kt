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
