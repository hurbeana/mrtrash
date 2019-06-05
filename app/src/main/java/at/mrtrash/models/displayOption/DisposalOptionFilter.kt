package at.mrtrash.models.displayOption

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
        return isBetween(minTime, openingHour.startTime, openingHour.endTime) || isBetween(
            maxTime,
            openingHour.startTime,
            openingHour.endTime
        )
    }

    private fun isBetween(timeToCheck: String, startTime: String, endTime: String): Boolean {
        val timeToCheckCalendar = getCalendarObject(timeToCheck)
        val startTimeCalendar = getCalendarObject(startTime)
        val endTimeCalendar = getCalendarObject(endTime)
        return timeToCheckCalendar.after(startTimeCalendar) && timeToCheckCalendar.before(endTimeCalendar)
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