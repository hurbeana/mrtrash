package at.mrtrash.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import at.mrtrash.R
import at.mrtrash.models.displayOption.DisposalOptionViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar

/**
 * Utility class to get actual location and notify observers if location has changed
 */
class LocationUtils(private var disposalOptionViewModel: DisposalOptionViewModel) :
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener {

    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var oldLocation: Location
    private var mLocationManager: LocationManager? = null
    private var locationRequest: LocationRequest? = null
    private val uInterval = (2 * 1000).toLong()  /* 10 secs */
    private val fInterval: Long = 2000 /* 2 sec */
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
        Snackbar.make(disposalOptionViewModel.view, R.string.location_connection_error, Snackbar.LENGTH_LONG)
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
            interval = uInterval
            fastestInterval = fInterval
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)
    }

    /**
     * Initializes LocationUtils
     */
    fun initLocation() {
        if (ContextCompat.checkSelfPermission(
                disposalOptionViewModel.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                disposalOptionViewModel.context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                disposalOptionViewModel.activity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                8
            )
        } else {
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
    }

    private fun checkLocation(): Boolean {
        val isLocationEnabled = isLocationEnabled()
        if (!isLocationEnabled) {
            showAlert()
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

    private fun showAlert() {
        val dialog = AlertDialog.Builder(disposalOptionViewModel.context)
        dialog.apply {
            setTitle(R.string.location_title)
            setMessage(R.string.location_message)
            setPositiveButton(
                (disposalOptionViewModel.getApplication() as Context).getString(R.string.location_settings)
            ) { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                disposalOptionViewModel.context.startActivity(intent)
            }
        }.show()
    }

    interface Callback {
        fun locationChange(location: Location)
    }

}
