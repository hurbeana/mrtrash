package at.mrtrash


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import at.mrtrash.adapter.DisposalOptionAdapter
import at.mrtrash.models.Wasteplace
import at.mrtrash.models.WasteplaceViewModel
import at.mrtrash.models.WastplaceViewModelFactory
import kotlinx.android.synthetic.main.fragment_disposal_options.view.*

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

        //TODO: maybe we could move this to adapter?
        val model = ViewModelProviders.of(this, WastplaceViewModelFactory(activity!!.application))
            .get(WasteplaceViewModel::class.java)
        model.getWasteplaces().observe(this, Observer<List<Wasteplace>> { newWasteplaces ->
            Log.i(TAG, "new data arrived: " + newWasteplaces.size)
            wasteplaces.clear()
            wasteplaces.addAll(newWasteplaces as ArrayList<Wasteplace>)
            activity!!.runOnUiThread {
                adapter.notifyDataSetChanged()
                Log.i(TAG, "notified")
            }
        })

        return view
    }

}
