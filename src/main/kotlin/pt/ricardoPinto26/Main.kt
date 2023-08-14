package pt.ricardoPinto26

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import java.io.File
import java.io.FileNotFoundException
import pt.ricardoPinto26.model.Day
import pt.ricardoPinto26.model.MeetingTime
import pt.ricardoPinto26.model.Schedule
import pt.ricardoPinto26.model.Subject
import pt.ricardoPinto26.model.computeSchedules
import pt.ricardoPinto26.model.serialize
import pt.ricardoPinto26.model.toTime
import pt.ricardoPinto26.storage.FileStorage
import pt.ricardoPinto26.storage.StringSerializer
import pt.ricardoPinto26.ui.BORDER_THICKNESS
import pt.ricardoPinto26.ui.SEGMENT_HEIGHT
import pt.ricardoPinto26.ui.SEGMENT_WIDTH
import pt.ricardoPinto26.ui.ScheduleMaker

object SubjectListSerializer : StringSerializer<List<Subject>> {
    override fun write(obj: List<Subject>) = obj.serialize()
    override fun parse(input: String): List<Subject> {
        try {
            var currentSubjectName = ""
            var currentClass = 0
            var currentProfessor: String? = null

            var meetingTimes = listOf<MeetingTime>()
            var currentSubjects = listOf<Subject>()

            var readingTimes = false
            input.split("\r\n").forEach {
                if (!readingTimes) {
                    val tokens = it.split(' ')
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
            return currentSubjects
        } catch (e: Exception) {
            throw IllegalArgumentException("Illegal string parsed!", e)
        }
    }

}

object ScheduleSerializer : StringSerializer<Schedule> {
    override fun write(obj: Schedule) = "${obj.label}\r\n${SubjectListSerializer.write(obj.subjects)}"

    override fun parse(input: String): Schedule {
        val tokens = input.split("\r\n", limit = 2)
        return Schedule(tokens[0], SubjectListSerializer.parse(tokens[1]))
    }

}

fun main(args: Array<String>) {
    var subjects: List<Subject> = emptyList()
    val filename = if (args.isNotEmpty()) args[0] else ""
    try {
        subjects = SubjectListSerializer.parse(File(filename).readText())
    } catch (e: FileNotFoundException) {
        println("No such file found. Initializing with empty schedule.")
    }

    val currentSchedules: List<Schedule> = subjects.computeSchedules()

    application {
        val winState =
            WindowState(
                size = DpSize(
                    SEGMENT_WIDTH * (Day.entries.size + 1 + 4),
                    SEGMENT_HEIGHT * 32 + BORDER_THICKNESS * 31 + 10.dp + 100.dp
                )
            )
        Window(
            title = "Schedule Maker",
            onCloseRequest = {
                exitApplication()
            },
            state = winState, resizable = false
        ) {
            ScheduleMaker(
                FileStorage(ScheduleSerializer),
                FileStorage(SubjectListSerializer),
                currentSchedules,
                subjects
            )
        }
    }
}
