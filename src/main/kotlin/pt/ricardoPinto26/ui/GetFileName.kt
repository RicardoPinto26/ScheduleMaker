package pt.ricardoPinto26.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState

@Composable
fun GetFileName(onInfoEntered: (String) -> Unit, onCancel: () -> Unit) = Dialog(
    onCloseRequest = onCancel,
    title = "Save Subjects",
    resizable = false,
    state = DialogState(size = DpSize.Unspecified)
) {
    var filename by remember { mutableStateOf("") }
    Row {
        TextField(
            label = { Text(text = "Filename*") },
            value = filename,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFEFEFEF)),
            onValueChange = { filename = it }
        )
        Button(onClick = { onInfoEntered(filename) }) {
            Text("OK")
        }
    }
}