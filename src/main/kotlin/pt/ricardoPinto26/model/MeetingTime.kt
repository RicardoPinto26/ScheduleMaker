package pt.ricardoPinto26.model

data class MeetingTime(
    val day: Day,
    val startTime: Time,
    val endTime: Time,
    val subjectName: String,
    val professor: String?,
    val classId: Int,
    val room: String?
) {
    init {
        require(startTime < endTime) { "Starting time should be before ending time" }
        require(
            startTime.hour in 8..22 && (startTime.minute == 0 || startTime.minute == 30)
        ) {
            "Class has to begin from 8h00 to 22h30. Increments should be in 30min intervals. 8h00-8h30-9h00-9h30"
        }
        require(endTime.hour in 8..23 && (endTime.minute == 0 || endTime.minute == 30)) {
            "Class has to end from 8h30 to 23h00. Increments should be in 30min intervals. 8h00-8h30-9h00-9h30"
        }
        require(subjectName.isNotEmpty()) { "subjectName cannot be empty" }
    }

    val duration = endTime - startTime

    fun isCompatible(other: MeetingTime) =
        this.day != other.day || !(other.startTime >= this.startTime && other.startTime < this.endTime)

    override fun toString() = "$day-$startTime-$endTime${if (room != null) "-$room" else ""}"
}

fun List<MeetingTime>.serialize(): String {
    var string = ""
    this.forEach { string += "$it;" }
    return string.dropLast(1)
}