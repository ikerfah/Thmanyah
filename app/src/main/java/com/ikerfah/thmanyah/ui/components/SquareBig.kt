package com.ikerfah.thmanyah.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ikerfah.thmanyah.ui.theme.ThmanyahTheme

@Composable
fun SquareBig(
    imageUrl: String?,
    title: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.width(SquareBigSizes.maxWidth),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(
            modifier = Modifier.background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                )
            )
        ) {
            CustomImage(
                imageUrl = imageUrl,
                aspectRatio = 7 / 5f,
                contentScale = ContentScale.FillBounds
            )
            // Scrim overlay for better text visibility
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
            )
            Box(
                Modifier
                    .align(Alignment.BottomEnd)

            ) {
                TitleText(
                    text = title,
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SquareBigPreview() {
    ThmanyahTheme {
        SquareBig(
            imageUrl = "https://media.npr.org/assets/img/2018/08/03/npr_tbl_podcasttile_sq-284e5618e2b2df0f3158b076d5bc44751e78e1b6.jpg?s=1400&c=66&f=jpg",
            title = "The Very Big Listen "
        )
    }
}

private object SquareBigSizes {
    val maxWidth = 260.dp
}