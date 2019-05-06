package at.mrtrash


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import at.mrtrash.adapter.DisposalOptionAdapter
import at.mrtrash.models.WasteType
import at.mrtrash.models.Wasteplace
import at.mrtrash.network.DataService
import at.mrtrash.network.WasteplaceResponse
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

        wasteplaces.add(Wasteplace(1, "Test", "Hallo", "Öffnung"))
        wasteplaces.add(Wasteplace(2, "Test", "Hallo", "Öffnung"))

        linearLayoutManager = LinearLayoutManager(activity)
        view.disposalOptionsRecyclerView.layoutManager = linearLayoutManager
        adapter = DisposalOptionAdapter(wasteplaces)
        view.disposalOptionsRecyclerView.adapter = adapter
        loadWasteplaces()

        val wt = arguments!!.getParcelable<WasteType>("selectedWasteType")
        Log.d(TAG, wt.toString())

        return view
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
                    wasteplaces.clear()

                    wasteplaceResponse.features?.forEach { feature ->
                        wasteplaces.add(
                            Wasteplace(
                                feature.properties?.district!!,
                                feature.properties?.address!!,
                                feature.properties?.objecttype!!,
                                feature.properties?.openingHours!!
                            )
                        )
                    }

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


}
