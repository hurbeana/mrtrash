package at.mrtrash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import at.mrtrash.WasteTypeFragmentDirections
import at.mrtrash.databinding.WasteTypeListItemBinding
import at.mrtrash.fastScrollRecyclerView.FastScrollRecyclerViewInterface
import at.mrtrash.models.WasteType
import java.util.*

class WasteTypeAdapter :
    ListAdapter<WasteType, WasteTypeAdapter.WasteTypeViewHolder>(WasteTypeDiffCallback()),
    FastScrollRecyclerViewInterface {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            WasteTypeAdapter.WasteTypeViewHolder =
        WasteTypeViewHolder(WasteTypeListItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun getMapIndex(): HashMap<String, Int> {
        val remap = hashMapOf<String, Int>()

        for (x in 0 until itemCount) {
            val minded = Character.toString(getItem(x).type[0])
            if (!remap.containsKey(minded))
                remap[minded] = x
        }
        return remap
    }

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
