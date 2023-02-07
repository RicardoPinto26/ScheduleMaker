package pt.ricardoPinto26.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import pt.ricardoPinto26.model.MeetingTime

@Composable
fun MeetingTimeView(meetingTime: MeetingTime) {
    Box(
        Modifier.width(SEGMENT_WIDTH).height(SEGMENT_HEIGHT * meetingTime.duration / SEGMENT_DURATION)
            .background(Color.LightGray).border(BORDER_THICKNESS, Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(meetingTime.label)
    }
}
