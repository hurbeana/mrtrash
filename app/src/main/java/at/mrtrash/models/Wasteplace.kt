package at.mrtrash.models

data class Wasteplace(val district: Int, val address: String, val objecttype: String, val openingHours: String, val coordinates: Coordinates, var distance: Float?)