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

    /**
     * Callback that is being called as soon as the map has done initializing
     *
     * @param googleMap The instance of the map
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // get location
        val loc = LatLng(viewModel.disposalOption.location.latitude, viewModel.disposalOption.location.longitude)

        // set location
        mMap.addMarker(MarkerOptions().position(loc).title(viewModel.disposalOption.getTitleString()))
        // move camera to location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
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

    /**
     * Gets and saves the map fragment as soon as were done loading
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val mapFragment: SupportMapFragment? =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

    }

    /**
     * Bind information to the view
     *
     * @param view The view that need the info
     */
    private fun bindDisposalOption(view: View) {
        view.disposalOptionDetailTextViewAddressValue.text = getAddressString(viewModel.disposalOption)
        view.disposalOptionDetailTextViewOpeningHoursValue.text = viewModel.disposalOption.openingHours
        view.disposalOptionDetailTextViewDistanceValue.text = getDistanceString(viewModel.disposalOption)
        view.disposalOptionDetailImageView.setImageResource(viewModel.disposalOption.getImageResource())
    }

    /**
     * Return formatted address string for the given DisposalOption
     *
     * @param disposalOption The DisposalOption with the info
     */
    private fun getAddressString(disposalOption: DisposalOption): String {
        return getString(R.string.address_value, disposalOption.address, disposalOption.district)
    }

    /**
     * Return formatted distance string for the given DisposalOption
     *
     * @param disposalOption The DisposalOption with the info
     */
    private fun getDistanceString(disposalOption: DisposalOption): String {
        return if (disposalOption.distance != null) {
            getString(R.string.distance_value, disposalOption.distance)
        } else {
            getString(R.string.value_not_present)
        }
    }


}
