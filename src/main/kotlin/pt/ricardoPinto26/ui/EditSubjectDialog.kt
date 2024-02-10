package pt.ricardoPinto26.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import pt.ricardoPinto26.model.Subject

@Composable
fun EditSubjectDialog(subject: Subject, onInfoEntered: (Subject?) -> Unit, onCancel: () -> Unit) = Dialog(
    onCloseRequest = onCancel,
    title = "New Subject",
    resizable = false,
    state = DialogState(size = DpSize.Unspecified)
) {
    var subjectName by remember { mutableStateOf(subject.name) }
    var professor by remember { mutableStateOf(subject.professor ?: "") }
    var classId by remember { mutableStateOf(subject.classId.toString()) }

    val maybeOnGameIdEntered = {
        onInfoEntered(
            Subject(
                subjectName,
                professor.ifEmpty { null },
                classId.toInt(),
                subject.meetingTimes
            )
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            label = { Text(text = "Subject Name*") },
            value = subjectName,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFEFEFEF)),
            onValueChange = { subjectName = it }
        )
        TextField(
            label = { Text(text = "Professor's Name*") },
            value = professor,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFEFEFEF)),
            onValueChange = { professor = it }
        )
        TextField(
            label = { Text(text = "Class Number*") },
            value = classId,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFEFEFEF)),
            onValueChange = { classId = it }
        )
        Row {
            Button(onClick = { onInfoEntered(null) }) {
                Text(text = "Delete")
            }
            Button(onClick = maybeOnGameIdEntered) {
                Text(text = "OK")
            }
        }
    }
}