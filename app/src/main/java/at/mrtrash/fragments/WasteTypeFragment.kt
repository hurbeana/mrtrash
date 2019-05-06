package at.mrtrash.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import at.mrtrash.adapter.WasteTypeAdapter
import at.mrtrash.databinding.FragmentWasteTypeBinding
import at.mrtrash.models.wasteType.WasteTypeListViewModel
import at.mrtrash.utils.InjectorUtils

/**
 * A simple [Fragment] subclass.
 *
 */
class WasteTypeFragment : Fragment() {

    private val TAG = WasteTypeFragment::class.qualifiedName

    private lateinit var viewModel: WasteTypeListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWasteTypeBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root

        viewModel = ViewModelProviders.of(this, InjectorUtils.provideWasteTypeListViewModelFactory(context))
            .get(WasteTypeListViewModel::class.java)

        val recyclerAdapter = WasteTypeAdapter()

        binding.fastScroller.apply {
            setRecyclerView(binding.wasteTypeFragmentView)
            sectionIndicator = binding.sectionTitleIndicator
        }
        // Assign vals to RecyclerView
        binding.wasteTypeFragmentView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            addOnScrollListener(binding.fastScroller.onScrollListener)
            adapter = recyclerAdapter
        }
        subscribeUi(recyclerAdapter)

        return binding.root
    }

    private fun subscribeUi(adapter: WasteTypeAdapter) {
        viewModel.liveWasteTypes.observe(viewLifecycleOwner, Observer { wasteTypes ->
            if (wasteTypes != null) adapter.submitList(wasteTypes)
        })
    }
}
