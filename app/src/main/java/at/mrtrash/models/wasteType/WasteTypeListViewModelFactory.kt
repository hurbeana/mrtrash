package at.mrtrash.models.wasteType

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModelFactory that produces WasteTypeListViewModels given the context
 */
class WasteTypeListViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WasteTypeListViewModel(context) as T
    }
}
