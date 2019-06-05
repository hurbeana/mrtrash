package at.mrtrash.utils

import at.mrtrash.models.displayOption.OpeningHour

fun parseOpeningHours(openingHoursString: String): List<OpeningHour> {
    val openingHours = ArrayList<OpeningHour>()

    if (openingHoursString.contains("Mo-Sa")) {
        for (i in 2..7)
            openingHours.add(OpeningHour(i, "07:00", "18:00"))
    }
    if (openingHoursString.contains("So 7")) {
        openingHours.add(OpeningHour(1, "07:00", "18:00"))
    }
    if (openingHoursString.contains("Montag bis Samstag")) {
        for (i in 2..7)
            openingHours.add(OpeningHour(i, "07:00", "18:00"))
    }
    if (openingHoursString.contains("Montag bis Freitag")) {
        for (i in 2..6)
            openingHours.add(OpeningHour(i, "16:00", "18:00"))
    }
    if (openingHoursString.contains(", Samstag von")) {
        openingHours.add(OpeningHour(6, "09:00", "13:00"))
    }

    return openingHours
}
