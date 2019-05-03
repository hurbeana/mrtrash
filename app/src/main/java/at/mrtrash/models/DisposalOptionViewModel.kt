package at.mrtrash.models

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import at.mrtrash.location.LocationUtils
import at.mrtrash.network.DataService
import at.mrtrash.network.ProblemMaterialCollectionPointResponse
import at.mrtrash.network.WasteplaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DisposalOptionViewModel(context: Context) : AndroidViewModel(context as Application), LocationUtils.Callback {

    private val TAG = "DisposalOptionViewModel"

    private val disposalOptions: MutableLiveData<List<DisposalOption>> by lazy {
        MutableLiveData<List<DisposalOption>>().also {
            loadDisposalOptions()
        }
    }

    private val locationUtils = LocationUtils(this).initLocation()
    private lateinit var lastLocation: Location

    fun getDisposalOptions(): LiveData<List<DisposalOption>> {
        return disposalOptions
    }

    fun loadDisposalOptions() {
        val tempDisposalOptions: ArrayList<DisposalOption> = ArrayList()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://data.wien.gv.at/daten/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(DataService::class.java)

        val wastplaceCall = service.getWasteplaces()
        wastplaceCall.enqueue(object : Callback<WasteplaceResponse> {
            override fun onResponse(call: Call<WasteplaceResponse>, response: Response<WasteplaceResponse>) {
                if (response.code() == 200) {
                    val wasteplaceResponse = response.body()!!

                    wasteplaceResponse.features?.forEach { feature ->
                        val location = Location(feature.properties?.address!!)
                        location.latitude = feature.geometry?.coordinates?.get(1)!!
                        location.longitude = feature.geometry?.coordinates?.get(0)!!

                        tempDisposalOptions.add(
                            Wasteplace(
                                feature.properties?.district!!,
                                feature.properties?.address!!,
                                feature.properties?.openingHours!!,
                                location,
                                getDistanceInKilometers(location),
                                feature.properties?.objecttype!!
                            )
                        )
                    }

                    tempDisposalOptions.sortWith(nullsLast(compareBy { it.distance }))
                    disposalOptions.postValue(tempDisposalOptions)
                }
            }

            override fun onFailure(call: Call<WasteplaceResponse>, t: Throwable) {
                //TODO: handle failure
            }
        })

        val problemMaterialCollectionPointCall = service.getProblemMaterialCollectionPoint()
        problemMaterialCollectionPointCall.enqueue(object : Callback<ProblemMaterialCollectionPointResponse> {
            override fun onResponse(
                call: Call<ProblemMaterialCollectionPointResponse>,
                response: Response<ProblemMaterialCollectionPointResponse>
            ) {
                if (response.code() == 200) {
                    val problemMaterialCollectionPointResponse = response.body()!!

                    problemMaterialCollectionPointResponse.features?.forEach { feature ->
                        val location = Location(feature.properties?.address!!)
                        location.latitude = feature.geometry?.coordinates?.get(1)!!
                        location.longitude = feature.geometry?.coordinates?.get(0)!!

                        tempDisposalOptions.add(
                            ProblemMaterialCollectionPoint(
                                feature.properties?.district!!,
                                feature.properties?.address!!,
                                feature.properties?.openingHours!!,
                                location,
                                getDistanceInKilometers(location),
                                feature.properties?.note!!,
                                feature.properties?.phone!!,
                                feature.properties?.furtherInformationLink!!
                            )
                        )
                    }

                    tempDisposalOptions.sortWith(nullsLast(compareBy { it.distance }))
                    disposalOptions.postValue(tempDisposalOptions)
                }
            }

            override fun onFailure(call: Call<ProblemMaterialCollectionPointResponse>, t: Throwable) {
                //TODO: handle failure
            }
        })
    }

    fun getDistanceInKilometers(location: Location): Float? {
        return if (::lastLocation.isInitialized) {
            lastLocation.distanceTo(location) / 1000
        } else {
            null
        }
    }

    override fun locationChange(location: Location) {
        lastLocation = location
        recalculateDistances()
    }

    private fun recalculateDistances() {
        if (disposalOptions.value != null) {
            val oldWasteplaces: ArrayList<DisposalOption>? = disposalOptions.value as ArrayList<DisposalOption>
            oldWasteplaces?.forEach { wasteplace -> wasteplace.distance = getDistanceInKilometers(wasteplace.location) }
            oldWasteplaces?.sortWith(nullsLast(compareBy { it.distance }))
            disposalOptions.postValue(oldWasteplaces)
        }
    }

}