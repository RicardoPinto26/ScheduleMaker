package pt.ricardoPinto26.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import pt.ricardoPinto26.model.Schedule
import pt.ricardoPinto26.model.Subject

class ViewModel(loadedSchedules: List<Schedule>, loadedSubjects: List<Subject>) {
    var schedules by mutableStateOf(loadedSchedules)
    var subjects by mutableStateOf(loadedSubjects)
    var currentIndex = 0
    var currentSchedule by mutableStateOf(schedules.firstOrNull() ?: Schedule.EMPTY_SCHEDULE)
}