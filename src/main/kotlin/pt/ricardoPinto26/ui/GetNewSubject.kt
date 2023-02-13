package pt.ricardoPinto26.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogState
import pt.ricardoPinto26.model.Day
import pt.ricardoPinto26.model.MeetingTime
import pt.ricardoPinto26.model.Subject
import pt.ricardoPinto26.model.toTime

@Composable
fun GetNewSubject(onInfoEntered: (Subject) -> Unit, onCancel: () -> Unit) = Dialog(
    onCloseRequest = onCancel,
    title = "New Subject",
    resizable = false,
    state = DialogState(size = DpSize.Unspecified)
) {
    val radioOptions = Day.values()
    var selectedDay by remember { mutableStateOf(radioOptions.first()) }

    var subjectName by remember { mutableStateOf("") }
    var professor by remember { mutableStateOf("") }
    var classId by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }
    var startTimeHours by remember { mutableStateOf("") }
    var startTimeMinutes by remember { mutableStateOf("") }
    var endTimeHours by remember { mutableStateOf("") }
    var endTimeMinutes by remember { mutableStateOf("") }

    var meetingTimes by remember { mutableStateOf(listOf<MeetingTime>()) }

    val maybeOnGameIdEntered = {
        require(meetingTimes.isNotEmpty()) { "You did not insert any meeting times." }
        onInfoEntered(
            Subject(
                subjectName,
                professor.ifEmpty { null },
                classId.toInt(),
                meetingTimes
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
        TextField(
            label = { Text(text = "Room") },
            value = room,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFEFEFEF)),
            onValueChange = { room = it }
        )
        Row {
            radioOptions.forEach {
                Column {
                    Text(it.name.first().toString())
                    RadioButton(
                        selected = it == selectedDay,
                        onClick = { selectedDay = it }
                    )
                }
            }
        }
        Text(text = "Start Time:*", modifier = Modifier.padding(8.dp))
        Row {
            TextField(
                label = { Text(text = "hours") },
                value = startTimeHours,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFEFEFEF)),
                onValueChange = { startTimeHours = it },
                placeholder = { Text(text = "12") }
            )
            TextField(
                label = { Text(text = "minutes") },
                value = startTimeMinutes,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFEFEFEF)),
                onValueChange = { startTimeMinutes = it },
                placeholder = { Text(text = "00") }
            )
        }
        Text(text = "End Time:*", modifier = Modifier.padding(8.dp))
        Row {
            TextField(
                label = { Text(text = "hours") },
                value = endTimeHours,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFEFEFEF)),
                onValueChange = { endTimeHours = it },
                placeholder = { Text(text = "12") }
            )
            TextField(
                label = { Text(text = "minutes") },
                value = endTimeMinutes,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFEFEFEF)),
                onValueChange = { endTimeMinutes = it },
                placeholder = { Text(text = "00") }
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = {
            require(subjectName.isNotEmpty() && subjectName.isNotBlank()) { "Subject's Name cannot be empty" }
            require(professor.isNotEmpty() && professor.isNotBlank()) { "Professor's Name cannot be empty" }
            require(classId.isNotEmpty() && classId.isNotBlank() && classId.all { it.isDigit() }) {
                "Class must be a number and cannot be empty"
            }
            require(startTimeHours.isNotEmpty() && startTimeHours.isNotBlank() && startTimeHours.all { it.isDigit() }) {
                "Starting Hour must be a number and cannot be empty"
            }
            require(endTimeHours.isNotEmpty() && endTimeHours.isNotBlank() && endTimeHours.all { it.isDigit() }) {
                "Ending Hour must be a number and cannot be empty"
            }
            meetingTimes = meetingTimes + MeetingTime(
                day = selectedDay,
                startTime = "$startTimeHours.${startTimeMinutes.ifEmpty { "00" }}".toTime(),
                endTime = "$endTimeHours.${endTimeMinutes.ifEmpty { "00" }}".toTime(),
                subjectName = subjectName,
                professor = professor,
                classId = classId.toInt(),
                room = room.ifEmpty { null }
            )
            startTimeHours = ""
            startTimeMinutes = ""
            endTimeHours = ""
            endTimeMinutes = ""
        }) {
            Text(text = "Add Meeting Time")
        }
        Button(onClick = maybeOnGameIdEntered) {
            Text(text = "OK")
        }
    }
}