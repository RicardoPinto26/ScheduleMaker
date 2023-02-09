package pt.ricardoPinto26.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar

@Composable
fun FrameWindowScope.ScheduleMenu(
    autoCompute: Boolean,
    onAddSubject: () -> Unit,
    onLoadSchedule: () -> Unit,
    onAutoComputeChange: (Boolean) -> Unit,
) = MenuBar {
    Menu("File") {
        Item("Add Subject [WIP]", onClick = onAddSubject)
        Item("Load Schedule [TODO]", onClick = onLoadSchedule)
    }
    Menu("Option") {
        CheckboxItem(
            text = "Automatically Compute Schedules",
            checked = autoCompute,
            onCheckedChange = onAutoComputeChange
        )
    }
}