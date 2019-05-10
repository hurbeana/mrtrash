package at.mrtrash.fragments


import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import at.mrtrash.R
import at.mrtrash.databinding.FragmentDisposalOptionsFilterBinding
import at.mrtrash.models.displayOption.DisposalOptionFilterViewModel

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
