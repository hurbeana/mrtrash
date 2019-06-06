package at.mrtrash.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import at.mrtrash.databinding.CardDisposalOptionBinding
import at.mrtrash.fragments.DisposalOptionsFragmentDirections
import at.mrtrash.models.DisposalOption


/**
 * An adapter for RecyclerViews that show DisposalOptions
 */
class DisposalOptionAdapter :
    ListAdapter<DisposalOption, DisposalOptionAdapter.DisposalOptionViewHolder>(DisposalOptionDiffCallback()) {

    /**
     * Standard onCreateViewHolder for inflating the elements of the RecyclerView
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DisposalOptionViewHolder(CardDisposalOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    /**
     * Standard onBindViewHolder for binding information to the ViewHolder of the item
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: DisposalOptionViewHolder, position: Int) {
        holder.apply {
            bind(getItem(position))
        }
    }

    /**
     * Class for ViewHolder items in the RecyclerView
     */
    class DisposalOptionViewHolder(private val binding: CardDisposalOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val context: Context = binding.root.context

        /**
         * Sets the needed bindings to the view, given the Item
         * @param item The item with information to be displayed
         */
        fun bind(item: DisposalOption) {
            binding.apply {
                disposalOption = item
                clickListenerDetail = createOnClickListenerDetail(item)
                clickListenerMap = createOnClickListenerMap(item)
                executePendingBindings()
            }
        }

        /**
         * Creates a ClickListener for the given Item for a map intent
         * @param item the item with information needed for the click action
         *
         * @return A ClickListener that can be set on a view
         */
        private fun createOnClickListenerMap(item: DisposalOption): View.OnClickListener {
            return View.OnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo:${item.location.latitude},${item.location.longitude}?q=${item.getTitleString()}+${item.getAddressString()}")
                )
                context.startActivity(intent)
            }
        }

        /**
         * Creates a ClickListener for the given Item for a navigation
         * @param disposalOption the item with information needed for the click action
         *
         * @return A ClickListener that can be set on a view
         */
        private fun createOnClickListenerDetail(disposalOption: DisposalOption): View.OnClickListener {
            return View.OnClickListener {
                val action = DisposalOptionsFragmentDirections
                    .actionDisposalOptionsFragmentToDisposalOptionDetailFragment(
                        disposalOption,
                        disposalOption.getTitleString()
                    )
                it.findNavController().navigate(action)
            }
        }
    }

    /**
     * Callback for calculating the diff between two non-null items in a list.
     */
    private class DisposalOptionDiffCallback : DiffUtil.ItemCallback<DisposalOption>() {
        override fun areItemsTheSame(oldItem: DisposalOption, newItem: DisposalOption): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DisposalOption, newItem: DisposalOption): Boolean {
            return oldItem.distance == newItem.distance
        }
    }

}