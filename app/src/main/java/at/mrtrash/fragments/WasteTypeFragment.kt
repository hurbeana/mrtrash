package at.mrtrash.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import at.mrtrash.R
import at.mrtrash.adapter.WasteTypeAdapter
import at.mrtrash.adapter.hideKeyboard
import at.mrtrash.databinding.FragmentWasteTypeBinding
import at.mrtrash.models.WasteType
import at.mrtrash.models.wasteType.WasteTypeListViewModel
import at.mrtrash.utils.InjectorUtils
import at.mrtrash.vuforia.ObjectTargets

/**
 * A simple [Fragment] subclass.
 * This fragment is the starting fragment of the apps navigation.
 * Contains navigation to the VuforiaActivity and tha DisposalOptionsFragment.
 * No fragment can navigate to this, except by going back.
 */
class WasteTypeFragment : Fragment(), SearchView.OnQueryTextListener, SearchView.OnCloseListener, SearchView.OnSuggestionListener {

    private lateinit var viewModel: WasteTypeListViewModel
    private lateinit var recyclerAdapter : WasteTypeAdapter


    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWasteTypeBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root

        setHasOptionsMenu(true)

        viewModel = ViewModelProviders.of(this, InjectorUtils.provideWasteTypeListViewModelFactory(context))
            .get(WasteTypeListViewModel::class.java)

        recyclerAdapter = WasteTypeAdapter {
            viewModel.filter("")
            binding.root.hideKeyboard()
        }

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

    /**
     * Private function to just bind the RecyclerView Adapter to the ViewModel
     * To be called only once per adapter
     *
     * @param adapter the adapter that wants to be bound to the ViewModels data
      */
    private fun subscribeUi(adapter: WasteTypeAdapter) {
        viewModel.liveWasteTypes.observe(viewLifecycleOwner, Observer { wasteTypes ->
            if (wasteTypes != null) adapter.submitList(wasteTypes)
        })
    }

    /**
     * Initialize the contents of the Fragment host's standard options menu.
     *
     * @param menu The options menu in which you place your items.
     */
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Suche Mist"
        searchView.setOnQueryTextListener(this)
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to
     *         proceed, true to consume it here.
     */
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
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Receive the result from a previous call to
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                var result = data!!.getStringExtra("result")
                //Toast.makeText(this, result, Toast.LENGTH_LONG).show()
                val wasteType: WasteType
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

    /**
     * Called when the user submits the query. This could be due to a key press on the
     * keyboard or due to pressing a submit button.
     * The listener can override the standard behavior by returning true
     * to indicate that it has handled the submit request. Otherwise return false to
     * let the SearchView handle the submission by launching any associated intent.
     *
     * @param query the query text that is to be submitted
     *
     * @return true if the query has been handled by the listener, false to let the
     * SearchView perform the default action.
     */
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param newText the new content of the query text field.
     *
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.filter(newText)
        return true
    }

    /**
     * The user is attempting to close the SearchView.
     *
     * @return true if the listener wants to override the default behavior of clearing the
     * text field and dismissing it, false otherwise.
     */
    override fun onClose(): Boolean {
        viewModel.filter("")
        return true
    }

    /**
     * Called when a suggestion was selected by navigating to it.
     * @param position the absolute position in the list of suggestions.
     *
     * @return true if the listener handles the event and wants to override the default
     * behavior of possibly rewriting the query based on the selected item, false otherwise.
     */
    override fun onSuggestionSelect(position: Int): Boolean {
        viewModel.filter("")
        return true
    }

    /**
     * Called when a suggestion was clicked.
     * @param position the absolute position of the clicked item in the list of suggestions.
     *
     * @return true if the listener handles the event and wants to override the default
     * behavior of launching any intent or submitting a search query specified on that item.
     * Return false otherwise.
     */
    override fun onSuggestionClick(position: Int): Boolean {
        viewModel.filter("")
        return true
    }
}
