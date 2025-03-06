package br.com.doafacil.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Green600,
    onPrimary = Gray50,
    primaryContainer = Green100,
    onPrimaryContainer = Green900,
    
    secondary = Blue600,
    onSecondary = Gray50,
    secondaryContainer = Blue100,
    onSecondaryContainer = Blue900,
    
    background = Gray50,
    onBackground = Gray900,
    surface = Gray50,
    onSurface = Gray900,
    
    error = Error,
    onError = Gray50,
    errorContainer = Error.copy(alpha = 0.1f),
    onErrorContainer = Error
)

private val DarkColorScheme = darkColorScheme(
    primary = Green400,
    onPrimary = Gray900,
    primaryContainer = Green800,
    onPrimaryContainer = Green100,
    
    secondary = Blue400,
    onSecondary = Gray900,
    secondaryContainer = Blue800,
    onSecondaryContainer = Blue100,
    
    background = Gray900,
    onBackground = Gray100,
    surface = Gray900,
    onSurface = Gray100,
    
    error = Error,
    onError = Gray900,
    errorContainer = Error.copy(alpha = 0.2f),
    onErrorContainer = Error
)

@Composable
fun DoaFacilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Abordagem moderna para definir as cores da barra de status
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
            }
            
            // Definir a cor da barra de status para corresponder à cor primária
            window.statusBarColor = colorScheme.primary.toArgb()
        }
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 