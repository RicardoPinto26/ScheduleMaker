package pt.ricardoPinto26.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar

@Composable
fun FrameWindowScope.ScheduleMenu(
    autoCompute: Boolean,
    onAddSubject: () -> Unit,
    onLoadSchedule: () -> Unit,
    onLoadSubjects: () -> Unit,
    onSaveSubjects: () -> Unit,
    onSaveSchedule: () -> Unit,
    onAutoComputeChange: (Boolean) -> Unit,
) = MenuBar {
    Menu("File") {
        Menu("Subject") {
            Item("New", onClick = onAddSubject)
            Item("Add", onClick = onLoadSubjects)
            Item("Save", onClick = onSaveSubjects)
        }
        Menu("Schedule") {
            Item("Load", onClick = onLoadSchedule)
            Item("Save", onClick = onSaveSchedule)
        }
    }
    Menu("Options") {
        CheckboxItem(
            text = "Automatically Compute Schedules",
            checked = autoCompute,
            onCheckedChange = onAutoComputeChange
        )
    }
}