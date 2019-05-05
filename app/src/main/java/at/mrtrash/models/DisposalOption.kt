package at.mrtrash.models

import android.location.Location
import java.io.Serializable

interface DisposalOption : Serializable {
    val district: Int
    val address: String
    val openingHours: String
    val location: Location
    var distance: Float?
}