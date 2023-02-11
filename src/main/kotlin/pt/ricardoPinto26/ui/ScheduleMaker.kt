package pt.ricardoPinto26.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.FrameWindowScope
import kotlinx.coroutines.launch
import pt.ricardoPinto26.model.Schedule
import pt.ricardoPinto26.model.Subject
import pt.ricardoPinto26.storage.Storage
import java.awt.FileDialog

@Composable
fun FrameWindowScope.ScheduleMaker(
    scheduleStorage: Storage<String, Schedule>,
    subjectStorage: Storage<String, List<Subject>>,
    loadedSchedules: List<Schedule>,
    loadedSubjects: List<Subject>
) {
    val viewModel = remember { ViewModel(scheduleStorage, subjectStorage, loadedSchedules, loadedSubjects) }
    val scope = rememberCoroutineScope()

    ScheduleMenu(
        autoCompute = viewModel.autoCompute,
        onAddSubject = {
            viewModel.openNewSubjectDialog = true
        },
        onLoadSchedule = {
            viewModel.openLoadScheduleDialog = true
        },
        onLoadSubjects = {
            viewModel.openLoadSubjectsDialog = true
        },
        onSaveSubjects = {
            viewModel.openSaveSubjectsDialog = true
        },
        onSaveSchedule = {
            viewModel.openSaveScheduleDialog = true
        },
        onAutoComputeChange = { viewModel.changeAutoCompute(it) },
    )

    Row {
        ScheduleView(viewModel.currentSchedule)
        Button({
            viewModel.prevSchedule()
        }, enabled = viewModel.schedules.size !in 0..1) {
            Text("Prev Schedule")
        }
        Column {
            Row {
                Button({
                    viewModel.nextSchedule()
                }, enabled = viewModel.schedules.size !in 0..1) {
                    Text("Next Schedule")
                }
                Button(
                    {
                        viewModel.deleteCurrentSchedule()
                    },
                    enabled = !(viewModel.schedules.all { it == Schedule.EMPTY_SCHEDULE })
                ) {
                    Text("Delete Schedule")
                }
            }
            Button(
                {
                    viewModel.computeSchedules()
                },
                enabled = viewModel.subjects.isNotEmpty()
            ) {
                Text("Compute Schedules")
            }
            SubjectListView(viewModel.subjects) {
                viewModel.deleteSubject(it)
            }
        }
    }

    if (viewModel.openNewSubjectDialog) {
        GetNewSubject(
            onInfoEntered = { subject ->
                viewModel.addSubject(subject)
                viewModel.openNewSubjectDialog = false
            },
            onCancel = { viewModel.openNewSubjectDialog = false }
        )
    }
    if (viewModel.openSaveSubjectsDialog) {
        viewModel.openSaveSubjectsDialog = false
        scope.launch {
            viewModel.saveSubjects(
                FileDialog(window, "TITLE WIP", FileDialog.SAVE).apply {
                    file = "*.subjects"
                    isVisible = true
                }.file ?: return@launch
            )
        }
    }
    if (viewModel.openLoadSubjectsDialog) {
        viewModel.openLoadSubjectsDialog = false
        scope.launch {
            viewModel.loadSubjects(
                FileDialog(window, "TITLE WIP", FileDialog.LOAD).apply {
                    file = "*.subjects"
                    isVisible = true
                }.file ?: return@launch
            )
        }
    }

    if (viewModel.openLoadScheduleDialog) {
        viewModel.openLoadScheduleDialog = false
        scope.launch {
            viewModel.loadSchedule(
                FileDialog(window, "TITLE WIP", FileDialog.LOAD).apply {
                    file = "*.schedule"
                    isVisible = true
                }.file ?: return@launch
            )
        }
    }

    if (viewModel.openSaveScheduleDialog) {
        viewModel.openSaveScheduleDialog = false
        scope.launch {
            viewModel.saveCurrentSchedule(
                FileDialog(window, "TITLE WIP", FileDialog.SAVE).apply {
                    file = "*.schedule"
                    isVisible = true
                }.file ?: return@launch
            )
        }
    }
}
