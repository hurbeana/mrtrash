package at.mrtrash.models.displayOption

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import at.mrtrash.models.DisposalOption

class DisposalOptionDetailViewModelFactory(private val disposalOption: DisposalOption) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DisposalOptionDetailViewModel(disposalOption) as T
    }

}