package at.mrtrash.models

import android.location.Location

interface DisposalOption {
    val district: Int
    val address: String
    val openingHours: String
    val location: Location
    var distance: Float?
}