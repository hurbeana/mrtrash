package at.mrtrash.models

import android.location.Location

data class ProblemMaterialCollectionPoint(
    override val district: Int,
    override val address: String,
    override val openingHours: String,
    override val location: Location,
    override var distance: Float?,
    val note: String,
    val phone: String,
    val furtherInformationLink: String
) : DisposalOption