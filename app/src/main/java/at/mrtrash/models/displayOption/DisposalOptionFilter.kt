package at.mrtrash.models.displayOption

import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Represents a filter with weekdays and start and end time
 */
class DisposalOptionFilter(
    private val minTime: String = "00:00",
    private val maxTime: String = "23:59",
    private val mon: Boolean = true,
    private val tue: Boolean = true,
    private val wed: Boolean = true,
    private val thu: Boolean = true,
    private val fri: Boolean = true,
    private val sat: Boolean = true,
    private val sun: Boolean = true
) {
    /**
     * Checks if one of the passed opening hours is in the filter.
     *
     * @param openingHours list of opening hours to check
     * @return true if an opening hour is in filter, false otherwise
     */
    fun isInFilter(openingHours: List<OpeningHour>): Boolean {
        val openingHoursPerDay: Map<Int, List<OpeningHour>> = createDayMap(openingHours)

        if (checkDay(mon, Calendar.MONDAY, openingHoursPerDay))
            return true
        if (checkDay(tue, Calendar.TUESDAY, openingHoursPerDay))
            return true
        if (checkDay(wed, Calendar.WEDNESDAY, openingHoursPerDay))
            return true
        if (checkDay(thu, Calendar.THURSDAY, openingHoursPerDay))
            return true
        if (checkDay(fri, Calendar.FRIDAY, openingHoursPerDay))
            return true
        if (checkDay(sat, Calendar.SATURDAY, openingHoursPerDay))
            return true
        if (checkDay(sun, Calendar.SUNDAY, openingHoursPerDay))
            return true
        return false
    }

    private fun checkDay(day: Boolean, calendarDay: Int, openingHoursPerDay: Map<Int, List<OpeningHour>>): Boolean {
        if (day) {
            openingHoursPerDay[calendarDay]?.forEach {
                if (isInRange(it)) {
                    return true
                }
            }
        }
        return false
    }

    private fun isInRange(openingHour: OpeningHour): Boolean {
        //java.time works with JSR-310 backport for Android

        val startTimeFilter = LocalTime.parse(minTime)
        val endTimeFilter = LocalTime.parse(maxTime)
        val startTimeOpeningHour = LocalTime.parse(openingHour.startTime)
        val endTimeOpeningHour = LocalTime.parse(openingHour.endTime)

        var maxStart = startTimeFilter
        if (startTimeOpeningHour.isAfter(startTimeFilter)) {
            maxStart = startTimeOpeningHour
        }
        var minEnd = endTimeFilter
        if (endTimeOpeningHour.isBefore(endTimeFilter)) {
            minEnd = endTimeOpeningHour
        }

        return maxStart.isBefore(minEnd)
    }

    private fun createDayMap(openingHours: List<OpeningHour>): Map<Int, List<OpeningHour>> {
        val openingHoursPerDay = HashMap<Int, ArrayList<OpeningHour>>()
        for (i in 1..7)
            openingHoursPerDay.put(i, ArrayList())
        openingHours.forEach {
            openingHoursPerDay[it.day]?.add(it)
        }
        return openingHoursPerDay
    }
}