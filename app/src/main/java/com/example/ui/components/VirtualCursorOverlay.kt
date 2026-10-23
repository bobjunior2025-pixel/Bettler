package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.RocketOrange
import com.example.viewmodel.VirtualCursorState
import kotlin.math.roundToInt

@Composable
fun VirtualCursorOverlay(
    cursorState: VirtualCursorState,
    modifier: Modifier = Modifier
) {
    if (!cursorState.isEnabled) return

    val density = LocalDensity.current
    val clickScale by animateFloatAsState(
        targetValue = if (cursorState.isClicking) 0.8f else 1.0f,
        label = "cursorClickScale"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Cursor body positioned at (cursorState.x, cursorState.y)
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = cursorState.x.roundToInt(),
                        y = cursorState.y.roundToInt()
                    )
                }
                .size(36.dp)
                .scale(clickScale)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width * 0.75f, size.height * 0.65f)
                    lineTo(size.width * 0.42f, size.height * 0.65f)
                    lineTo(size.width * 0.60f, size.height * 0.98f)
                    lineTo(size.width * 0.45f, size.height * 1.0f)
                    lineTo(size.width * 0.28f, size.height * 0.68f)
                    lineTo(0f, size.height * 0.85f)
                    close()
                }

                // Outer neon glow shadow
                drawPath(
                    path = path,
                    brush = Brush.radialGradient(
                        colors = listOf(NeonCyan, Color.Transparent),
                        center = Offset(size.width * 0.3f, size.height * 0.3f),
                        radius = size.width
                    )
                )

                // Fill cursor
                drawPath(
                    path = path,
                    color = NeonCyan
                )

                // Border contour
                drawPath(
                    path = path,
                    color = DarkBackground,
                    style = Stroke(width = 2.5f)
                )

                // Pulsing central dot for precision
                drawCircle(
                    color = if (cursorState.isClicking) RocketOrange else ElectricViolet,
                    radius = 4f,
                    center = Offset(2f, 2f)
                )
            }
        }
    }
}
