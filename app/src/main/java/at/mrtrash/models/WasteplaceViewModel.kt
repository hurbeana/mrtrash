package at.mrtrash.models

import android.app.Application
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import at.mrtrash.location.LocationUtils
import at.mrtrash.network.DataService
import at.mrtrash.network.WasteplaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WasteplaceViewModel(context: Context) : AndroidViewModel(context as Application), LocationUtils.Callback {

    private val TAG = "WasteplaceViewModel"

    private val wasteplaces: MutableLiveData<List<Wasteplace>> by lazy {
        MutableLiveData<List<Wasteplace>>().also {
            loadWasteplaces()
        }
    }

    private val locationUtils = LocationUtils(this).initLocation()
    private lateinit var lastLocation: Location

    fun getWasteplaces(): LiveData<List<Wasteplace>> {
        return wasteplaces
    }

    fun loadWasteplaces() {
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
                    val newWastplaces: ArrayList<Wasteplace> = ArrayList()

                    wasteplaceResponse.features?.forEach { feature ->
                        val coordinates = Coordinates(
                            feature.geometry?.coordinates?.get(1)!!,
                            feature.geometry?.coordinates?.get(0)!!
                        )

                        newWastplaces.add(
                            Wasteplace(
                                feature.properties?.district!!,
                                feature.properties?.address!!,
                                feature.properties?.objecttype!!,
                                feature.properties?.openingHours!!,
                                coordinates,
                                getDistanceInKilometers(coordinates)
                            )
                        )
                    }

                    newWastplaces.sortWith(nullsLast(compareBy { it.distance }))
                    wasteplaces.postValue(newWastplaces)
                }
            }

            override fun onFailure(call: Call<WasteplaceResponse>, t: Throwable) {
                //TODO: handle failure
//                Log.e(TAG, t.localizedMessage)
            }
        })
    }

    fun getDistanceInKilometers(coordinates: Coordinates): Float? {
        return if (::lastLocation.isInitialized) {
            val otherLocation = Location("other")
            otherLocation.latitude = coordinates.latitude
            otherLocation.longitude = coordinates.longitude

            lastLocation.distanceTo(otherLocation) / 1000
        } else {
            null
        }
    }

    override fun locationChange(location: Location) {
        lastLocation = location
        recalculateDistances()
    }

    private fun recalculateDistances() {
        if(wasteplaces.value != null) {
            val oldWasteplaces: ArrayList<Wasteplace>? = wasteplaces.value as ArrayList<Wasteplace>
            oldWasteplaces?.forEach { wasteplace -> wasteplace.distance = getDistanceInKilometers(wasteplace.coordinates) }
            oldWasteplaces?.sortWith(nullsLast(compareBy { it.distance }))
            wasteplaces.postValue(oldWasteplaces)
        }
    }

}