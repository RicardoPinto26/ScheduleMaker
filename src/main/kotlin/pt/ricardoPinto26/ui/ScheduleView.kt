package pt.ricardoPinto26.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ricardoPinto26.model.Day
import pt.ricardoPinto26.model.Schedule
import pt.ricardoPinto26.model.Time
import pt.ricardoPinto26.model.timesFor

val SEGMENT_HEIGHT = 24.dp
val SEGMENT_WIDTH = 128.dp
val BORDER_THICKNESS = 1.dp
const val SEGMENT_DURATION = 30
const val TIME_SLOTS = 30
val STARTING_TIME = Time(8, 0)

@Composable
fun ScheduleView(schedule: Schedule) {
    Row {
        Column {
            var time = STARTING_TIME
            Box(
                Modifier.height(SEGMENT_HEIGHT).width(SEGMENT_WIDTH).border(BORDER_THICKNESS, Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(schedule.label, color = Color.Red)
            }
            repeat(TIME_SLOTS) {
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
        Day.entries.forEach { day ->
            var minusSegments = 0
            Column {
                var time = Time(8, 0)
                Box(
                    Modifier.height(SEGMENT_HEIGHT).width(SEGMENT_WIDTH).border(BORDER_THICKNESS, Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text(day.name)
                }
                repeat(TIME_SLOTS) {
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