package com.eatfair.partner.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BrandOrange,
    onPrimary = Color.White,
    primaryContainer = BrandOrangeDark,
    onPrimaryContainer = Color.White,
    
    secondary = TealAccent,
    onSecondary = Color.White,
    secondaryContainer = DeepBlue,
    onSecondaryContainer = Color.White,
    
    tertiary = BrandRed,
    onTertiary = Color.White,
    
    background = NeutralGray50,
    onBackground = NeutralGray900,
    
    surface = Color.White,
    onSurface = NeutralGray900,
    surfaceVariant = NeutralGray100,
    onSurfaceVariant = NeutralGray700,
    
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRedLight,
    onErrorContainer = ErrorRed,
    
    outline = NeutralGray300,
    outlineVariant = NeutralGray200,
)

@Composable
fun PartnerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}