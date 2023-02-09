package pt.ricardoPinto26

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import pt.ricardoPinto26.model.*
import pt.ricardoPinto26.ui.BORDER_THICKNESS
import pt.ricardoPinto26.ui.SEGMENT_HEIGHT
import pt.ricardoPinto26.ui.SEGMENT_WIDTH
import pt.ricardoPinto26.ui.ScheduleMaker
import java.io.File
import java.io.FileNotFoundException

fun main() {
    var currentSubjectName = ""
    var currentClass = 0
    var currentProfessor: String? = null

    var meetingTimes = listOf<MeetingTime>()
    var currentSubjects = listOf<Subject>()

    var readingTimes = false
    try {
        File("schedule.txt").readLines().forEach {
            if (!readingTimes) {
                val tokens = it.split(' ')
                println(tokens)
                currentSubjectName = tokens[0]
                currentClass = tokens[1].toInt()
                currentProfessor = if (tokens.size == 3) tokens[2] else null
                readingTimes = true
            } else {
                val times = it.split(';')
                times.forEach { time ->
                    val tokens = time.split('-')
                    val day = Day.parse(tokens[0].uppercase())
                    val startTime = tokens[1].toTime()
                    val endTime = tokens[2].toTime()
                    val room = if (tokens.size == 4) tokens[3] else null
                    meetingTimes = meetingTimes + MeetingTime(
                        day,
                        startTime,
                        endTime,
                        currentSubjectName,
                        currentProfessor,
                        currentClass,
                        room
                    )
                }
                currentSubjects =
                    currentSubjects + Subject(currentSubjectName, currentProfessor, currentClass, meetingTimes)
                meetingTimes = listOf()
                readingTimes = false
            }
        }
    } catch (_: FileNotFoundException) {
        println("No such file found. Initializing with empty schedule.")
    }

    val currentSchedules: List<Schedule> = currentSubjects.computeSchedules()

    application {
        val winState =
            WindowState(
                size = DpSize(
                    SEGMENT_WIDTH * (Day.values().size + 1 + 4),
                    SEGMENT_HEIGHT * 31 + BORDER_THICKNESS * 31 + 8.dp
                )
            )
        Window(
            title = "Schedule Maker",
            onCloseRequest = {
                exitApplication()
            },
            state = winState, resizable = false
        ) {
            ScheduleMaker(currentSchedules, currentSubjects)
        }
    }
}
