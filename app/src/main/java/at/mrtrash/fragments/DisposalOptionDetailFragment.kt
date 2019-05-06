package at.mrtrash.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import at.mrtrash.R
import at.mrtrash.adapter.format
import at.mrtrash.models.DisposalOption
import at.mrtrash.models.displayOption.DisposalOptionDetailViewModel
import at.mrtrash.models.displayOption.DisposalOptionDetailViewModelFactory
import kotlinx.android.synthetic.main.fragment_disposal_option_detail.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class DisposalOptionDetailFragment : Fragment() {

    private val args: DisposalOptionDetailFragmentArgs by navArgs()
    private lateinit var viewModel: DisposalOptionDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this,
            DisposalOptionDetailViewModelFactory(args.disposalOption)
        )
            .get(DisposalOptionDetailViewModel::class.java)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_disposal_option_detail, container, false)
        bindDisposalOption(view)
        return view
    }

    private fun bindDisposalOption(view: View) {
        view.disposalOptionDetailTextViewAddressValue.text = getAddressString(viewModel.disposalOption)
        view.disposalOptionDetailTextViewOpeningHoursValue.text = viewModel.disposalOption.openingHours
        view.disposalOptionDetailTextViewDistanceValue.text = getDistanceString(viewModel.disposalOption)
    }

    private fun getAddressString(disposalOption: DisposalOption): String {
        return disposalOption.address + ", 1" + disposalOption.district.toString().padStart(2, '0') + "0 Wien"
    }

    private fun getDistanceString(disposalOption: DisposalOption): String {
        return if (disposalOption.distance != null) {
            "${disposalOption.distance?.format(2)} km entfernt"
        } else {
            "-"
        }
    }


}
