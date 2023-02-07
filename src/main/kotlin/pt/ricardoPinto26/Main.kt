package pt.ricardoPinto26

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import pt.ricardoPinto26.model.*
import pt.ricardoPinto26.ui.BORDER_THICKNESS
import pt.ricardoPinto26.ui.SEGMENT_HEIGHT
import pt.ricardoPinto26.ui.SEGMENT_WIDTH
import pt.ricardoPinto26.ui.ScheduleView
import java.io.File

fun main() {

    var currentLabel = ""
    var currentClass = 0

    var meetingTimes = listOf<MeetingTime>()
    var currentSubjects = listOf<Subject>()

    var readingTimes = false
    File("schedule.txt").readLines().forEach {
        if (!readingTimes) {
            val tokens = it.split(' ')
            println(tokens)
            currentLabel = tokens[0]
            currentClass = tokens[1].toInt()
            readingTimes = true
        } else {
            val times = it.split(';')
            times.forEach { time ->
                val tokens = time.split('-')
                val day = Day.parse(tokens[0])
                val startTime = tokens[1].toTime()
                val endTime = tokens[2].toTime()
                meetingTimes = meetingTimes + MeetingTime(day, startTime, endTime, currentLabel)
            }
            currentSubjects = currentSubjects + Subject(currentLabel, currentClass, meetingTimes)
            meetingTimes = listOf()
            readingTimes = false
        }
    }

    val currentSchedules: List<Schedule> = computeSchedules(currentSubjects)

    application {
        val winState =
            WindowState(size = DpSize(SEGMENT_WIDTH * (8 + 4), SEGMENT_HEIGHT * 31 + BORDER_THICKNESS * 31 + 8.dp))
        var schedules by remember { mutableStateOf(currentSchedules) }
        var currentIndex = 0
        var currentSchedule by remember { mutableStateOf(schedules.firstOrNull() ?: Schedule.EMPTY_SCHEDULE) }
        Window(::exitApplication, state = winState, resizable = false) {
            Row {
                ScheduleView(currentSchedule)
                Button({
                    currentIndex =
                        if (currentIndex == 0) schedules.size - 1
                        else currentIndex - 1
                    currentSchedule = schedules[currentIndex]
                }, enabled = schedules.size !in 0..1) {
                    Text("Prev Schedule")
                }
                Button({
                    currentIndex =
                        if (currentIndex == schedules.size - 1) 0
                        else currentIndex + 1
                    currentSchedule = schedules[currentIndex]
                }, enabled = schedules.size !in 0..1) {
                    Text("Next Schedule")
                }
            }
        }
    }
}

/*
fun mainInput() {

    var input: String
    var newSubject = true
    var newClass = true
    var currentSubjects = listOf<Subject>()
    var currentMeetingTimes = listOf<MeetingTime>()
    var subjectLabel = ""
    var classLabel = ""
    var skipSubject = false
    while (true) {
        if (newSubject) {
            print("Subject Label?\n> ")
            subjectLabel = readln()
            if (subjectLabel == "skip") skipSubject = true
            newSubject = false
            newClass = true
        }
        if(newClass) {
            print("Class Label? (40 for 41N, 41D/42D/43D)\n> ")
            classLabel = readln()
            newClass = false
        }
        if (!skipSubject) Day.values().forEach { day ->
            print("meeting time for $day? (Format Example: 8.00-9.30\n> ")
            input = readln()
            when (input) {
                "" -> {
                }

                else -> {
                    val tokens = input.split('-')
                    val startTime = tokens[0].toTime()
                    val endTime = tokens[1].toTime()
                    currentMeetingTimes = currentMeetingTimes + MeetingTime(day, startTime, endTime, subjectLabel)
                }
            }
        }
        print("Wanna input another subject or finish? (input/finish)\n >")
        input = readln()
        when (input) {
            "input" -> {
                currentSubjects = currentSubjects + Subject(subjectLabel, classLabel.toInt(), currentMeetingTimes)
                currentMeetingTimes = listOf()
                newSubject = true
            }

            "finish" -> {
                currentSubjects = currentSubjects + Subject(subjectLabel, classLabel.toInt(), currentMeetingTimes)
                break
            }
        }
    }

    val currentSchedules: List<Schedule> = computeSchedules(currentSubjects)

    application {
        val winState =
            WindowState(size = DpSize(SEGMENT_WIDTH * (8 + 4), SEGMENT_HEIGHT * 31 + BORDER_THICKNESS * 31 + 8.dp))
        var schedules by remember { mutableStateOf(currentSchedules + Schedule.EMPTY_SCHEDULE) }
        var currentIndex = 0
        var currentSchedule by remember { mutableStateOf(schedules.first()) }
        Window(::exitApplication, state = winState, resizable = false) {
            Row {
                ScheduleView(currentSchedule)
                Button({
                    currentIndex =
                        if (currentIndex == 0) schedules.size - 1
                        else currentIndex - 1
                    currentSchedule = schedules[currentIndex]
                }) {
                    Text("Prev Schedule")
                }
                Button({
                    currentIndex =
                        if (currentIndex == schedules.size - 1) 0
                        else currentIndex + 1
                    currentSchedule = schedules[currentIndex]
                }) {
                    Text("Next Schedule")
                }
            }
        }
    }
}
*/
