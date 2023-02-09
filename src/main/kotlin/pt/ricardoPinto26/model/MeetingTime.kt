package pt.ricardoPinto26.model

data class Time(val hour: Int, val minute: Int) {
    init {
        require(hour in 0..23) { "Hour has to be from 0 to 23" }
        require(minute in 0..59) { "Minute has to be from 0 to 59" }
    }

    override fun toString() = "$hour.${"%02d".format(minute)}"

    operator fun compareTo(time: Time) = when {
        (this.hour > time.hour) -> 1
        (this.hour < time.hour) -> -1
        (this.minute > time.minute) -> 1
        (this.minute < time.minute) -> -1
        else -> 0
    }

    operator fun plus(time: Time): Time {
        var newMinute = this.minute + time.minute
        var newHour = this.hour + time.hour

        if (newMinute > 59) {
            newMinute -= 60
            newHour += 1
        }

        if (newHour > 23) {
            newHour -= 24
        }

        return Time(newHour, newMinute)
    }

    operator fun plus(minutes: Int): Time {
        var newMinute = this.minute + minutes
        var newHour = this.hour

        while (newMinute > 59) {
            newMinute -= 60
            newHour += 1
        }

        while (newHour > 23) {
            newHour -= 24
        }

        return Time(newHour, newMinute)
    }

    operator fun times(i: Int): Time {
        var newTime = Time(0, 0)

        repeat(i) {
            newTime += this
        }
        return newTime
    }

    operator fun minus(time: Time): Int {
        var newMinute = this.minute - time.minute
        var newHour = this.hour - time.hour

        if (newMinute < 0) {
            newMinute += 60
            newHour -= 1
        }

        return newMinute + newHour * 60
    }
}

/**
 * String should be formatted as such: hour.minutes
 */
fun String.toTime(): Time {
    val tokens = this.split('.')
    return Time(tokens[0].toInt(), tokens[1].toInt())
}

enum class Day {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY;

    companion object {
        operator fun invoke(index: Int): Day {
            require(index in 0..values().size)
            return values()[index]
        }

        fun parse(name: String): Day =
            Day.values().firstOrNull { it.name == name } ?: throw IllegalArgumentException("No such day exists")
    }
}

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
            "Class has to begin from 8h00 to 22h30"
        }
        require(endTime.hour in 8..23 && (endTime.minute == 0 || endTime.minute == 30)) {
            "Class has to end from 8h30 to 23h00"
        }
        require(subjectName.isNotEmpty()) { "subjectName cannot be empty" }
    }

    val duration = endTime - startTime

    fun isCompatible(other: MeetingTime) =
        this.day != other.day || !(other.startTime >= this.startTime && other.startTime < this.endTime)
}

class Subject(val name: String, val professor: String?, val classId: Int, val meetingTimes: List<MeetingTime>) {
    fun isCompatible(other: Subject) =
        this.meetingTimes.all { thisMeetingTime ->
            other.meetingTimes.all { otherMeetingTime ->
                thisMeetingTime.isCompatible(otherMeetingTime)
            }
        }
}

