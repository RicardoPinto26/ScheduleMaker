package pt.ricardoPinto26

import androidx.compose.foundation.layout.Column
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
import pt.ricardoPinto26.ui.*
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
                    val day = Day.parse(tokens[0])
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
                    currentSubjects + Subject(currentSubjectName, currentClass, meetingTimes)
                meetingTimes = listOf()
                readingTimes = false
            }
        }
    } catch (_: FileNotFoundException) {
        println("No such file found. Initializing with empty schedule.")
    }

    val currentSchedules: List<Schedule> = computeSchedules(currentSubjects)

    application {
        val winState =
            WindowState(size = DpSize(SEGMENT_WIDTH * (8 + 4), SEGMENT_HEIGHT * 31 + BORDER_THICKNESS * 31 + 8.dp))
        var schedules by remember { mutableStateOf(currentSchedules) }
        var subjects by remember { mutableStateOf(currentSubjects) }
        var currentIndex = 0
        var currentSchedule by remember { mutableStateOf(schedules.firstOrNull() ?: Schedule.EMPTY_SCHEDULE) }
        Window(
            onCloseRequest = {
                if(true) exitApplication()
            },
            state = winState, resizable = false
        ) {
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
                Column {
                    Row {
                        Button({
                            currentIndex =
                                if (currentIndex == schedules.size - 1) 0
                                else currentIndex + 1
                            currentSchedule = schedules[currentIndex]
                        }, enabled = schedules.size !in 0..1) {
                            Text("Next Schedule")
                        }
                        Button({
                            schedules = schedules - currentSchedule
                            currentIndex =
                                if (currentIndex == 0) schedules.size - 1
                                else currentIndex - 1
                            currentSchedule = schedules[currentIndex]
                        }, enabled = schedules.size !in 0..1) {
                            Text("Delete Schedule")
                        }
                    }
                    SubjectListView(subjects) {
                        subjects = subjects - it
                        schedules = computeSchedules(subjects)
                        currentIndex = 0
                        currentSchedule = schedules.firstOrNull() ?: Schedule.EMPTY_SCHEDULE
                    }
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
