package pt.ricardoPinto26.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar

@Composable
fun FrameWindowScope.ScheduleMenu(
    autoCompute: Boolean,
    onAddSubject: () -> Unit,
    onLoadSchedule: () -> Unit,
    onSaveSubjects: () -> Unit,
    onSaveSchedule: () -> Unit,
    onAutoComputeChange: (Boolean) -> Unit,
) = MenuBar {
    Menu("File") {
        Item("Add Subject", onClick = onAddSubject)
        Item("Load Schedule [TODO]", onClick = onLoadSchedule)
        Item("Save Subjects [WIP]", onClick = onSaveSubjects)
        Item("Save Schedule [TODO]", onClick = onSaveSchedule)
    }
    Menu("Options") {
        CheckboxItem(
            text = "Automatically Compute Schedules",
            checked = autoCompute,
            onCheckedChange = onAutoComputeChange
        )
    }
}