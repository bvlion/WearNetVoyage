package net.ambitious.android.wearnetvoyage.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme
import net.ambitious.android.wearnetvoyage.ui.theme.darkBackground
import net.ambitious.android.wearnetvoyage.ui.theme.darkError
import net.ambitious.android.wearnetvoyage.ui.theme.darkOnBackground
import net.ambitious.android.wearnetvoyage.ui.theme.darkOnError
import net.ambitious.android.wearnetvoyage.ui.theme.darkOnPrimary
import net.ambitious.android.wearnetvoyage.ui.theme.darkOnSecondary
import net.ambitious.android.wearnetvoyage.ui.theme.darkOnSurface
import net.ambitious.android.wearnetvoyage.ui.theme.darkSurface
import net.ambitious.android.wearnetvoyage.ui.theme.primary
import net.ambitious.android.wearnetvoyage.ui.theme.primaryVariant
import net.ambitious.android.wearnetvoyage.ui.theme.secondary

internal val wearColorPalette: Colors = Colors(
  primary = primary,
  primaryVariant = primaryVariant,
  secondary = secondary,
  background = darkBackground,
  surface = darkSurface,
  error = darkError,
  onPrimary = darkOnPrimary,
  onSecondary = darkOnSecondary,
  onBackground = darkOnBackground,
  onSurface = darkOnSurface,
  onError = darkOnError
)

@Composable
fun WearNetVoyageTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colors = wearColorPalette,
    typography = Typography,
    content = content
  )
}

object LinkTileTheme {
  private val darkBlue = Color(0xFF202124)
  val colors = androidx.wear.tiles.material.Colors(
    wearColorPalette.primary.toArgb(),
    wearColorPalette.onPrimary.toArgb(),
    darkBlue.toArgb(),
    wearColorPalette.onSurface.toArgb(),
  )
}
