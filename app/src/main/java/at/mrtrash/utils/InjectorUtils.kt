package at.mrtrash.utils

import android.content.Context
import at.mrtrash.models.WasteTypeListViewModelFactory

object InjectorUtils {
    fun provideWasteTypeListViewModelFactory(context: Context): WasteTypeListViewModelFactory {
        return WasteTypeListViewModelFactory(context)
    }
}