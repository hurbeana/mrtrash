package at.mrtrash.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import at.mrtrash.R
import at.mrtrash.models.DisposalOption
import at.mrtrash.models.displayOption.DisposalOptionDetailViewModel
import at.mrtrash.models.displayOption.DisposalOptionDetailViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_disposal_option_detail.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class DisposalOptionDetailFragment : Fragment(), OnMapReadyCallback {

    private val args: DisposalOptionDetailFragmentArgs by navArgs()
    private lateinit var viewModel: DisposalOptionDetailViewModel
    private lateinit var mMap: GoogleMap

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val loc = LatLng(viewModel.disposalOption.location.latitude, viewModel.disposalOption.location.longitude)

        mMap.addMarker(MarkerOptions().position(loc).title(viewModel.disposalOption.getTitleString()))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(
            this,
            DisposalOptionDetailViewModelFactory(args.disposalOption)
        )
            .get(DisposalOptionDetailViewModel::class.java)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_disposal_option_detail, container, false)
        bindDisposalOption(view)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val mapFragment: SupportMapFragment? =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

    }

    private fun bindDisposalOption(view: View) {
        view.disposalOptionDetailTextViewAddressValue.text = getAddressString(viewModel.disposalOption)
        view.disposalOptionDetailTextViewOpeningHoursValue.text = viewModel.disposalOption.openingHours
        view.disposalOptionDetailTextViewDistanceValue.text = getDistanceString(viewModel.disposalOption)
        view.disposalOptionDetailImageView.setImageResource(viewModel.disposalOption.getImageResource())
    }

    private fun getAddressString(disposalOption: DisposalOption): String {
        return getString(R.string.address_value, disposalOption.address, disposalOption.district)
    }

    private fun getDistanceString(disposalOption: DisposalOption): String {
        return if (disposalOption.distance != null) {
            getString(R.string.distance_value, disposalOption.distance)
        } else {
            getString(R.string.value_not_present)
        }
    }


}
