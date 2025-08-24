package com.ikerfah.thmanyah.ui.components

import androidx.annotation.FloatRange
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.ikerfah.thmanyah.ui.R
import com.ikerfah.thmanyah.ui.formatRelativeDateTime
import com.ikerfah.thmanyah.ui.formatSecondsToHoursAndMinutes
import com.ikerfah.thmanyah.ui.theme.ThmanyahTheme
import com.ikerfah.thmanyah.ui.theme.date
import com.ikerfah.thmanyah.ui.theme.duration
import com.ikerfah.thmanyah.ui.theme.title
import java.time.LocalDateTime

@Composable
fun CustomImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, fromInclusive = false) aspectRatio: Float = 1f,
    contentScale: ContentScale = ContentScale.Fit,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = if (LocalInspectionMode.current) {
                    R.drawable.img
                } else {
                    imageUrl
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio),
            contentScale = contentScale
        )
    }
}

@Composable
fun TitleText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    color: Color = MaterialTheme.colorScheme.onPrimary
) {
    Text(
        text = text,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.padding(8.dp),
        color = color,
        style = MaterialTheme.typography.title
    )
}

@Composable
fun Duration(
    durationInSeconds: Int,
    modifier: Modifier = Modifier
) {
    val formattedDuration = formatSecondsToHoursAndMinutes(durationInSeconds)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(25.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(
                start = 8.dp,
                top = 4.dp,
                bottom = 4.dp,
                end = 4.dp,
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = formattedDuration,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.duration
        )
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun Date(
    dateTime: LocalDateTime,
    modifier: Modifier = Modifier
) {
    val formattedDate = formatRelativeDateTime(dateTime)
    Text(
        text = formattedDate,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.date,
        modifier = modifier
    )
}

@Preview
@Composable
private fun DurationPreview() {
    ThmanyahTheme {
        Duration(durationInSeconds = 320)
    }
}

@Preview
@Composable
private fun DatePreview() {
    ThmanyahTheme {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {
            val now = LocalDateTime.now()
            Date(now)
            Date(now.minusHours(5))
            Date(now.minusDays(1))
            Date(now.minusDays(2))
            Date(now.minusWeeks(1))
            Date(now.minusWeeks(2))
        }
    }
}