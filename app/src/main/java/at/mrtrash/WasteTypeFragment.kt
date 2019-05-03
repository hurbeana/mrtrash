package at.mrtrash


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import at.mrtrash.adapter.WasteTypeAdapter
import at.mrtrash.models.WasteType
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import kotlinx.android.synthetic.main.fragment_waste_type.*
import kotlinx.android.synthetic.main.fragment_waste_type.view.*
import java.io.InputStream
import java.io.StringReader

/**
 * A simple [Fragment] subclass.
 *
 */
class WasteTypeFragment : Fragment() {

    private val TAG = WasteTypeFragment::class.qualifiedName

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: WasteTypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_waste_type, container, false)

        // Inflate the layout for this fragment
        linearLayoutManager = LinearLayoutManager(activity)
        view.waste_type_fragment_view.layoutManager = linearLayoutManager

        val wasteTypes = mutableListOf<WasteType>()
        adapter = WasteTypeAdapter(wasteTypes)
        view.waste_type_fragment_view.adapter = adapter


        loadWasteTypes(wasteTypes)
        //button.setOnClickListener {
            //Navigation.findNavController(view).navigate(R.id.disposalOptionsFragment)
        //}

        return view
    }

    private fun loadWasteTypes(wasteTypes: MutableList<WasteType>) {
        try {
            val inputStream: InputStream = context?.assets?.open("muellABC.json")!!
            val klaxon = Klaxon()
            val string = inputStream.bufferedReader().readText()
            JsonReader(StringReader(string)).use { reader ->
                reader.beginArray {
                    while (reader.hasNext()) {
                        val wasteType = klaxon.parse<WasteType>(reader)
                        if(wasteType != null) wasteTypes.add(wasteType)
                    }
                }
            }
        } catch (e:Exception){
            Log.d(TAG, e.toString())
            throw e
        }
        Log.d(TAG, wasteTypes.toString())

        activity!!.runOnUiThread {
            adapter.notifyDataSetChanged()
            Log.i(TAG, "notified")
        }
    }
}
