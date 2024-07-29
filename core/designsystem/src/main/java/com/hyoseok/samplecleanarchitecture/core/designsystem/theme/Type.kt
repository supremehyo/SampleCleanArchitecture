package com.hyoseok.samplecleanarchitecture.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hyoseok.samplecleanarchitecture.core.designsystem.R

val pretendard = FontFamily(
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),
    Font(R.font.pretendard_light, FontWeight.Light),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
)

private val PretendardStyle = TextStyle(
    fontFamily = pretendard,
    fontWeight = FontWeight.Normal,
)

internal val Typography = SampleTypography(
    displayLargeR = PretendardStyle.copy(
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMediumR = PretendardStyle.copy(
        fontSize = 45.sp,
        lineHeight = 52.sp,
    ),
    displaySmallR = PretendardStyle.copy(
        fontSize = 36.sp,
        lineHeight = 44.sp,
    ),
    headlineLargeEB = PretendardStyle.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.ExtraBold,
    ),
    headlineLargeSB = PretendardStyle.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    headlineLargeR = PretendardStyle.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    headlineMediumB = PretendardStyle.copy(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Bold,
    ),
    headlineMediumM = PretendardStyle.copy(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Medium,
    ),
    headlineMediumR = PretendardStyle.copy(
        fontSize = 28.sp,
        lineHeight = 36.sp,
    ),
    headlineSmallBL = PretendardStyle.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = (-0.2).sp,
    ),
    headlineSmallM = PretendardStyle.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Medium,
    ),
    headlineSmallR = PretendardStyle.copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    titleLargeBL = PretendardStyle.copy(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Black,
    ),
    titleLargeB = PretendardStyle.copy(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleLargeM = PretendardStyle.copy(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Medium,
    ),
    titleLargeR = PretendardStyle.copy(
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    titleMediumBL = PretendardStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Black,
    ),
    titleMediumB = PretendardStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleMediumR = PretendardStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    titleSmallB = PretendardStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.25.sp,
    ),
    titleSmallM = PretendardStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.25.sp,
    ),
    titleSmallM140 = PretendardStyle.copy(
        fontSize = 14.sp,
        lineHeight = (19.6).sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = (-0.2).sp,
    ),
    titleSmallR140 = PretendardStyle.copy(
        fontSize = 14.sp,
        lineHeight = (19.6).sp,
        letterSpacing = (-0.2).sp,
    ),
    titleSmallR = PretendardStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodyLargeR = PretendardStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMediumR = PretendardStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmallR = PretendardStyle.copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)

@Immutable
data class SampleTypography(
    val displayLargeR: TextStyle,
    val displayMediumR: TextStyle,
    val displaySmallR: TextStyle,

    val headlineLargeEB: TextStyle,
    val headlineLargeSB: TextStyle,
    val headlineLargeR: TextStyle,
    val headlineMediumB: TextStyle,
    val headlineMediumM: TextStyle,
    val headlineMediumR: TextStyle,
    val headlineSmallBL: TextStyle,
    val headlineSmallM: TextStyle,
    val headlineSmallR: TextStyle,

    val titleLargeBL: TextStyle,
    val titleLargeB: TextStyle,
    val titleLargeM: TextStyle,
    val titleLargeR: TextStyle,
    val titleMediumBL: TextStyle,
    val titleMediumB: TextStyle,
    val titleMediumR: TextStyle,
    val titleSmallB: TextStyle,
    val titleSmallM: TextStyle,
    val titleSmallM140: TextStyle,
    val titleSmallR: TextStyle,
    val titleSmallR140: TextStyle,

    val bodyLargeR: TextStyle,
    val bodyMediumR: TextStyle,
    val bodySmallR: TextStyle,
)

val LocalTypography = staticCompositionLocalOf {
    SampleTypography(
        displayLargeR = PretendardStyle,
        displayMediumR = PretendardStyle,
        displaySmallR = PretendardStyle,
        headlineLargeEB = PretendardStyle,
        headlineLargeSB = PretendardStyle,
        headlineLargeR = PretendardStyle,
        headlineMediumB = PretendardStyle,
        headlineMediumM = PretendardStyle,
        headlineMediumR = PretendardStyle,
        headlineSmallBL = PretendardStyle,
        headlineSmallM = PretendardStyle,
        headlineSmallR = PretendardStyle,
        titleLargeBL = PretendardStyle,
        titleLargeB = PretendardStyle,
        titleLargeM = PretendardStyle,
        titleLargeR = PretendardStyle,
        titleMediumBL = PretendardStyle,
        titleMediumB = PretendardStyle,
        titleMediumR = PretendardStyle,
        titleSmallB = PretendardStyle,
        titleSmallM = PretendardStyle,
        titleSmallM140 = PretendardStyle,
        titleSmallR = PretendardStyle,
        titleSmallR140 = PretendardStyle,
        bodyLargeR = PretendardStyle,
        bodyMediumR = PretendardStyle,
        bodySmallR = PretendardStyle,
    )
}