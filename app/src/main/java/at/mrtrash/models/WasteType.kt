package at.mrtrash.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WasteType(val type: String, val wastePlaces: List<String>) : Parcelable