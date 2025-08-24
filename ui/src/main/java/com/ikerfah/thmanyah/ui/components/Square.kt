package com.ikerfah.thmanyah.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ikerfah.thmanyah.ui.theme.ThmanyahTheme
import java.time.LocalDateTime

@Composable
fun Square(
    imageUrl: String?,
    title: String,
    durationInSeconds: Int?,
    releaseDate: LocalDateTime?,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier.width(SquareSizes.maxWidth)) {
        CustomImage(imageUrl = imageUrl)
        TitleText(text = title)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            durationInSeconds?.let { Duration(it, modifier = Modifier.padding(start = 8.dp)) }
            releaseDate?.let {
                Date(it)
            }
        }
    }
}

@Preview
@Composable
private fun SquarePreview() {
    ThmanyahTheme {
        Square(
            imageUrl = "https://media.npr.org/assets/img/2018/08/03/npr_tbl_podcasttile_sq-284e5618e2b2df0f3158b076d5bc44751e78e1b6.jpg?s=1400&c=66&f=jpg",
            title = "The Very Big Listen ",
            durationInSeconds = 320,
            releaseDate = LocalDateTime.now()
        )
    }
}

private object SquareSizes {
    val maxWidth = 170.dp
}