package at.mrtrash.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import at.mrtrash.adapter.DisposalOptionAdapter
import at.mrtrash.databinding.FragmentDisposalOptionsBinding
import at.mrtrash.models.displayOption.DisposalOptionViewModel
import at.mrtrash.models.displayOption.DisposalOptionViewModelFactory
import at.mrtrash.models.WasteType

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

        viewModel = ViewModelProviders.of(this,
            DisposalOptionViewModelFactory(activity!!.application)
        )
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
