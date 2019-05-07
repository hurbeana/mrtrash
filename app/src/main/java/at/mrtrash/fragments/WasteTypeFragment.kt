package at.mrtrash.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import at.mrtrash.R
import at.mrtrash.adapter.WasteTypeAdapter
import at.mrtrash.databinding.FragmentWasteTypeBinding
import at.mrtrash.models.wasteType.WasteTypeListViewModel
import at.mrtrash.utils.InjectorUtils
import at.mrtrash.vuforia.ObjectTargets

/**
 * A simple [Fragment] subclass.
 *
 */
class WasteTypeFragment : Fragment() {

    private val TAG = WasteTypeFragment::class.qualifiedName

    private lateinit var viewModel: WasteTypeListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWasteTypeBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root

        setHasOptionsMenu(true)

        viewModel = ViewModelProviders.of(this, InjectorUtils.provideWasteTypeListViewModelFactory(context))
            .get(WasteTypeListViewModel::class.java)

        val recyclerAdapter = WasteTypeAdapter()

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
            if (wasteTypes != null) adapter.submitList(wasteTypes)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
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
                val result = data!!.getStringExtra("result")
                //Toast.makeText(this, result, Toast.LENGTH_LONG).show()
                val wasteType =
                    viewModel.liveWasteTypes.value!!.find { wt -> wt.type == "Papiertaschentücher" }!!
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
}
