package at.mrtrash


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import at.mrtrash.adapter.WasteTypeAdapter
import at.mrtrash.databinding.FragmentWasteTypeBinding
import at.mrtrash.models.WasteType
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import kotlinx.android.synthetic.main.fragment_waste_type.*
import kotlinx.android.synthetic.main.fragment_waste_type.view.*
import java.io.InputStream
import java.io.StringReader
import at.mrtrash.fastScrollRecyclerView.FastScrollRecyclerViewItemDecoration
import at.mrtrash.models.WasteTypeListViewModel
import at.mrtrash.utils.InjectorUtils


/**
 * A simple [Fragment] subclass.
 *
 */
class WasteTypeFragment : Fragment() {

    private val TAG = WasteTypeFragment::class.qualifiedName

    private lateinit var adapter: WasteTypeAdapter
    private lateinit var viewModel: WasteTypeListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWasteTypeBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root

        viewModel = ViewModelProviders.of(this, InjectorUtils.provideWasteTypeListViewModelFactory(context))
            .get(WasteTypeListViewModel::class.java)

        // Init all vals needed for RecyclerView
        val decoration = FastScrollRecyclerViewItemDecoration(activity)
        adapter = WasteTypeAdapter()

        // Assign vals to RecyclerView
        binding.wasteTypeFragmentView.itemAnimator = DefaultItemAnimator()
        binding.wasteTypeFragmentView.addItemDecoration(decoration)
        binding.wasteTypeFragmentView.adapter = adapter

        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: WasteTypeAdapter) {
        viewModel.liveWasteTypes.observe(viewLifecycleOwner, Observer { wasteTypes ->
            if (wasteTypes != null) adapter.submitList(wasteTypes)
        })
    }
}
