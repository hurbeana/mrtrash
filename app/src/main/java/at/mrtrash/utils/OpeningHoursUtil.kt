package at.mrtrash.utils

import at.mrtrash.models.displayOption.OpeningHour

fun parseOpeningHours(openingHoursString: String): List<OpeningHour> {
    //TODO: parse this correctly

    val openingHours = ArrayList<OpeningHour>()
    for (i in 1..7)
        openingHours.add(OpeningHour(i, "07:00", "18:00"))
    return openingHours
}
