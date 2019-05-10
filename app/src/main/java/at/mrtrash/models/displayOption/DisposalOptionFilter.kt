package at.mrtrash.models.displayOption

class DisposalOptionFilter(
    val minTime: String = "00:00",
    val maxTime: String = "23:59",
    val mon: Boolean = true,
    val tue: Boolean = true,
    val wed: Boolean = true,
    val thu: Boolean = true,
    val fri: Boolean = true,
    val sat: Boolean = true,
    val sun: Boolean = true
)