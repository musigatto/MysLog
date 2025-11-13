package com.example.compose
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.myslog.ui.theme.backgroundDark
import com.example.myslog.ui.theme.backgroundDarkHighContrast
import com.example.myslog.ui.theme.backgroundDarkMediumContrast
import com.example.myslog.ui.theme.backgroundLight
import com.example.myslog.ui.theme.backgroundLightHighContrast
import com.example.myslog.ui.theme.backgroundLightMediumContrast
import com.example.myslog.ui.theme.errorContainerDark
import com.example.myslog.ui.theme.errorContainerDarkHighContrast
import com.example.myslog.ui.theme.errorContainerDarkMediumContrast
import com.example.myslog.ui.theme.errorContainerLight
import com.example.myslog.ui.theme.errorContainerLightHighContrast
import com.example.myslog.ui.theme.errorContainerLightMediumContrast
import com.example.myslog.ui.theme.errorDark
import com.example.myslog.ui.theme.errorDarkHighContrast
import com.example.myslog.ui.theme.errorDarkMediumContrast
import com.example.myslog.ui.theme.errorLight
import com.example.myslog.ui.theme.errorLightHighContrast
import com.example.myslog.ui.theme.errorLightMediumContrast
import com.example.myslog.ui.theme.inverseOnSurfaceDark
import com.example.myslog.ui.theme.inverseOnSurfaceDarkHighContrast
import com.example.myslog.ui.theme.inverseOnSurfaceDarkMediumContrast
import com.example.myslog.ui.theme.inverseOnSurfaceLight
import com.example.myslog.ui.theme.inverseOnSurfaceLightHighContrast
import com.example.myslog.ui.theme.inverseOnSurfaceLightMediumContrast
import com.example.myslog.ui.theme.inversePrimaryDark
import com.example.myslog.ui.theme.inversePrimaryDarkHighContrast
import com.example.myslog.ui.theme.inversePrimaryDarkMediumContrast
import com.example.myslog.ui.theme.inversePrimaryLight
import com.example.myslog.ui.theme.inversePrimaryLightHighContrast
import com.example.myslog.ui.theme.inversePrimaryLightMediumContrast
import com.example.myslog.ui.theme.inverseSurfaceDark
import com.example.myslog.ui.theme.inverseSurfaceDarkHighContrast
import com.example.myslog.ui.theme.inverseSurfaceDarkMediumContrast
import com.example.myslog.ui.theme.inverseSurfaceLight
import com.example.myslog.ui.theme.inverseSurfaceLightHighContrast
import com.example.myslog.ui.theme.inverseSurfaceLightMediumContrast
import com.example.myslog.ui.theme.onBackgroundDark
import com.example.myslog.ui.theme.onBackgroundDarkHighContrast
import com.example.myslog.ui.theme.onBackgroundDarkMediumContrast
import com.example.myslog.ui.theme.onBackgroundLight
import com.example.myslog.ui.theme.onBackgroundLightHighContrast
import com.example.myslog.ui.theme.onBackgroundLightMediumContrast
import com.example.myslog.ui.theme.onErrorContainerDark
import com.example.myslog.ui.theme.onErrorContainerDarkHighContrast
import com.example.myslog.ui.theme.onErrorContainerDarkMediumContrast
import com.example.myslog.ui.theme.onErrorContainerLight
import com.example.myslog.ui.theme.onErrorContainerLightHighContrast
import com.example.myslog.ui.theme.onErrorContainerLightMediumContrast
import com.example.myslog.ui.theme.onErrorDark
import com.example.myslog.ui.theme.onErrorDarkHighContrast
import com.example.myslog.ui.theme.onErrorDarkMediumContrast
import com.example.myslog.ui.theme.onErrorLight
import com.example.myslog.ui.theme.onErrorLightHighContrast
import com.example.myslog.ui.theme.onErrorLightMediumContrast
import com.example.myslog.ui.theme.onPrimaryContainerDark
import com.example.myslog.ui.theme.onPrimaryContainerDarkHighContrast
import com.example.myslog.ui.theme.onPrimaryContainerDarkMediumContrast
import com.example.myslog.ui.theme.onPrimaryContainerLight
import com.example.myslog.ui.theme.onPrimaryContainerLightHighContrast
import com.example.myslog.ui.theme.onPrimaryContainerLightMediumContrast
import com.example.myslog.ui.theme.onPrimaryDark
import com.example.myslog.ui.theme.onPrimaryDarkHighContrast
import com.example.myslog.ui.theme.onPrimaryDarkMediumContrast
import com.example.myslog.ui.theme.onPrimaryLight
import com.example.myslog.ui.theme.onPrimaryLightHighContrast
import com.example.myslog.ui.theme.onPrimaryLightMediumContrast
import com.example.myslog.ui.theme.onSecondaryContainerDark
import com.example.myslog.ui.theme.onSecondaryContainerDarkHighContrast
import com.example.myslog.ui.theme.onSecondaryContainerDarkMediumContrast
import com.example.myslog.ui.theme.onSecondaryContainerLight
import com.example.myslog.ui.theme.onSecondaryContainerLightHighContrast
import com.example.myslog.ui.theme.onSecondaryContainerLightMediumContrast
import com.example.myslog.ui.theme.onSecondaryDark
import com.example.myslog.ui.theme.onSecondaryDarkHighContrast
import com.example.myslog.ui.theme.onSecondaryDarkMediumContrast
import com.example.myslog.ui.theme.onSecondaryLight
import com.example.myslog.ui.theme.onSecondaryLightHighContrast
import com.example.myslog.ui.theme.onSecondaryLightMediumContrast
import com.example.myslog.ui.theme.onSurfaceDark
import com.example.myslog.ui.theme.onSurfaceDarkHighContrast
import com.example.myslog.ui.theme.onSurfaceDarkMediumContrast
import com.example.myslog.ui.theme.onSurfaceLight
import com.example.myslog.ui.theme.onSurfaceLightHighContrast
import com.example.myslog.ui.theme.onSurfaceLightMediumContrast
import com.example.myslog.ui.theme.onSurfaceVariantDark
import com.example.myslog.ui.theme.onSurfaceVariantDarkHighContrast
import com.example.myslog.ui.theme.onSurfaceVariantDarkMediumContrast
import com.example.myslog.ui.theme.onSurfaceVariantLight
import com.example.myslog.ui.theme.onSurfaceVariantLightHighContrast
import com.example.myslog.ui.theme.onSurfaceVariantLightMediumContrast
import com.example.myslog.ui.theme.onTertiaryContainerDark
import com.example.myslog.ui.theme.onTertiaryContainerDarkHighContrast
import com.example.myslog.ui.theme.onTertiaryContainerDarkMediumContrast
import com.example.myslog.ui.theme.onTertiaryContainerLight
import com.example.myslog.ui.theme.onTertiaryContainerLightHighContrast
import com.example.myslog.ui.theme.onTertiaryContainerLightMediumContrast
import com.example.myslog.ui.theme.onTertiaryDark
import com.example.myslog.ui.theme.onTertiaryDarkHighContrast
import com.example.myslog.ui.theme.onTertiaryDarkMediumContrast
import com.example.myslog.ui.theme.onTertiaryLight
import com.example.myslog.ui.theme.onTertiaryLightHighContrast
import com.example.myslog.ui.theme.onTertiaryLightMediumContrast
import com.example.myslog.ui.theme.outlineDark
import com.example.myslog.ui.theme.outlineDarkHighContrast
import com.example.myslog.ui.theme.outlineDarkMediumContrast
import com.example.myslog.ui.theme.outlineLight
import com.example.myslog.ui.theme.outlineLightHighContrast
import com.example.myslog.ui.theme.outlineLightMediumContrast
import com.example.myslog.ui.theme.outlineVariantDark
import com.example.myslog.ui.theme.outlineVariantDarkHighContrast
import com.example.myslog.ui.theme.outlineVariantDarkMediumContrast
import com.example.myslog.ui.theme.outlineVariantLight
import com.example.myslog.ui.theme.outlineVariantLightHighContrast
import com.example.myslog.ui.theme.outlineVariantLightMediumContrast
import com.example.myslog.ui.theme.primaryContainerDark
import com.example.myslog.ui.theme.primaryContainerDarkHighContrast
import com.example.myslog.ui.theme.primaryContainerDarkMediumContrast
import com.example.myslog.ui.theme.primaryContainerLight
import com.example.myslog.ui.theme.primaryContainerLightHighContrast
import com.example.myslog.ui.theme.primaryContainerLightMediumContrast
import com.example.myslog.ui.theme.primaryDark
import com.example.myslog.ui.theme.primaryDarkHighContrast
import com.example.myslog.ui.theme.primaryDarkMediumContrast
import com.example.myslog.ui.theme.primaryLight
import com.example.myslog.ui.theme.primaryLightHighContrast
import com.example.myslog.ui.theme.primaryLightMediumContrast
import com.example.myslog.ui.theme.scrimDark
import com.example.myslog.ui.theme.scrimDarkHighContrast
import com.example.myslog.ui.theme.scrimDarkMediumContrast
import com.example.myslog.ui.theme.scrimLight
import com.example.myslog.ui.theme.scrimLightHighContrast
import com.example.myslog.ui.theme.scrimLightMediumContrast
import com.example.myslog.ui.theme.secondaryContainerDark
import com.example.myslog.ui.theme.secondaryContainerDarkHighContrast
import com.example.myslog.ui.theme.secondaryContainerDarkMediumContrast
import com.example.myslog.ui.theme.secondaryContainerLight
import com.example.myslog.ui.theme.secondaryContainerLightHighContrast
import com.example.myslog.ui.theme.secondaryContainerLightMediumContrast
import com.example.myslog.ui.theme.secondaryDark
import com.example.myslog.ui.theme.secondaryDarkHighContrast
import com.example.myslog.ui.theme.secondaryDarkMediumContrast
import com.example.myslog.ui.theme.secondaryLight
import com.example.myslog.ui.theme.secondaryLightHighContrast
import com.example.myslog.ui.theme.secondaryLightMediumContrast
import com.example.myslog.ui.theme.surfaceBrightDark
import com.example.myslog.ui.theme.surfaceBrightDarkHighContrast
import com.example.myslog.ui.theme.surfaceBrightDarkMediumContrast
import com.example.myslog.ui.theme.surfaceBrightLight
import com.example.myslog.ui.theme.surfaceBrightLightHighContrast
import com.example.myslog.ui.theme.surfaceBrightLightMediumContrast
import com.example.myslog.ui.theme.surfaceContainerDark
import com.example.myslog.ui.theme.surfaceContainerDarkHighContrast
import com.example.myslog.ui.theme.surfaceContainerDarkMediumContrast
import com.example.myslog.ui.theme.surfaceContainerHighDark
import com.example.myslog.ui.theme.surfaceContainerHighDarkHighContrast
import com.example.myslog.ui.theme.surfaceContainerHighDarkMediumContrast
import com.example.myslog.ui.theme.surfaceContainerHighLight
import com.example.myslog.ui.theme.surfaceContainerHighLightHighContrast
import com.example.myslog.ui.theme.surfaceContainerHighLightMediumContrast
import com.example.myslog.ui.theme.surfaceContainerHighestDark
import com.example.myslog.ui.theme.surfaceContainerHighestDarkHighContrast
import com.example.myslog.ui.theme.surfaceContainerHighestDarkMediumContrast
import com.example.myslog.ui.theme.surfaceContainerHighestLight
import com.example.myslog.ui.theme.surfaceContainerHighestLightHighContrast
import com.example.myslog.ui.theme.surfaceContainerHighestLightMediumContrast
import com.example.myslog.ui.theme.surfaceContainerLight
import com.example.myslog.ui.theme.surfaceContainerLightHighContrast
import com.example.myslog.ui.theme.surfaceContainerLightMediumContrast
import com.example.myslog.ui.theme.surfaceContainerLowDark
import com.example.myslog.ui.theme.surfaceContainerLowDarkHighContrast
import com.example.myslog.ui.theme.surfaceContainerLowDarkMediumContrast
import com.example.myslog.ui.theme.surfaceContainerLowLight
import com.example.myslog.ui.theme.surfaceContainerLowLightHighContrast
import com.example.myslog.ui.theme.surfaceContainerLowLightMediumContrast
import com.example.myslog.ui.theme.surfaceContainerLowestDark
import com.example.myslog.ui.theme.surfaceContainerLowestDarkHighContrast
import com.example.myslog.ui.theme.surfaceContainerLowestDarkMediumContrast
import com.example.myslog.ui.theme.surfaceContainerLowestLight
import com.example.myslog.ui.theme.surfaceContainerLowestLightHighContrast
import com.example.myslog.ui.theme.surfaceContainerLowestLightMediumContrast
import com.example.myslog.ui.theme.surfaceDark
import com.example.myslog.ui.theme.surfaceDarkHighContrast
import com.example.myslog.ui.theme.surfaceDarkMediumContrast
import com.example.myslog.ui.theme.surfaceDimDark
import com.example.myslog.ui.theme.surfaceDimDarkHighContrast
import com.example.myslog.ui.theme.surfaceDimDarkMediumContrast
import com.example.myslog.ui.theme.surfaceDimLight
import com.example.myslog.ui.theme.surfaceDimLightHighContrast
import com.example.myslog.ui.theme.surfaceDimLightMediumContrast
import com.example.myslog.ui.theme.surfaceLight
import com.example.myslog.ui.theme.surfaceLightHighContrast
import com.example.myslog.ui.theme.surfaceLightMediumContrast
import com.example.myslog.ui.theme.surfaceVariantDark
import com.example.myslog.ui.theme.surfaceVariantDarkHighContrast
import com.example.myslog.ui.theme.surfaceVariantDarkMediumContrast
import com.example.myslog.ui.theme.surfaceVariantLight
import com.example.myslog.ui.theme.surfaceVariantLightHighContrast
import com.example.myslog.ui.theme.surfaceVariantLightMediumContrast
import com.example.myslog.ui.theme.tertiaryContainerDark
import com.example.myslog.ui.theme.tertiaryContainerDarkHighContrast
import com.example.myslog.ui.theme.tertiaryContainerDarkMediumContrast
import com.example.myslog.ui.theme.tertiaryContainerLight
import com.example.myslog.ui.theme.tertiaryContainerLightHighContrast
import com.example.myslog.ui.theme.tertiaryContainerLightMediumContrast
import com.example.myslog.ui.theme.tertiaryDark
import com.example.myslog.ui.theme.tertiaryDarkHighContrast
import com.example.myslog.ui.theme.tertiaryDarkMediumContrast
import com.example.myslog.ui.theme.tertiaryLight
import com.example.myslog.ui.theme.tertiaryLightHighContrast
import com.example.myslog.ui.theme.tertiaryLightMediumContrast

//TODO : añadir más temas para el usuario
private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkScheme // Asegúrate de que darkScheme esté definido en tu archivo
        else -> lightScheme // Asegúrate de que lightScheme esté definido en tu archivo
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
