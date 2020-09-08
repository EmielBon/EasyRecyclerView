# EasyRecyclerView

## Features

* Single source of truth. There is no Adapter to put data into, EasyRecyclerView asks for only the data it needs to display a row, when it needs it.
* Built in support for Material Design list style using `MaterialRowViewHolder`
* Support for sections
  * `IndexPath`
  * `numberOfSections`, `numberOfRowsInSection`
  * Default material design style sections (`titleForHeaderInSection`)
  * Customizable (`sectionView`)
* Handle selection (`onRowSelected`)
* Row dividers (`app:dividerStyle=none|basic`, `app:dividerLeftInset`, `app:dividerRightInset`)

```xml
<com.github.emielbon.easyrecyclerview.EasyRecyclerView
    android:id="@+id/contactRecyclerView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:dividerLeftInset="16dp"
    app:dividerStyle="basic" />
```

## 

```kotlin
class MainActivity : AppCompatActivity(), EasyRecyclerView.DataSource, EasyRecyclerView.Delegate {

    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        easyRecyclerView.dataSource = this
        easyRecyclerView.delegate = this
    }

    /*---------------------------------------- DataSource ----------------------------------------*/

    override fun numberOfSections(recyclerView: EasyRecyclerView) {
        // Optional, return number of sections, defaults to 1
    }

    override fun titleForHeaderInSection(section: Int): String? {
        // Optional, provide a title for a given section, if the default title view is used. Defaults to null.
    }
    
    override fun sectionView(recyclerView: EasyRecyclerView, inflater: LayoutInflater, parent: ViewGroup): View {
        // Optional, provide a custom view for the section titles
    }

    override fun viewHolderForSectionView(recyclerView: EasyRecyclerView, itemView: View): ViewHolder {
        // Optional, provide a custom view holder for the title view
    }    

    override fun numberOfRowsInSection(recyclerView: EasyRecyclerView, section: Int) {
        // Return number of rows, defaults to 0
    }

    override fun viewTypeForRow(recyclerView: EasyRecyclerView, indexPath: IndexPath): Int {
        // Optional, return the type of view as an Int. 
        // If `viewForViewType` is not implemented and a layout resource is returned here, it is automatically inflated.
        // If `viewForViewTyoe` is implemented, any number can be used for the view type, which is passed into `viewForViewType`
    }

    override fun itemViewForViewType(recyclerView: EasyRecyclerView, viewType: Int, inflater: LayoutInflater, parent: ViewGroup): View {
        // Optional, inflate an item view for the given view type
    }
    
    override fun viewHolderForItemView(recyclerView: EasyRecyclerView, itemView: View, viewType: Int): EasyRecyclerView.ViewHolder {
        // Optional, instantiate a ViewHolder for the given item view / view type. Defaults to MaterialRowViewHolder.
    }
   
 
    /*----------------------------------------- Delegate -----------------------------------------*/      

    override fun prepareSectionViewForDisplay(recyclerView: EasyRecyclerView, viewHolder: ViewHolder, section: Int) {
        // Optional, configure the section view holder
    }

    override fun prepareViewHolderForDisplay(recyclerView: EasyRecyclerView, viewHolder: ViewHolder, indexPath: IndexPath) {
        viewHolder as MaterialRowViewHolder // By default viewHolder is a `MaterialRowViewHolder`

        // Configure view holder
    }

    override fun onRowSelected(recyclerView: EasyRecyclerView, viewHolder: ViewHolder, indexPath: IndexPath) {
        // Optional, do something if the row is tapped
    }

    override fun prepareViewHolderForReuse(recyclerView: EasyRecyclerView, viewHolder: ViewHolder) {
        // Optional, clean up the view holder for reuse if needed
    }
        
}
```



## EasyGridRecyclerView

To display grids of items, use `EasyGridRecyclerView`.

* Specify number of columns by overriding `numberOfColumns`
* Manage spacing between items simply by setting `app:interItemSpacing`. That exact amount of space will be _between_ items, same as for iOS. Additionally, spacing of the entire RecyclerView can be done by setting a margin.
* Rest of API is the same as for `EasyRecyclerView` 

```xml
<com.github.emielbon.easyrecyclerview.EasyGridRecyclerView
    android:id="@+id/contactRecyclerView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:numberOfColumns="3"
    app:interItemSpacing="8dp" />
```
