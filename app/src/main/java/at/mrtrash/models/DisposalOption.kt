package at.mrtrash.models

import android.location.Location
import at.mrtrash.R
import at.mrtrash.adapter.format
import at.mrtrash.models.displayOption.ProblemMaterialCollectionPoint
import at.mrtrash.models.displayOption.Wasteplace
import java.io.Serializable

interface DisposalOption : Serializable {
    val id: String
    val district: Int
    val address: String
    val openingHours: String
    val location: Location
    var distance: Float?

    fun getTitleString(): String {
        return when (this) {
            is Wasteplace -> "Mistplatz"
            is ProblemMaterialCollectionPoint -> "Problemstoffsammelstelle"
            else -> "Entsorgungsmöglichkeit"
        }
    }

    fun getImageResource() : Int {
        return when (this) {
            is Wasteplace -> R.drawable.mistplatz_wien
            is ProblemMaterialCollectionPoint -> R.drawable.prosafahrzeug
            else -> 0
        }
    }

    fun getAddressString(): String {
        return address + ", 1" + district.toString().padStart(2, '0') + "0 Wien"
    }

    fun getDistanceString(): String {
        return if (distance != null) {
            "${distance?.format(2)} km entfernt"
        } else {
            "-"
        }
    }
}