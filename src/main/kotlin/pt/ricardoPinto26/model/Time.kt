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
 * String should be formatted as such: "hour.minutes"
 */
fun String.toTime(): Time {
    val tokens = this.split('.')
    return Time(tokens[0].toInt(), tokens[1].toInt())
}
