package at.mrtrash.models.displayOption

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * ViewModel for the disposal option filter
 */
class DisposalOptionFilterViewModel : ViewModel() {
    val disposalOptionFilter: MutableLiveData<DisposalOptionFilter> = MutableLiveData()

    var minTime: String = "00:00"
    var maxTime: String = "23:59"
    var mon: Boolean = true
    var tue: Boolean = true
    var wed: Boolean = true
    var thu: Boolean = true
    var fri: Boolean = true
    var sat: Boolean = true
    var sun: Boolean = true

    /**
     * Returns a list of the selected days as integer values.
     * Sunday = 1, Monday = 2, ...
     *
     * @return list of integer values
     */
    fun getSelectedDays(): List<Int> {
        val days = ArrayList<Int>()
        if (mon)
            days.add(Calendar.MONDAY)
        if (tue)
            days.add(Calendar.TUESDAY)
        if (wed)
            days.add(Calendar.WEDNESDAY)
        if (thu)
            days.add(Calendar.THURSDAY)
        if (fri)
            days.add(Calendar.FRIDAY)
        if (sat)
            days.add(Calendar.SATURDAY)
        if (sun)
            days.add(Calendar.SUNDAY)
        return days
    }

    /**
     * Returns a list of booleans of the selection state of the days. First value represents monday.
     *
     * @return list of boolean values
     */
    fun getSelectedDaysAsBooleanList(): List<Boolean> {
        val dayBooleans = ArrayList<Boolean>()
        dayBooleans.add(mon)
        dayBooleans.add(tue)
        dayBooleans.add(wed)
        dayBooleans.add(thu)
        dayBooleans.add(fri)
        dayBooleans.add(sat)
        dayBooleans.add(sun)
        return dayBooleans
    }

    /**
     * Updates live data if filter button was clicked
     */
    fun onFilterClicked() {
        disposalOptionFilter.value = DisposalOptionFilter(minTime, maxTime, mon, tue, wed, thu, fri, sat, sun)
    }

    /**
     * Sets filter to now and updates live data if opened now button was clicked
     */
    fun onFilterNowClicked() {
        val sdf = SimpleDateFormat("HH:mm")
        val calender = Calendar.getInstance()

        minTime = sdf.format(calender.time)
        calender.add(Calendar.HOUR, 2)
        maxTime = sdf.format(calender.time)
        mon = Calendar.MONDAY == calender.get(Calendar.DAY_OF_WEEK)
        tue = Calendar.TUESDAY == calender.get(Calendar.DAY_OF_WEEK)
        wed = Calendar.WEDNESDAY == calender.get(Calendar.DAY_OF_WEEK)
        thu = Calendar.THURSDAY == calender.get(Calendar.DAY_OF_WEEK)
        fri = Calendar.FRIDAY == calender.get(Calendar.DAY_OF_WEEK)
        sat = Calendar.SATURDAY == calender.get(Calendar.DAY_OF_WEEK)
        sun = Calendar.SUNDAY == calender.get(Calendar.DAY_OF_WEEK)

        onFilterClicked()
    }
}