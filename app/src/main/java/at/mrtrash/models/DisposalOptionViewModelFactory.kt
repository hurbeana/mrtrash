package at.mrtrash.models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * This is needed because DisposalOptionViewModel is from type AndroidViewModel and not ViewModel
 */
class DisposalOptionViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DisposalOptionViewModel(application) as T
    }
}