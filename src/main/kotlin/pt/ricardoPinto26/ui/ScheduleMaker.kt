package pt.ricardoPinto26.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.FrameWindowScope
import pt.ricardoPinto26.model.Schedule
import pt.ricardoPinto26.model.Subject
import pt.ricardoPinto26.storage.Storage

@Composable
fun FrameWindowScope.ScheduleMaker(
    storage: Storage<String, List<Subject>>,
    loadedSchedules: List<Schedule>,
    loadedSubjects: List<Subject>
) {
    val viewModel = remember { ViewModel(storage, loadedSchedules, loadedSubjects) }

    ScheduleMenu(
        autoCompute = viewModel.autoCompute,
        onAddSubject = {
            viewModel.openNewSubjectDialog = true
        },
        onLoadSchedule = {

        },
        onSaveSubjects = {
            viewModel.openSaveSubjectsDialog = true
        },
        onSaveSchedule = {

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
        GetFileName(
            onInfoEntered = { filename ->
                viewModel.saveSubjects(filename)
                viewModel.openSaveSubjectsDialog = false
            },
            onCancel = { viewModel.openSaveSubjectsDialog = false }
        )
    }
}
