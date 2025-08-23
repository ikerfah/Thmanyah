package com.ikerfah.thmanyah.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp


@Composable
fun Queue(
    imagesUrl: List<String?>,
    title: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                bottom = 16.dp,
                end = 8.dp,
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OverlappingRow(overlappingPercentage = 0.88f) {
                imagesUrl.forEach { url ->
                    CustomImage(
                        modifier = Modifier.width(QueueSizes.maxWidth),
                        imageUrl = url,
                    )
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                title?.let {
                    TitleText(
                        text = it,
                        maxLines = 2,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
            IconButton(
                onClick = {/* Not Implemented */ },
                modifier = Modifier.align(Alignment.Bottom)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
private fun OverlappingRow(
    overlappingPercentage: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {

    val factor = (1 - overlappingPercentage)
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = { measurables, constraints ->
            val placeables = measurables.map { it.measure(constraints) }
            val widthsExceptFirst = placeables.subList(1, placeables.size).sumOf { it.width }
            val firstWidth = placeables.getOrNull(0)?.width ?: 0
            val width = (widthsExceptFirst * factor + firstWidth).toInt()
            val height = placeables.maxOf { it.height }
            layout(width, height) {
                var x = 0
                for (placeable in placeables) {
                    placeable.placeRelative(x, 0, 0f)
                    x += (placeable.width * factor).toInt()
                }
            }
        }
    )
}

private object QueueSizes {
    val maxWidth = 130.dp
}