package at.mrtrash.models.displayOption

import android.location.Location
import at.mrtrash.R
import at.mrtrash.models.DisposalOption

data class Wasteplace(
    override val id: String,
    override val district: Int,
    override val address: String,
    override val openingHours: String,
    override val location: Location,
    override var distance: Float?,
    val objecttype: String
) : DisposalOption {

    override fun getTitleString(): String {
        return "Mistplatz"
    }

    override fun getImageResource(): Int {
        return R.drawable.wasteplace
    }

}