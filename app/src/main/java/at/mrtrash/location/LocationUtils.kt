package at.mrtrash.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import at.mrtrash.models.DisposalOptionViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

class LocationUtils(private var disposalOptionViewModel: DisposalOptionViewModel) :
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener {

    private val TAG = "LocationUtils"

    lateinit var googleApiClient: GoogleApiClient
    private lateinit var oldLocation: Location
    private var mLocationManager: LocationManager? = null
    private var locationRequest: LocationRequest? = null
    private val UINTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FINTERVAL: Long = 2000 /* 2 sec */
    private lateinit var locationManager: LocationManager

    override fun onLocationChanged(location: Location) {
        if (!::oldLocation.isInitialized) {
            oldLocation = location
            disposalOptionViewModel.locationChange(location)
        } else if (oldLocation.distanceTo(location) > 10) {
            oldLocation = location
            disposalOptionViewModel.locationChange(location)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        googleApiClient.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        //TODO: pActivity.location.text = connectionResult.errorMessage.toString()
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(pLocationChanged: Bundle?) {
        startLocationUpdates()
        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(disposalOptionViewModel.getApplication() as Context)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            OnSuccessListener<Location> { location ->
                if (location != null) {
                    if (!::oldLocation.isInitialized) {
                        oldLocation = location
                        disposalOptionViewModel.locationChange(location)
                    } else if (oldLocation.distanceTo(location) > 10) {
                        oldLocation = location
                        disposalOptionViewModel.locationChange(location)
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = UINTERVAL
            fastestInterval = FINTERVAL
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)
    }

    fun initLocation() {
        googleApiClient = GoogleApiClient.Builder(disposalOptionViewModel.getApplication() as Context).apply {
            addConnectionCallbacks(this@LocationUtils)
            addConnectionCallbacks(this@LocationUtils)
            addApi(LocationServices.API)
        }.build()

        mLocationManager =
            (disposalOptionViewModel.getApplication() as Context).getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (checkLocation()) {
            googleApiClient.connect()
        }
    }

    private fun checkLocation(): Boolean {
        val isLocationEnabled = isLocationEnabled()
        if (!isLocationEnabled) {
            //TODO
//            showAlert(
//                (disposalOptionViewModel.getApplication() as Context).getString(R.string.location_title),
//                (disposalOptionViewModel.getApplication() as Context).getString(R.string.location_message)
//            )
        }
        return isLocationEnabled
    }

    private fun isLocationEnabled(): Boolean {
        locationManager =
            (disposalOptionViewModel.getApplication() as Context).getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )

    }

    private fun showAlert(pTitle: String, pMessage: String) {
        //TODO
//        val dialog = AlertDialog.Builder(pActivity)
//        dialog.apply {
//            setTitle(pTitle)
//            setMessage(pMessage)
//            setPositiveButton((disposalOptionViewModel.getApplication() as Context).getString(R.string.location_settings),
//                { _, _ ->
//                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                    pActivity.startActivity(myIntent)
//                })
//        }.show()
    }

    interface Callback {
        fun locationChange(location: Location)
    }

}
