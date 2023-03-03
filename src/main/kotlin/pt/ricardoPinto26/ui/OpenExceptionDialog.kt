package pt.ricardoPinto26.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState

@Composable
fun OpenExceptionDialog(exception: Exception, onCancel: () -> Unit) = Dialog(
    onCloseRequest = onCancel,
    title = "Error!",
    resizable = false,
    state = DialogState(size = DpSize.Unspecified)
) {
    Column {
        Text(exception.message ?: "An unexpected exception has been found!")
        Button(onClick = { onCancel() }) {
            Text("OK")
        }
    }
}