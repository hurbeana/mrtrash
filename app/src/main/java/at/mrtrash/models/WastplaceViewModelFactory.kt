package at.mrtrash.models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * This is needed because WasteplaceViewModel is from type AndroidViewModel and not ViewModel
 */
class WastplaceViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WasteplaceViewModel(application) as T
    }
}