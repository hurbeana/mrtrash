package at.mrtrash.models.displayOption

import android.location.Location
import at.mrtrash.R
import at.mrtrash.models.DisposalOption

/**
 * Data class for the ProblemMaterialCollectionPoint, which is one Type of DisposalOption
 */
data class ProblemMaterialCollectionPoint(
    override val id: String,
    override val district: Int,
    override val address: String,
    override val openingHours: String,
    override val openingHoursConcrete: List<OpeningHour>,
    override val location: Location,
    override var distance: Float?,
    val note: String,
    val phone: String,
    val furtherInformationLink: String
) : DisposalOption {

    /**
     * Returns the title string which can be displayed
     *
     * @return title string for interface purposes
     */
    override fun getTitleString(): String {
        return "Problemstoffsammelstelle"
    }

    /**
     * Returns the associated image resource
     *
     * @return the image resource for interface purposes
     */
    override fun getImageResource(): Int {
        return R.drawable.problem_material_collection_point
    }

}