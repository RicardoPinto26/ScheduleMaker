package pt.ricardoPinto26.model

class Subject(val name: String, val professor: String?, val classId: Int, val meetingTimes: List<MeetingTime>) {
    fun isCompatible(other: Subject) =
        this.meetingTimes.all { thisMeetingTime ->
            other.meetingTimes.all { otherMeetingTime ->
                thisMeetingTime.isCompatible(otherMeetingTime)
            }
        }
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