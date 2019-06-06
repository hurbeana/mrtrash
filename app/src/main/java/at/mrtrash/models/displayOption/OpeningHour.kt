package at.mrtrash.models.displayOption

/**
 * Represents an opening hour for one day.
 * Day is represented as integer value.
 * Sunday = 1, Monday = 2, ...
 */
class OpeningHour(val day: Int, val startTime: String, val endTime: String)