package at.mrtrash.models.displayOption

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.ArrayList

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

    fun getSelectedDays(): List<Int> {
        val days = ArrayList<Int>()
        if(mon)
            days.add(Calendar.MONDAY)
        if(tue)
            days.add(Calendar.TUESDAY)
        if(wed)
            days.add(Calendar.WEDNESDAY)
        if(thu)
            days.add(Calendar.THURSDAY)
        if(fri)
            days.add(Calendar.FRIDAY)
        if(sat)
            days.add(Calendar.SATURDAY)
        if(sun)
            days.add(Calendar.SUNDAY)
        return days
    }

    fun onFilterClicked() {
        disposalOptionFilter.value = DisposalOptionFilter(minTime, maxTime, mon, tue, wed, thu, fri, sat, sun)
    }
}