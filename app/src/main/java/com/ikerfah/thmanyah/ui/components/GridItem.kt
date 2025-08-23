package com.ikerfah.thmanyah.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.ikerfah.thmanyah.R
import com.ikerfah.thmanyah.ui.theme.ThmanyahTheme
import java.time.LocalDateTime

@Composable
fun GridItem(
    imageUrl: String?,
    title: String,
    durationInSeconds: Int?,
    releaseDate: LocalDateTime?,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.height(GridItemSizes.maxHeight)) {
        Card(
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
                contentDescription = title,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )
        }

        Column {
            releaseDate?.let {
                Date(
                    dateTime = it,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            TitleText(
                text = title,
                maxLines = 2,
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.onPrimary,
            )
            durationInSeconds?.let {
                Duration(it, modifier = Modifier.padding(start = 16.dp))
            }
        }
    }
}

@Preview
@Composable
private fun GridItemPreview() {
    ThmanyahTheme {
        GridItem(
            imageUrl = "https://media.npr.org/assets/img/2018/08/03/npr_tbl_podcasttile_sq-284e5618e2b2df0f3158b076d5bc44751e78e1b6.jpg?s=1400&c=66&f=jpg",
            title = "The Very Big Listen ",
            durationInSeconds = 320,
            releaseDate = LocalDateTime.now()
        )
    }
}

internal object GridItemSizes {
    val maxHeight = 150.dp
}