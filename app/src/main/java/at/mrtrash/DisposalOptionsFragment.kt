package at.mrtrash


import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import at.mrtrash.adapter.DisposalOptionAdapter
import at.mrtrash.models.Coordinates
import at.mrtrash.models.Wasteplace
import at.mrtrash.network.DataService
import at.mrtrash.network.WasteplaceResponse
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_disposal_options.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass.
 *
 */
class DisposalOptionsFragment : Fragment() {

    private val TAG = "DisposalOptionsFragment"

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: DisposalOptionAdapter
    private var wasteplaces: ArrayList<Wasteplace> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_disposal_options, container, false)

        linearLayoutManager = LinearLayoutManager(activity)
        view.disposalOptionsRecyclerView.layoutManager = linearLayoutManager
        adapter = DisposalOptionAdapter(wasteplaces)
        view.disposalOptionsRecyclerView.adapter = adapter
        loadWasteplaces()

        return view
    }

    fun loadWasteplaces() {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
        }

        var location: Location? = null
        LocationServices.getFusedLocationProviderClient(context!!)
            .lastLocation.addOnSuccessListener { actLocation: Location? ->
            location = actLocation
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://data.wien.gv.at/daten/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(DataService::class.java)
        val call = service.getWasteplaces()
        call.enqueue(object : Callback<WasteplaceResponse> {
            override fun onResponse(call: Call<WasteplaceResponse>, response: Response<WasteplaceResponse>) {
                if (response.code() == 200) {
                    val wasteplaceResponse = response.body()!!
                    wasteplaces.clear()

                    wasteplaceResponse.features?.forEach { feature ->
                        val coordinates = Coordinates(
                            feature.geometry?.coordinates?.get(1)!!,
                            feature.geometry?.coordinates?.get(0)!!
                        )

                        wasteplaces.add(
                            Wasteplace(
                                feature.properties?.district!!,
                                feature.properties?.address!!,
                                feature.properties?.objecttype!!,
                                feature.properties?.openingHours!!,
                                coordinates,
                                getDistance(location, coordinates)
                            )
                        )
                    }

                    wasteplaces.sortWith(nullsLast(compareBy { it.distance }))

                    activity!!.runOnUiThread {
                        adapter.notifyDataSetChanged()
                        Log.i(TAG, "notified")
                    }

                    Log.i(TAG, wasteplaces.size.toString())
                }
            }

            override fun onFailure(call: Call<WasteplaceResponse>, t: Throwable) {
                Log.e(TAG, t.localizedMessage)
            }
        })
    }

    fun getDistance(deviceLocation: Location?, coordinates: Coordinates): Float? {
        return if (deviceLocation != null) {
            val otherLocation = Location("other")
            otherLocation.latitude = coordinates.latitude
            otherLocation.longitude = coordinates.longitude

            deviceLocation.distanceTo(otherLocation) / 1000
        } else {
            null
        }
    }

}
