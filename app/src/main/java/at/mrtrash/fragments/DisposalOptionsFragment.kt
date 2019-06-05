package at.mrtrash.fragments


import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import at.mrtrash.R
import at.mrtrash.adapter.DisposalOptionAdapter
import at.mrtrash.databinding.FragmentDisposalOptionsBinding
import at.mrtrash.databinding.FragmentDisposalOptionsFilterBinding
import at.mrtrash.models.WasteType
import at.mrtrash.models.displayOption.DisposalOptionFilterViewModel
import at.mrtrash.models.displayOption.DisposalOptionViewModel
import at.mrtrash.models.displayOption.DisposalOptionViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.touchboarder.weekdaysbuttons.WeekdaysDataItem
import com.touchboarder.weekdaysbuttons.WeekdaysDataSource
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class DisposalOptionsFragment : Fragment() {

    private lateinit var viewModel: DisposalOptionViewModel

    private lateinit var viewModelFilter: DisposalOptionFilterViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ViewModelProviders.of(activity!!).get(DisposalOptionFilterViewModel::class.java).disposalOptionFilter.observe(
            this,
            Observer {
                viewModel.filter(it)
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val binding = FragmentDisposalOptionsBinding.inflate(inflater, container, false)

        val wasteType = arguments!!.getParcelable<WasteType>("selectedWasteType")

        viewModel = ViewModelProviders.of(
            this,
            DisposalOptionViewModelFactory(activity!!.application, wasteType)
        ).get(DisposalOptionViewModel::class.java)

        val adapter = DisposalOptionAdapter()
        binding.disposalOptionsRecyclerView.adapter = adapter
        subscribeUi(adapter)

        binding.fabShowNearest.setOnClickListener {
            if (!viewModel.disposalOptions.value.isNullOrEmpty()) {
                val item = viewModel.disposalOptions.value!![0]
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo:${item.location.latitude},${item.location.longitude}?q=${item.getTitleString()}+${item.getAddressString()}")
                )
                binding.root.context.startActivity(intent)
            }
        }

        onCreateFilterView(binding.disposalOptionsFilterBinding)

        return binding.root
    }

    private fun subscribeUi(adapter: DisposalOptionAdapter) {
        viewModel.disposalOptions.observe(viewLifecycleOwner, Observer {
            if (it != null) adapter.submitList(it)
        })
    }

    private fun onCreateFilterView(binding: FragmentDisposalOptionsFilterBinding) {
        viewModelFilter = ViewModelProviders.of(activity!!).get(DisposalOptionFilterViewModel::class.java)
        binding.viewModel = viewModelFilter

        val weekdaysDataSource = WeekdaysDataSource(activity!! as AppCompatActivity, R.id.weekdays_stub, binding.root)
            .setFirstDayOfWeek(Calendar.MONDAY)
            .setSelectedDays(*viewModelFilter.getSelectedDays().toIntArray())
            .setUnselectedColor(Color.TRANSPARENT)
            .setTextColorUnselected(Color.BLACK)
            .setFontTypeFace(Typeface.MONOSPACE)
            .setNumberOfLetters(2)
            .setFontBaseSize(18)
            .start(object : WeekdaysDataSource.Callback {
                override fun onWeekdaysItemClicked(attachId: Int, item: WeekdaysDataItem) {
                    // Do something if today is selected?
                    when (item.calendarDayId) {
                        Calendar.MONDAY -> viewModelFilter.mon = item.isSelected
                        Calendar.TUESDAY -> viewModelFilter.tue = item.isSelected
                        Calendar.WEDNESDAY -> viewModelFilter.wed = item.isSelected
                        Calendar.THURSDAY -> viewModelFilter.thu = item.isSelected
                        Calendar.FRIDAY -> viewModelFilter.fri = item.isSelected
                        Calendar.SATURDAY -> viewModelFilter.sat = item.isSelected
                        Calendar.SUNDAY -> viewModelFilter.sun = item.isSelected
                    }

                }

                override fun onWeekdaysSelected(attachId: Int, items: ArrayList<WeekdaysDataItem>) {
                    //Filter on the attached id if there is multiple weekdays data sources
                    //do nothing
                }
            })

        fun createOnClickListenerFilter(): View.OnClickListener {
            return View.OnClickListener {
                viewModelFilter.onFilterClicked()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
        binding.clickListenerFilter = createOnClickListenerFilter()

        fun createOnClickListenerFilterNow(): View.OnClickListener {
            return View.OnClickListener {
                viewModelFilter.onFilterNowClicked()
                binding.viewModel = viewModelFilter
                activity!!.runOnUiThread {
                    weekdaysDataSource.setSelectedDays(*viewModelFilter.getSelectedDays().toIntArray())
                }
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
        binding.clickListenerFilterNow = createOnClickListenerFilterNow()

        fun createOnClickListenerFrom(): View.OnClickListener {
            return View.OnClickListener {
                val time = (it as EditText).text

                val timePicker = TimePickerDialog(
                    activity!!,
                    TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                        it.setText(getString(R.string.time_chooser, h, m))
                    }),
                    Integer.parseInt(time.split(":")[0]),
                    Integer.parseInt(time.split(":")[1]),
                    true
                )
                timePicker.show()
            }
        }
        binding.clickListenerFrom = createOnClickListenerFrom()

        fun createOnClickListenerTo(): View.OnClickListener {
            return View.OnClickListener {
                val time = (it as EditText).text

                val timePicker = TimePickerDialog(
                    activity!!,
                    TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                        it.setText(getString(R.string.time_chooser, h, m))
                    }),
                    Integer.parseInt(time.split(":")[0]),
                    Integer.parseInt(time.split(":")[1]),
                    true
                )
                timePicker.show()
            }
        }
        binding.clickListenerTo = createOnClickListenerTo()

        bottomSheetBehavior = BottomSheetBehavior.from(binding.filterLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_filter, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_filter) {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
//            findNavController().navigate(R.id.action_disposalOptionsFragment_to_disposalOptionsFilterFragment)
        }

        return super.onOptionsItemSelected(item)
    }

}
