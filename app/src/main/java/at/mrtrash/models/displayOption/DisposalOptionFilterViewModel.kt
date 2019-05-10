package at.mrtrash.models.displayOption

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

    fun onFilterClicked() {
        disposalOptionFilter.value = DisposalOptionFilter(minTime, maxTime, mon, tue, wed, thu, fri, sat, sun)
    }
}