package pt.ricardoPinto26.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

val SUBJECT_BUTTON_HEIGHT = 34.dp

@Composable
fun SubjectListView(list: List<Subject>, onDelete: (Subject) -> Unit) {
    Column(
        modifier = Modifier.background(Color.LightGray).border(BORDER_THICKNESS, Color.Black).width(SEGMENT_WIDTH * 2)
            .heightIn(min = SUBJECT_BUTTON_HEIGHT)
    ) {
        list.forEach {
            Button(
                border = BorderStroke(BORDER_THICKNESS, Color.Black),
                shape = RoundedCornerShape(100),
                modifier = Modifier.padding(0.dp).height(SUBJECT_BUTTON_HEIGHT).fillMaxWidth(),
                onClick = { onDelete(it) }) {
                Text(
                    modifier = Modifier.padding(0.dp),
                    fontSize = 12.sp,
                    text = "${it.name} ${it.professor} ${it.classId}"
                )
            }

        }
    }
}
