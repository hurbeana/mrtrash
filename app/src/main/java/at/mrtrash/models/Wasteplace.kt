package at.mrtrash.models

import android.location.Location

data class Wasteplace(
    override val id: String,
    override val district: Int,
    override val address: String,
    override val openingHours: String,
    override val location: Location,
    override var distance: Float?,
    val objecttype: String
) : DisposalOption