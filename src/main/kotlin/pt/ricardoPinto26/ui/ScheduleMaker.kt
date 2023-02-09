package pt.ricardoPinto26.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import pt.ricardoPinto26.model.Schedule
import pt.ricardoPinto26.model.Subject
import pt.ricardoPinto26.model.computeSchedules

@Composable
fun ScheduleMaker(loadedSchedules: List<Schedule>, loadedSubjects: List<Subject>) {
    val viewModel = remember { ViewModel(loadedSchedules, loadedSubjects) }

    Row {
        ScheduleView(viewModel.currentSchedule)
        Button({
            viewModel.currentIndex =
                if (viewModel.currentIndex == 0) viewModel.schedules.size - 1
                else viewModel.currentIndex - 1
            viewModel.currentSchedule = viewModel.schedules[viewModel.currentIndex]
        }, enabled = viewModel.schedules.size !in 0..1) {
            Text("Prev Schedule")
        }
        Column {
            Row {
                Button({
                    viewModel.currentIndex =
                        if (viewModel.currentIndex == viewModel.schedules.size - 1) 0
                        else viewModel.currentIndex + 1
                    viewModel.currentSchedule = viewModel.schedules[viewModel.currentIndex]
                }, enabled = viewModel.schedules.size !in 0..1) {
                    Text("Next Schedule")
                }
                Button(
                    {
                        viewModel.schedules = viewModel.schedules - viewModel.currentSchedule
                        viewModel.currentIndex =
                            if (viewModel.currentIndex == viewModel.schedules.size) 0
                            else viewModel.currentIndex
                        if (viewModel.schedules.isEmpty()) viewModel.schedules =
                            viewModel.schedules + Schedule.EMPTY_SCHEDULE
                        viewModel.currentSchedule = viewModel.schedules[viewModel.currentIndex]
                    },
                    enabled = !(viewModel.schedules.all { it == Schedule.EMPTY_SCHEDULE })
                ) {
                    Text("Delete Schedule")
                }
            }
            Row {
                SubjectListView(viewModel.subjects) {
                    viewModel.subjects = viewModel.subjects - it
                    viewModel.schedules = viewModel.subjects.computeSchedules()
                    viewModel.currentIndex = 0
                    viewModel.currentSchedule = viewModel.schedules.firstOrNull() ?: Schedule.EMPTY_SCHEDULE
                }
                Button({
                    TODO()
                }) {
                    Text("Add Subject")
                }
            }
        }
    }
}