package pt.ricardoPinto26.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.FrameWindowScope
import java.awt.FileDialog
import kotlinx.coroutines.launch
import pt.ricardoPinto26.model.Schedule
import pt.ricardoPinto26.model.Subject
import pt.ricardoPinto26.storage.Storage

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
        Column {
            Column {
                Row {
                    Button({
                        viewModel.prevSchedule()
                    }, enabled = viewModel.schedules.size !in 0..1) {
                        Text("Prev Schedule")
                    }
                    Button({
                        viewModel.nextSchedule()
                    }, enabled = viewModel.schedules.size !in 0..1) {
                        Text("Next Schedule")
                    }
                }
                Row {
                    Button(
                        {
                            viewModel.openRenameScheduleDialog = true
                        },
                        enabled = viewModel.currentSchedule != Schedule.EMPTY_SCHEDULE
                    ) {
                        Text("Rename Schedule")
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
            }
            Row {
                SubjectListView(viewModel.subjects) {
                    viewModel.deleteSubject(it)
                }
                Button(
                    {
                        viewModel.computeSchedules()
                    },
                    enabled = viewModel.subjects.isNotEmpty()
                ) {
                    Text("Compute Schedules")
                }
            }
        }
    }

    val currentException = viewModel.exception

    if (viewModel.openErrorDialog && currentException != null) {
        OpenExceptionDialog(
            exception = currentException,
            onCancel = { viewModel.openErrorDialog = false; viewModel.exception = null }
        )
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
                FileDialog(window, "Save Subjects", FileDialog.SAVE).apply {
                    file = "*.subjects"
                    isVisible = true
                }.let {
                    (it.directory ?: return@let null) + (it.file ?: return@let null)
                } ?: return@launch
            )
        }
    }
    if (viewModel.openLoadSubjectsDialog) {
        viewModel.openLoadSubjectsDialog = false
        scope.launch {
            try {
                viewModel.loadSubjects(
                    FileDialog(window, "Load Subjects", FileDialog.LOAD).apply {
                        file = "*.subjects"
                        isVisible = true
                    }.let {
                        (it.directory ?: return@let null) + (it.file ?: return@let null)
                    } ?: return@launch
                )
            } catch (e: IllegalArgumentException) {
                viewModel.openErrorDialog = true
                viewModel.exception = IllegalArgumentException("File not formatted correctly.\nNo subjects loaded.", e)
            }
        }
    }

    if (viewModel.openLoadScheduleDialog) {
        viewModel.openLoadScheduleDialog = false
        scope.launch {
            try {
                viewModel.loadSchedule(
                    FileDialog(window, "Load Schedule", FileDialog.LOAD).apply {
                        file = "*.schedule"
                        isVisible = true
                    }.let {
                        (it.directory ?: return@let null) + (it.file ?: return@let null)
                    } ?: return@launch
                )
            } catch (e: IllegalArgumentException) {
                viewModel.openErrorDialog = true
                viewModel.exception =
                    IllegalArgumentException("File not formatted correctly.\nSchedule was not loaded.", e)
            }
        }
    }

    if (viewModel.openSaveScheduleDialog) {
        viewModel.openSaveScheduleDialog = false
        scope.launch {
            viewModel.saveCurrentSchedule(
                FileDialog(window, "Save Schedule", FileDialog.SAVE).apply {
                    file = "*.schedule"
                    isVisible = true
                }.let {
                    (it.directory ?: return@let null) + (it.file ?: return@let null)
                } ?: return@launch
            )
        }
    }

    if (viewModel.openRenameScheduleDialog) {
        GetStringDialog(
            title = "Rename Schedule",
            label = "New Label",
            onInfoEntered = {
                viewModel.renameCurrentSchedule(it)
                viewModel.openRenameScheduleDialog = false
            },
            onCancel = { viewModel.openRenameScheduleDialog = false })
    }
}
