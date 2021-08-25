package com.github.amrmsaraya.weather.presentation.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.github.amrmsaraya.weather.R

val Spartan = FontFamily(
    Font(R.font.spartan_bold)
)

val Cairo = FontFamily(
    Font(R.font.cairo_regular, FontWeight.Normal),
    Font(R.font.cairo_bold, FontWeight.Bold)
)


// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val ArabicTypography = Typography(defaultFontFamily = Cairo)
