package at.mrtrash.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data class for WasteType
 */
@Parcelize
data class WasteType(val type: String, val wastePlaces: List<String>) : Parcelable
