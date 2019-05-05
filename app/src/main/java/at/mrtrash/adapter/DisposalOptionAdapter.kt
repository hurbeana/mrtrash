package at.mrtrash.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import at.mrtrash.DisposalOptionsFragmentDirections
import at.mrtrash.databinding.CardDisposalOptionBinding
import at.mrtrash.models.DisposalOption

class DisposalOptionAdapter :
    ListAdapter<DisposalOption, DisposalOptionAdapter.DisposalOptionViewHolder>(DisposalOptionDiffCallback()) {

    private val TAG = "DisposalOptionAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DisposalOptionViewHolder(CardDisposalOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DisposalOptionViewHolder, position: Int) {
        holder.apply {
            bind(getItem(position))
        }
    }

    class DisposalOptionViewHolder(private val binding: CardDisposalOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DisposalOption) {
            binding.apply {
                disposalOption = item
                clickListenerDetail = createOnClickListenerDetail(item)
                executePendingBindings()
            }
        }

        private fun createOnClickListenerDetail(disposalOption: DisposalOption): View.OnClickListener {
            return View.OnClickListener {
                val action = DisposalOptionsFragmentDirections
                    .actionDisposalOptionsFragmentToDisposalOptionDetailFragment(disposalOption)
                it.findNavController().navigate(action)
            }

        }
    }

    private class DisposalOptionDiffCallback : DiffUtil.ItemCallback<DisposalOption>() {
        override fun areItemsTheSame(oldItem: DisposalOption, newItem: DisposalOption): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DisposalOption, newItem: DisposalOption): Boolean {
            return oldItem.distance == newItem.distance
        }
    }

}