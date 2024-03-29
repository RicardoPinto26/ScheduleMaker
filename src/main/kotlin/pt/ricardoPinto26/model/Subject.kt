package pt.ricardoPinto26.model

data class Subject(val name: String, val professor: String?, val classId: Int, val meetingTimes: List<MeetingTime>) {
    fun isCompatible(other: Subject) =
        this.meetingTimes.all { thisMeetingTime ->
            other.meetingTimes.all { otherMeetingTime ->
                thisMeetingTime.isCompatible(otherMeetingTime)
            }
        }

    override fun toString() =
        "$name $classId${if (professor != null) " $professor" else ""}\r\n${meetingTimes.serialize()}"
}

fun List<Subject>.timesFor(day: Day): List<MeetingTime> {
    var list = listOf<MeetingTime>()

    this.forEach { subject ->
        subject.meetingTimes.forEach { meetingTime ->
            if (meetingTime.day == day) list = list + meetingTime
        }
    }

    return list
}

fun List<Subject>.isCompatible(): Boolean =
    this.all { sub ->
        this.all { sub == it || it.isCompatible(sub) }
    }

fun List<Subject>.serialize(): String {
    var string = ""
    this.forEach { string += "$it\r\n" }
    return string.dropLast(2)
}