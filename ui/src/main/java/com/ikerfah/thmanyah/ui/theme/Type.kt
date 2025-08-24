package com.ikerfah.thmanyah.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ikerfah.thmanyah.ui.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val Typography.sectionHeader: TextStyle
    get() = TextStyle(
        fontFamily = IBMPlexSansArabicFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

val Typography.title: TextStyle
    get() = TextStyle(
        fontFamily = IBMPlexSansArabicFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    )

val Typography.duration: TextStyle
    get() = TextStyle(
        fontFamily = IBMPlexSansArabicFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    )

val Typography.date: TextStyle
    get() = TextStyle(
        fontFamily = IBMPlexSansArabicFamily,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

val Typography.appBarText: TextStyle
    get() = TextStyle(
        fontFamily = IBMPlexSansArabicFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    )

private val IBMPlexSansArabicFamily = FontFamily(
    Font(R.font.ibmplexsansarabic_light, FontWeight.Light),
    Font(R.font.ibmplexsansarabic_regular, FontWeight.Normal),
    Font(R.font.ibmplexsansarabic_medium, FontWeight.Medium),
    Font(R.font.ibmplexsansarabic_bold, FontWeight.Bold)
)