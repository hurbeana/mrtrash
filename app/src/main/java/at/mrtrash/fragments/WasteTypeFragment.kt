package at.mrtrash.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.databinding.adapters.SearchViewBindingAdapter.setOnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import at.mrtrash.R
import at.mrtrash.adapter.WasteTypeAdapter
import at.mrtrash.databinding.FragmentWasteTypeBinding
import at.mrtrash.models.WasteType
import at.mrtrash.models.wasteType.WasteTypeListViewModel
import at.mrtrash.utils.InjectorUtils
import at.mrtrash.vuforia.ObjectTargets
import kotlinx.android.synthetic.main.fragment_waste_type.*
import java.util.Locale.filter

/**
 * A simple [Fragment] subclass.
 *
 */
class WasteTypeFragment : Fragment(), SearchView.OnQueryTextListener {

    private val TAG = WasteTypeFragment::class.qualifiedName

    private lateinit var viewModel: WasteTypeListViewModel
    private lateinit var recyclerAdapter : WasteTypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWasteTypeBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root

        setHasOptionsMenu(true)

        viewModel = ViewModelProviders.of(this, InjectorUtils.provideWasteTypeListViewModelFactory(context))
            .get(WasteTypeListViewModel::class.java)

        recyclerAdapter = WasteTypeAdapter()

        binding.fastScroller.apply {
            setRecyclerView(binding.wasteTypeFragmentView)
            sectionIndicator = binding.sectionTitleIndicator
        }
        // Assign vals to RecyclerView
        binding.wasteTypeFragmentView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            addOnScrollListener(binding.fastScroller.onScrollListener)
            adapter = recyclerAdapter
        }
        subscribeUi(recyclerAdapter)

        return binding.root
    }

    private fun subscribeUi(adapter: WasteTypeAdapter) {
        viewModel.liveWasteTypes.observe(viewLifecycleOwner, Observer { wasteTypes ->
            if (loading_indicator.visibility == View.VISIBLE) loading_indicator.visibility = View.INVISIBLE
            if (wasteTypes != null) adapter.submitList(wasteTypes)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Suche Mist"
        searchView.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_scan) {
            val myIntent = Intent(activity, ObjectTargets::class.java)
            //myIntent.putExtra("key", value) //Optional parameters
            startActivityForResult(myIntent, 1)
            //Toast.makeText(this@MainActivity, "Action clicked", Toast.LENGTH_LONG).show()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                var result = data!!.getStringExtra("result")
                //Toast.makeText(this, result, Toast.LENGTH_LONG).show()
                var wasteType: WasteType
                result = if (result == "tempo") "Papiertaschentücher" else "Zigarettenschachteln"
                wasteType = viewModel.liveWasteTypes.value!!.find { wt -> wt.type == result }!!
                val direction =
                    WasteTypeFragmentDirections.actionWasteTypeFragmentToDisposalOptionsFragment(
                        wasteType,
                        wasteType.type
                    )
                findNavController().navigate(direction)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.filter(newText)
        return true
    }
}
