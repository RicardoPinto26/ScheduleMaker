package pt.ricardoPinto26.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ricardoPinto26.model.MeetingTime
import pt.ricardoPinto26.model.Subject

@Composable
fun MeetingTimeView(meetingTime: MeetingTime) {
    Box(
        Modifier.width(SEGMENT_WIDTH).height(SEGMENT_HEIGHT * meetingTime.duration / SEGMENT_DURATION)
            .background(Color.LightGray).border(BORDER_THICKNESS, Color.Black),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.padding(5.dp)) {
                Text(meetingTime.subjectName)
                if (meetingTime.professor != null) Text(meetingTime.professor)
                Text(meetingTime.classId.toString())
            }
            if (meetingTime.room != null) Text(modifier = Modifier.padding(5.dp), text = meetingTime.room)
        }
    }
}


@Composable
fun SubjectListView(list: List<Subject>, onDelete: (Subject) -> Unit) {
    Column(Modifier.background(Color.LightGray).border(BORDER_THICKNESS, Color.Black)) {
        list.forEach {
            Button(modifier = Modifier.padding(0.dp), onClick = { onDelete(it) }) {
                Text("${it.name} ${it.classId}")
            }

        }
    }
}
