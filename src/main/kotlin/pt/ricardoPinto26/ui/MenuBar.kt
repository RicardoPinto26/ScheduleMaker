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
            Item("Add", onClick = onAddSubject)
            Item("Load", onClick = onLoadSubjects)
            Item("Save", onClick = onSaveSubjects)
        }
        Menu("Schedule") {
            Item("Load [TODO]", onClick = onLoadSchedule)
            Item("Save [wip]", onClick = onSaveSchedule)
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