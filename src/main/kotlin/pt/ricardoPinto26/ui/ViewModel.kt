package pt.ricardoPinto26.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import pt.ricardoPinto26.model.Schedule
import pt.ricardoPinto26.model.Subject
import pt.ricardoPinto26.model.computeSchedules
import pt.ricardoPinto26.storage.Storage

class ViewModel(
    private val storage: Storage<String, List<Subject>>,
    loadedSchedules: List<Schedule>,
    loadedSubjects: List<Subject>
) {
    var schedules by mutableStateOf(loadedSchedules)
        private set
    var subjects by mutableStateOf(loadedSubjects)
        private set
    var currentSchedule by mutableStateOf(schedules.firstOrNull() ?: Schedule.EMPTY_SCHEDULE)
        private set
    var autoCompute = true
        private set

    var openNewSubjectDialog by mutableStateOf(false)
    var openSaveSubjectsDialog by mutableStateOf(false)

    private var currentIndex = 0

    fun changeAutoCompute(value: Boolean) {
        autoCompute = value
    }

    fun computeSchedules() {
        schedules = subjects.computeSchedules()
        currentIndex = 0
        currentSchedule = schedules.firstOrNull() ?: Schedule.EMPTY_SCHEDULE
    }

    fun prevSchedule() {
        currentIndex =
            if (currentIndex == 0) schedules.size - 1
            else currentIndex - 1
        currentSchedule = schedules[currentIndex]
    }

    fun nextSchedule() {
        currentIndex =
            if (currentIndex == schedules.size - 1) 0
            else currentIndex + 1
        currentSchedule = schedules[currentIndex]
    }

    private fun deleteSchedule(schedule: Schedule) {
        schedules = schedules - schedule
        currentIndex =
            if (currentIndex == schedules.size) 0
            else currentIndex
        if (schedules.isEmpty()) schedules =
            schedules + Schedule.EMPTY_SCHEDULE
        currentSchedule = schedules[currentIndex]
    }

    fun deleteCurrentSchedule() = deleteSchedule(currentSchedule)

    fun deleteSubject(subject: Subject) {
        subjects = subjects - subject
        if (autoCompute) computeSchedules()
    }

    fun addSubject(subject: Subject) {
        subjects = subjects + subject
        if (autoCompute) computeSchedules()
    }

    fun saveSubjects(filename: String) {
        storage.save(filename, subjects)
    }
}