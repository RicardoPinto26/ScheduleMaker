package pt.ricardoPinto26.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import pt.ricardoPinto26.model.Schedule
import pt.ricardoPinto26.model.Subject
import pt.ricardoPinto26.model.computeSchedules
import pt.ricardoPinto26.storage.Storage

class ViewModel(
    private val scheduleStorage: Storage<String, Schedule>,
    private val subjectStorage: Storage<String, List<Subject>>,
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

    var openErrorDialog by mutableStateOf(false)
    var exception by mutableStateOf<Exception?>(null)

    var openNewSubjectDialog by mutableStateOf(false)
    var openSaveSubjectsDialog by mutableStateOf(false)
    var openLoadSubjectsDialog by mutableStateOf(false)
    var openRenameScheduleDialog by mutableStateOf(false)
    var openSaveScheduleDialog by mutableStateOf(false)
    var openLoadScheduleDialog by mutableStateOf(false)

    private var currentIndex = 0

    fun changeAutoCompute(value: Boolean) {
        autoCompute = value
    }

    fun computeSchedules() {
        val computedSchedules = subjects.computeSchedules().filter { computedSchedule ->
            schedules.all { it.subjects != computedSchedule.subjects }
        }
        schedules = schedules + computedSchedules
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

    fun renameCurrentSchedule(newLabel: String) {
        require(newLabel.isNotEmpty() && newLabel.isNotBlank()) { "New label cannot be empty" }
        val newSchedule = currentSchedule.copy(label = newLabel)
        schedules = schedules.map {
            if (it == currentSchedule)
                newSchedule
            else it
        }
        currentSchedule = newSchedule
    }

    fun loadSchedule(filename: String) {
        val newSchedule = scheduleStorage.load(filename)
        newSchedule.subjects.forEach { subject ->
            if (subject !in subjects) {
                subjects = subjects + subject
            }
        }
        schedules = schedules.filter { it.subjects != newSchedule.subjects } + newSchedule
        if (autoCompute) computeSchedules()
        val newIndex = schedules.indexOf(newSchedule)
        check(newIndex != -1) { "WHYY?????????" }
        currentIndex = newIndex
        currentSchedule = schedules[currentIndex]

    }

    fun saveCurrentSchedule(filename: String) {
        scheduleStorage.save(filename, currentSchedule)
    }

    private fun deleteSchedule(schedule: Schedule) {
        schedules = schedules - schedule
        currentIndex =
            if (currentIndex == schedules.size || schedules.isEmpty()) 0
            else currentIndex
        currentSchedule =
            if (schedules.isEmpty()) Schedule.EMPTY_SCHEDULE
            else schedules[currentIndex]
    }

    fun deleteCurrentSchedule() = deleteSchedule(currentSchedule)

    fun deleteSubject(subject: Subject) {
        subjects = subjects - subject
        schedules = schedules.filter { subject !in it.subjects }
        if (autoCompute) computeSchedules()
        currentIndex = 0
        currentSchedule =
            if (schedules.isEmpty()) Schedule.EMPTY_SCHEDULE
            else schedules[currentIndex]
    }

    fun addSubject(subject: Subject) {
        subjects = subjects + subject
        if (autoCompute) computeSchedules()
    }

    fun loadSubjects(filename: String) {
        subjects = subjectStorage.load(filename)
        if (autoCompute) computeSchedules()
    }

    fun saveSubjects(filename: String) {
        subjectStorage.save(filename, subjects)
    }
}