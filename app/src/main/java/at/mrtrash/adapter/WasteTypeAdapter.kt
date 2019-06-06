package at.mrtrash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SectionIndexer
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import at.mrtrash.R
import at.mrtrash.databinding.WasteTypeListItemBinding
import at.mrtrash.fragments.WasteTypeFragmentDirections
import at.mrtrash.models.WasteType

/**
 * An adapter for RecyclerViews that show WasteTypes
 */
class WasteTypeAdapter(val onclick: () -> Unit) :
    ListAdapter<WasteType, WasteTypeAdapter.WasteTypeViewHolder>(WasteTypeDiffCallback()), SectionIndexer {
     private var alph : Array<String> = arrayOf("A","B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "Ä", "Ö", "Ü")

    /**
     * Given the index of a section within the array of section objects, returns
     * the starting position of that section within the adapter.
     *
     * If the section's starting position is outside of the adapter bounds, the
     * position must be clipped to fall within the size of the adapter.
     *
     * @param sectionIndex the index of the section within the array of section
     *            objects
     * @return the starting position of that section within the adapter,
     *         constrained to fall within the adapter bounds
     */
    override fun getPositionForSection(sectionIndex: Int): Int {
        return 0
    }

    /**
     * Given a position within the adapter, returns the index of the
     * corresponding section within the array of section objects.
     *
     * If the section index is outside of the section array bounds, the index
     * must be clipped to fall within the size of the section array.
     *
     * For example, consider an indexer where the section at array index 0
     * starts at adapter position 100. Calling this method with position 10,
     * which is before the first section, must return index 0.
     *
     * @param position the position within the adapter for which to return the
     *            corresponding section index
     * @return the index of the corresponding section within the array of
     *         section objects, constrained to fall within the array bounds
     */
    override fun getSectionForPosition(position: Int): Int {
        var pos = position
        if (pos >= itemCount) {
            pos = itemCount - 1
        }
        return alph.indexOfFirst{ it == Character.toString(getItem(pos).type[0]) }
    }

    /**
     * Returns an array of objects representing sections of the list. The
     * returned array and its contents should be non-null.
     *
     * The list view will call toString() on the objects to get the preview text
     * to display while scrolling. For example, an adapter may return an array
     * of Strings representing letters of the alphabet. Or, it may return an
     * array of objects whose toString() methods return their section titles.
     *
     * @return the array of section objects
     */
    override fun getSections(): Array<Any> {
        return alph as Array<Any>
    }

    /**
     * Standard onCreateViewHolder for inflating the elements of the RecyclerView
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            WasteTypeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.waste_type_list_item, parent, false))

    /**
     * Standard onBindViewHolder for binding information to the ViewHolder of the item
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: WasteTypeViewHolder, position: Int) {
        val wasteType = getItem(position)
        holder.apply {
            bind(createOnClickListener(wasteType), wasteType)
            itemView.tag = wasteType
        }
    }

    /**
     *  Class for ViewHolder items in the RecyclerView
     */
    class WasteTypeViewHolder(private val binding: WasteTypeListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Sets the needed bindings to the view, given the Item
         * @param item The item with information to be displayed
         */
        fun bind(listener: View.OnClickListener, item: WasteType) {
            binding.apply {
                clickListener = listener
                wasteTypeItem = item
                executePendingBindings()
            }
        }
    }

    /**
     * Creates a ClickListener for the given Item for navigation
     * @param wasteType the item with information needed for the click action
     *
     * @return A ClickListener that can be set on a view
     */
    private fun createOnClickListener(wasteType: WasteType): View.OnClickListener {
        return View.OnClickListener {
            onclick()
            val direction =
                WasteTypeFragmentDirections.actionWasteTypeFragmentToDisposalOptionsFragment(wasteType, wasteType.type)
            it.findNavController().navigate(direction)
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 */
private class WasteTypeDiffCallback : DiffUtil.ItemCallback<WasteType>() {

    override fun areItemsTheSame(oldItem: WasteType, newItem: WasteType): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(oldItem: WasteType, newItem: WasteType): Boolean {
        return oldItem == newItem
    }
}
