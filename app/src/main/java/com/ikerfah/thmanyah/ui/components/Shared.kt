package com.ikerfah.thmanyah.ui.components

import androidx.annotation.FloatRange
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.ikerfah.thmanyah.R
import com.ikerfah.thmanyah.ui.theme.title

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
fun CustomText(
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