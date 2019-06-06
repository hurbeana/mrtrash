package at.mrtrash.models.displayOption

import android.app.Application
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import at.mrtrash.models.WasteType

/**
 * This is needed because DisposalOptionViewModel is from type AndroidViewModel and not ViewModel
 */
class DisposalOptionViewModelFactory(
    private val application: Application,
    private val wasteType: WasteType,
    private val progressBar: ProgressBar
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DisposalOptionViewModel(application, wasteType, progressBar) as T
    }

}