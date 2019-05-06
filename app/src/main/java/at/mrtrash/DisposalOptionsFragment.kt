package at.mrtrash


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import at.mrtrash.adapter.DisposalOptionAdapter
import at.mrtrash.databinding.FragmentDisposalOptionsBinding
import at.mrtrash.models.DisposalOption
import at.mrtrash.models.DisposalOptionViewModel
import at.mrtrash.models.DisposalOptionViewModelFactory
import at.mrtrash.adapter.DisposalOptionAdapter
import at.mrtrash.models.WasteType
import at.mrtrash.models.Wasteplace
import at.mrtrash.network.DataService
import at.mrtrash.network.WasteplaceResponse
import kotlinx.android.synthetic.main.fragment_disposal_options.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class DisposalOptionsFragment : Fragment() {

    private val TAG = "DisposalOptionsFragment"

    private lateinit var viewModel: DisposalOptionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDisposalOptionsBinding.inflate(inflater, container, false)

        viewModel = ViewModelProviders.of(this, DisposalOptionViewModelFactory(activity!!.application))
            .get(DisposalOptionViewModel::class.java)

        val adapter = DisposalOptionAdapter()
        binding.disposalOptionsRecyclerView.adapter = adapter
        subscribeUi(adapter)

        val wt = arguments!!.getParcelable<WasteType>("selectedWasteType")
        Log.d(TAG, wt.toString())

        return binding.root
    }

    private fun subscribeUi(adapter: DisposalOptionAdapter) {
        viewModel.disposalOptions.observe(viewLifecycleOwner, Observer { disposalOptions ->
            if (disposalOptions != null) adapter.submitList(disposalOptions)
        })
    }

}
