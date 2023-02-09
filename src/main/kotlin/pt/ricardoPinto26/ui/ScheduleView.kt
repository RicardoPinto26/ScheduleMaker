package pt.ricardoPinto26.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ricardoPinto26.model.*

val SEGMENT_HEIGHT = 24.dp
val SEGMENT_WIDTH = 128.dp
val BORDER_THICKNESS = 1.dp
const val SEGMENT_DURATION = 30

@Composable
fun ScheduleView(schedule: Schedule) {
    Row {
        Column {
            var time = Time(8, 0)
            Box(
                Modifier.height(SEGMENT_HEIGHT).width(SEGMENT_WIDTH).border(BORDER_THICKNESS, Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(schedule.label, color = Color.Red)
            }
            repeat(30) {
                val nextTime = time + 30
                Box(
                    Modifier.height(SEGMENT_HEIGHT).width(SEGMENT_WIDTH).border(BORDER_THICKNESS, Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text("$time - $nextTime")
                }
                time = nextTime
            }
        }
        Day.values().forEach { day ->
            var minusSegments = 0
            Column {
                var time = Time(8, 0)
                Box(
                    Modifier.height(SEGMENT_HEIGHT).width(SEGMENT_WIDTH).border(BORDER_THICKNESS, Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text(day.name)
                }
                repeat(31) {
                    val possibleMeetingTime = schedule.subjects.timesFor(day).firstOrNull { it.startTime == time }
                    if (possibleMeetingTime != null) {
                        MeetingTimeView(possibleMeetingTime)
                        minusSegments = possibleMeetingTime.duration / SEGMENT_DURATION - 1
                    } else {
                        if (minusSegments == 0) Box(
                            Modifier.height(SEGMENT_HEIGHT).width(SEGMENT_WIDTH).border(BORDER_THICKNESS, Color.Black)
                        ) else minusSegments -= 1
                    }
                    time += 30
                }
            }
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