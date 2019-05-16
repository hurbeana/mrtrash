package at.mrtrash.fragments


import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import at.mrtrash.R
import at.mrtrash.databinding.FragmentDisposalOptionsFilterBinding
import at.mrtrash.models.displayOption.DisposalOptionFilterViewModel
import com.touchboarder.weekdaysbuttons.WeekdaysDataItem
import com.touchboarder.weekdaysbuttons.WeekdaysDataSource
import java.util.*


/**
 * A simple [Fragment] subclass.
 *
 */
class DisposalOptionsFilterFragment : Fragment() {

    private lateinit var viewModel: DisposalOptionFilterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(DisposalOptionFilterViewModel::class.java)
        val binding: FragmentDisposalOptionsFilterBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_disposal_options_filter, container, false)
        binding.viewModel = viewModel
        binding.clickListenerFilter = createOnClickListenerFilter()
        binding.clickListenerFrom = createOnClickListenerFrom()
        binding.clickListenerTo = createOnClickListenerTo()

        WeekdaysDataSource(activity!! as AppCompatActivity, R.id.weekdays_stub, binding.root)
            .setFirstDayOfWeek(Calendar.MONDAY)
            .setSelectedDays(*viewModel.getSelectedDays().toIntArray())
            .setUnselectedColor(Color.TRANSPARENT)
            .setTextColorUnselected(Color.BLACK)
            .setFontTypeFace(Typeface.MONOSPACE)
            .setNumberOfLetters(2)
            .setFontBaseSize(18)
            .start(object : WeekdaysDataSource.Callback {
                override fun onWeekdaysItemClicked(attachId: Int, item: WeekdaysDataItem) {
                    // Do something if today is selected?
                    when (item.calendarDayId) {
                        Calendar.MONDAY -> viewModel.mon = item.isSelected
                        Calendar.TUESDAY -> viewModel.tue = item.isSelected
                        Calendar.WEDNESDAY -> viewModel.wed = item.isSelected
                        Calendar.THURSDAY -> viewModel.thu = item.isSelected
                        Calendar.FRIDAY -> viewModel.fri = item.isSelected
                        Calendar.SATURDAY -> viewModel.sat = item.isSelected
                        Calendar.SUNDAY -> viewModel.sun = item.isSelected
                    }

                }

                override fun onWeekdaysSelected(attachId: Int, items: ArrayList<WeekdaysDataItem>) {
                    //Filter on the attached id if there is multiple weekdays data sources
                    //do nothing
                }
            })

        return binding.root
    }

    private fun createOnClickListenerFilter(): View.OnClickListener {
        return View.OnClickListener {
            viewModel.onFilterClicked()
            fragmentManager!!.popBackStack()
        }
    }

    private fun createOnClickListenerFrom(): View.OnClickListener {
        return View.OnClickListener {
            val time = (it as EditText).text

            val timePicker = TimePickerDialog(
                activity!!,
                TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                    it.setText(h.toString().padStart(2, '0') + ":" + m.toString().padStart(2, '0'))
                }),
                Integer.parseInt(time.split(":")[0]),
                Integer.parseInt(time.split(":")[1]),
                true
            )
            timePicker.show()
        }
    }

    private fun createOnClickListenerTo(): View.OnClickListener {
        return View.OnClickListener {
            val time = (it as EditText).text

            val timePicker = TimePickerDialog(
                activity!!,
                TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                    it.setText(h.toString().padStart(2, '0') + ":" + m.toString().padStart(2, '0'))
                }),
                Integer.parseInt(time.split(":")[0]),
                Integer.parseInt(time.split(":")[1]),
                true
            )
            timePicker.show()
        }
    }

}
