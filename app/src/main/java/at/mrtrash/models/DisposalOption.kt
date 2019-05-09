package at.mrtrash.models

import android.location.Location
import at.mrtrash.adapter.format
import java.io.Serializable

interface DisposalOption : Serializable {
    val id: String
    val district: Int
    val address: String
    val openingHours: String
    val location: Location
    var distance: Float?

    fun getTitleString(): String {
        return "Entsorgungsmöglichkeit"
    }

    fun getImageResource(): Int {
        return 0
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