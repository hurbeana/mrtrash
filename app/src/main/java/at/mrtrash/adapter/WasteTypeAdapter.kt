package at.mrtrash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SectionIndexer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import at.mrtrash.WasteTypeFragmentDirections
import at.mrtrash.databinding.WasteTypeListItemBinding
import at.mrtrash.models.WasteType

class WasteTypeAdapter :
    ListAdapter<WasteType, WasteTypeAdapter.WasteTypeViewHolder>(WasteTypeDiffCallback()), SectionIndexer {
     private var alph : Array<String> = arrayOf("A","B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "Ä", "Ö", "Ü")

    override fun getPositionForSection(sectionIndex: Int): Int {
        return 0
    }

    override fun getSectionForPosition(position: Int): Int {
        var pos = position
        if (pos >= itemCount) {
            pos = itemCount - 1
        }
        return alph.indexOfFirst{ it == Character.toString(getItem(pos).type[0]) }
    }

    override fun getSections(): Array<Any> {
        return alph as Array<Any>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            WasteTypeAdapter.WasteTypeViewHolder =
        WasteTypeViewHolder(WasteTypeListItemBinding.inflate(LayoutInflater.from(parent.context)))

    class WasteTypeViewHolder(private val binding: WasteTypeListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: WasteType) {
            binding.apply {
                clickListener = listener
                wasteTypeItem = item
                executePendingBindings()
            }
        }
    }

    private fun createOnClickListener(wasteType: WasteType): View.OnClickListener {
        return View.OnClickListener {
            val direction =
                WasteTypeFragmentDirections.actionWasteTypeFragmentToDisposalOptionsFragment(wasteType, wasteType.type)
            it.findNavController().navigate(direction)
        }
    }

    override fun onBindViewHolder(holder: WasteTypeViewHolder, position: Int) {
        val wasteType = getItem(position)
        holder.apply {
            bind(createOnClickListener(wasteType), wasteType)
            itemView.tag = wasteType
        }
    }
}

private class WasteTypeDiffCallback : DiffUtil.ItemCallback<WasteType>() {

    override fun areItemsTheSame(oldItem: WasteType, newItem: WasteType): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(oldItem: WasteType, newItem: WasteType): Boolean {
        return oldItem == newItem
    }
}
