package at.mrtrash.fragments


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import at.mrtrash.R
import at.mrtrash.adapter.DisposalOptionAdapter
import at.mrtrash.databinding.FragmentDisposalOptionsBinding
import at.mrtrash.models.WasteType
import at.mrtrash.models.displayOption.DisposalOptionFilterViewModel
import at.mrtrash.models.displayOption.DisposalOptionViewModel
import at.mrtrash.models.displayOption.DisposalOptionViewModelFactory

/**
 * A simple [Fragment] subclass.
 *
 */
class DisposalOptionsFragment : Fragment() {

    private val TAG = "DisposalOptionsFragment"

    private lateinit var viewModel: DisposalOptionViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ViewModelProviders.of(activity!!).get(DisposalOptionFilterViewModel::class.java).disposalOptionFilter.observe(
            this,
            Observer {
                viewModel.filter(it)
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val binding = FragmentDisposalOptionsBinding.inflate(inflater, container, false)

        val wasteType = arguments!!.getParcelable<WasteType>("selectedWasteType")

        viewModel = ViewModelProviders.of(
            this,
            DisposalOptionViewModelFactory(activity!!.application, wasteType)
        ).get(DisposalOptionViewModel::class.java)

        val adapter = DisposalOptionAdapter()
        binding.disposalOptionsRecyclerView.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }

    private fun subscribeUi(adapter: DisposalOptionAdapter) {
        viewModel.disposalOptions.observe(viewLifecycleOwner, Observer {
            if (it != null) adapter.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_filter, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        findNavController().navigate(R.id.action_disposalOptionsFragment_to_disposalOptionsFilterFragment)
        if (id == R.id.action_scan) {
        }

        return super.onOptionsItemSelected(item)
    }

}
