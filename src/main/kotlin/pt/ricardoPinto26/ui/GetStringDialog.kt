package pt.ricardoPinto26.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState

@Composable
fun GetStringDialog(title: String, label: String, onInfoEntered: (String) -> Unit, onCancel: () -> Unit) = Dialog(
    onCloseRequest = onCancel,
    title = title,
    resizable = false,
    state = DialogState(size = DpSize.Unspecified)
) {
    var input by remember { mutableStateOf("") }
    Row {
        TextField(
            label = { Text(text = label) },
            value = input,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFEFEFEF)),
            onValueChange = { input = it }
        )
        Button(onClick = { onInfoEntered(input) }) {
            Text("OK")
        }
    }
}