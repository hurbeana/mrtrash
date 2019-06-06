package at.mrtrash.models.displayOption

import java.text.SimpleDateFormat
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
        val startTimeFilterCalendar = getCalendarObject(minTime)
        val endTimeFilterCalendar = getCalendarObject(maxTime)
        val startTimeOpeningHourCalendar = getCalendarObject(openingHour.startTime)
        val endTimeOpeningHourCalendar = getCalendarObject(openingHour.endTime)

        var maxStart = startTimeFilterCalendar
        if (startTimeOpeningHourCalendar.after(startTimeFilterCalendar)) {
            maxStart = startTimeOpeningHourCalendar
        }
        var minEnd = endTimeFilterCalendar
        if (endTimeOpeningHourCalendar.after(endTimeFilterCalendar)) {
            minEnd = endTimeOpeningHourCalendar
        }

        return maxStart.before(minEnd)
    }

    private fun getCalendarObject(timeString: String): Calendar {
        val time = SimpleDateFormat("HH:mm").parse(timeString)
        val calendar = Calendar.getInstance()
        calendar.time = time
        return calendar
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