package at.mrtrash.utils

import android.content.Context
import at.mrtrash.models.wasteType.WasteTypeListViewModelFactory

/**
 * Singleton for ViewModelFactories
 */
object InjectorUtils {
    fun provideWasteTypeListViewModelFactory(context: Context): WasteTypeListViewModelFactory {
        return WasteTypeListViewModelFactory(context)
    }
}
