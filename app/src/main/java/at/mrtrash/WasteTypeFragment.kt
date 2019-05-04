package at.mrtrash


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import at.mrtrash.adapter.WasteTypeAdapter
import at.mrtrash.models.WasteType
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import kotlinx.android.synthetic.main.fragment_waste_type.*
import kotlinx.android.synthetic.main.fragment_waste_type.view.*
import java.io.InputStream
import java.io.StringReader
import at.mrtrash.fastScrollRecyclerView.FastScrollRecyclerViewItemDecoration



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

        // Init all vals needed for RecyclerView
        linearLayoutManager = LinearLayoutManager(activity)
        val decoration = FastScrollRecyclerViewItemDecoration(activity)
        val wasteTypes = mutableListOf<WasteType>()
        adapter = WasteTypeAdapter(wasteTypes)
        adapter.onClick = { pos, type ->
            val action =
                WasteTypeFragmentDirections.actionWasteTypeFragmentToDisposalOptionsFragment(adapter.wasteTypes[pos], adapter.wasteTypes[pos].type)
            view.findNavController().navigate(action)
        }

        // Assign vals to RecyclerView
        view.waste_type_fragment_view.itemAnimator = DefaultItemAnimator()
        view.waste_type_fragment_view.addItemDecoration(decoration)
        view.waste_type_fragment_view.adapter = adapter
        view.waste_type_fragment_view.layoutManager = linearLayoutManager


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

        wasteTypes.sortBy { wt -> wt.type }

        activity!!.runOnUiThread {
            adapter.notifyDataSetChanged()
            Log.i(TAG, "notified")
        }
    }
}
