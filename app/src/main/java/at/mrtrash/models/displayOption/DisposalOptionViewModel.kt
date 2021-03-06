package at.mrtrash.models.displayOption

import android.app.Activity
import android.app.Application
import android.content.Context
import android.location.Location
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import at.mrtrash.R
import at.mrtrash.models.DisposalOption
import at.mrtrash.models.WasteType
import at.mrtrash.utils.LocationUtils
import at.mrtrash.utils.network.DataService
import at.mrtrash.utils.network.ProblemMaterialCollectionPointResponse
import at.mrtrash.utils.network.WasteplaceResponse
import at.mrtrash.utils.parseOpeningHours
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ViewModel for DisposalOptionsFragment
 */
class DisposalOptionViewModel(
    val context: Context,
    private val wasteType: WasteType,
    private val progressBar: ProgressBar,
    val view: View,
    val activity: Activity
) :
    AndroidViewModel(context as Application), LocationUtils.Callback {

    val disposalOptions: MutableLiveData<List<DisposalOption>> by lazy {
        MutableLiveData<List<DisposalOption>>().also {
            loadDisposalOptions()
        }
    }

    private lateinit var allDisposalOptions: ArrayList<DisposalOption>

    private val locationUtils = LocationUtils(this).initLocation()
    private lateinit var lastLocation: Location

    private fun loadDisposalOptions() {
//        val tempDisposalOptions: ArrayList<DisposalOption> = ArrayList()
        allDisposalOptions = ArrayList()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://data.wien.gv.at/daten/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(DataService::class.java)

        var wasteplacesLoaded = false
        var problemMaterialCollectionPointsLoaded = false

        fun afterDownloaded() {
            if (wasteplacesLoaded && problemMaterialCollectionPointsLoaded) {
                allDisposalOptions.sortWith(nullsLast(compareBy { it.distance }))
                disposalOptions.postValue(allDisposalOptions)
                progressBar.visibility = View.GONE
            }
        }

        if (wasteType.wastePlaces.contains("Mistplatz")) {
            val wastplaceCall = service.getWasteplaces()
            wastplaceCall.enqueue(object : Callback<WasteplaceResponse> {
                override fun onResponse(call: Call<WasteplaceResponse>, response: Response<WasteplaceResponse>) {
                    if (response.code() == 200) {
                        val wasteplaceResponse = response.body()!!

                        wasteplaceResponse.features?.forEach { feature ->
                            val location = Location(feature.properties?.address!!)
                            location.latitude = feature.geometry?.coordinates?.get(1)!!
                            location.longitude = feature.geometry?.coordinates?.get(0)!!

                            allDisposalOptions.add(
                                Wasteplace(
                                    feature.id!!,
                                    feature.properties?.district!!,
                                    feature.properties?.address!!,
                                    feature.properties?.openingHours!!,
                                    parseOpeningHours(feature.properties?.openingHours!!),
                                    location,
                                    getDistanceInKilometers(location),
                                    feature.properties?.objecttype!!
                                )
                            )
                        }

                        wasteplacesLoaded = true
                        afterDownloaded()
                    }
                }

                override fun onFailure(call: Call<WasteplaceResponse>, t: Throwable) {
                    Snackbar.make(view, R.string.loading_error_wasteplaces, Snackbar.LENGTH_LONG)
                }
            })
        } else {
            wasteplacesLoaded = true
            afterDownloaded()
        }

        if (wasteType.wastePlaces.contains("Problemstoffsammelstelle")) {
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

                            allDisposalOptions.add(
                                ProblemMaterialCollectionPoint(
                                    feature.id!!,
                                    feature.properties?.district!!,
                                    feature.properties?.address!!,
                                    feature.properties?.openingHours!!,
                                    parseOpeningHours(feature.properties?.openingHours!!),
                                    location,
                                    getDistanceInKilometers(location),
                                    feature.properties?.note!!,
                                    feature.properties?.phone!!,
                                    feature.properties?.furtherInformationLink!!
                                )
                            )
                        }

                        problemMaterialCollectionPointsLoaded = true
                        afterDownloaded()
                    }
                }

                override fun onFailure(call: Call<ProblemMaterialCollectionPointResponse>, t: Throwable) {
                    Snackbar.make(view, R.string.loading_error_problem_material_collection_point, Snackbar.LENGTH_LONG)
                }
            })
        } else {
            problemMaterialCollectionPointsLoaded = true
            afterDownloaded()
        }
    }

    private fun getDistanceInKilometers(location: Location): Float? {
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

    /**
     * Filters the list of all disposal options with the passed DisposalOptionFilter object
     *
     * @param disposalOptionFilter filter to filter with
     */
    fun filter(disposalOptionFilter: DisposalOptionFilter) {
        val filteredList: ArrayList<DisposalOption> = ArrayList()
        allDisposalOptions.forEach {
            if (it.isInFilter(disposalOptionFilter)) {
                filteredList.add(it)
            }
        }
        disposalOptions.postValue(filteredList)
    }

}