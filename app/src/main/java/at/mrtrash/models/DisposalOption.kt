package at.mrtrash.models

import android.location.Location
import at.mrtrash.adapter.format
import at.mrtrash.models.displayOption.DisposalOptionFilter
import at.mrtrash.models.displayOption.OpeningHour
import java.io.Serializable

/**
 * An interface for all available disposal options
 */
interface DisposalOption : Serializable {
    val id: String
    val district: Int
    val address: String
    val openingHours: String
    val openingHoursConcrete: List<OpeningHour>
    val location: Location
    var distance: Float?

    /**
     * Returns the title string which can be displayed
     *
     * @return title string for interface purposes
     */
    fun getTitleString(): String {
        return "Entsorgungsmöglichkeit"
    }

    /**
     * Returns the associated image resource
     *
     * @return the image resource for interface purposes
     */
    fun getImageResource(): Int {
        return 0
    }

    /**
     * Returns the address string which can be displayed
     *
     * @return a formatted address string with "[address], 1[district]0 Wien" formatting
     */
    fun getAddressString(): String {
        return address + ", 1" + district.toString().padStart(2, '0') + "0 Wien"
    }

    /**
     * Returns a formatted distance between user and DisposalOption
     *
     * @return a formatted string in german or "-" if distance is not available
     */
    fun getDistanceString(): String {
        return if (distance != null) {
            "${distance?.format(2)} km entfernt"
        } else {
            "-"
        }
    }

    /**
     * Returns if this DisposalOption is in the passed DisposalOptionFilter
     *
     * @param disposalOptionFilter the filter
     * @return true if this is in filter, false otherwise
     */
    fun isInFilter(disposalOptionFilter: DisposalOptionFilter): Boolean {
        return disposalOptionFilter.isInFilter(openingHoursConcrete)
    }

}