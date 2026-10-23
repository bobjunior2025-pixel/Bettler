package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AspectMode
import com.example.model.StreamResolution
import com.example.model.UserAgentProfile
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.RocketOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.BoosteroidViewModel
import com.example.viewmodel.UiState

@Composable
fun HudDrawer(
    state: UiState,
    viewModel: BoosteroidViewModel,
    onClose: () -> Unit,
    onExitGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = state.isHudOpen,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it }),
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
                .clickable { onClose() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(min = 340.dp, max = 440.dp)
                    .align(Alignment.CenterEnd)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(DarkSurface, DarkBackground)
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(NeonCyan.copy(alpha = 0.6f), ElectricViolet.copy(alpha = 0.3f))
                        ),
                        shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                    )
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .clickable(enabled = false) {}
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(ElectricViolet.copy(alpha = 0.2f), CircleShape)
                                    .border(1.dp, NeonCyan, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SportsEsports,
                                    contentDescription = "HUD",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Better Boosteroid",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Painel Rápido Fire Stick",
                                    fontSize = 12.sp,
                                    color = NeonCyan
                                )
                            }
                        }

                        IconButton(
                            onClick = onClose,
                            modifier = Modifier.testTag("close_hud_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fechar HUD",
                                tint = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = DarkCardBorder)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Action: Mouse Virtual Toggle
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (state.cursorState.isEnabled) NeonCyan else DarkCardBorder
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleVirtualCursor() }
                            .testTag("hud_toggle_virtual_mouse")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Mouse,
                                    contentDescription = null,
                                    tint = if (state.cursorState.isEnabled) NeonCyan else TextSecondary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Mouse Virtual (Controle)",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = if (state.cursorState.isEnabled) "ATIVADO: Use analógico / D-Pad" else "DESATIVADO: Modo Gamepad Nativo",
                                        fontSize = 11.sp,
                                        color = if (state.cursorState.isEnabled) SuccessGreen else TextSecondary
                                    )
                                }
                            }
                            Switch(
                                checked = state.cursorState.isEnabled,
                                onCheckedChange = { viewModel.setVirtualCursor(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = NeonCyan,
                                    checkedTrackColor = ElectricViolet
                                )
                            )
                        }
                    }

                    if (state.cursorState.isEnabled) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Velocidade do Mouse: ${String.format("%.1fx", state.settings.cursorSpeed)}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Slider(
                            value = state.settings.cursorSpeed,
                            onValueChange = { viewModel.updateCursorSpeed(it) },
                            valueRange = 0.8f..3.0f,
                            steps = 4,
                            colors = SliderDefaults.colors(
                                thumbColor = NeonCyan,
                                activeTrackColor = ElectricViolet
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Resolution Tweaks
                    Text(
                        text = "RESOLUÇÃO DA NUVEM",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    StreamResolution.values().forEach { res ->
                        val isSelected = state.settings.resolution == res
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) ElectricViolet.copy(alpha = 0.35f) else DarkSurfaceVariant)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) NeonCyan else DarkCardBorder,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.updateResolution(res) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = res.label,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) NeonCyan else TextPrimary
                                    )
                                    if (res == StreamResolution.RES_720P) {
                                        Text(
                                            text = "★ LITE",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RocketOrange
                                        )
                                    }
                                }
                                Text(
                                    text = res.description,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Aspect Ratio / Zoom
                    Text(
                        text = "ENQUADRAMENTO DE TELA (TV)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    AspectMode.values().forEach { mode ->
                        val isSelected = state.settings.aspectMode == mode
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) ElectricViolet.copy(alpha = 0.35f) else DarkSurfaceVariant)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) NeonCyan else DarkCardBorder,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.updateAspectMode(mode) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = mode.label,
                                fontSize = 12.sp,
                                color = if (isSelected) NeonCyan else TextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Fire Stick Lite Optimization
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Memory,
                                        contentDescription = null,
                                        tint = RocketOrange,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Modo Fire Stick Lite (1GB RAM)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                }
                                Switch(
                                    checked = state.settings.fireStickLiteMode,
                                    onCheckedChange = { viewModel.toggleFireStickLiteMode() },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = RocketOrange,
                                        checkedTrackColor = ElectricViolet
                                    )
                                )
                            }
                            Text(
                                text = "RAM livre: ~${state.ramFreeMb} MB / ${state.ramTotalMb} MB",
                                fontSize = 11.sp,
                                color = if (state.ramFreeMb < 200) RocketOrange else SuccessGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Server Ping Live Test
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.NetworkCheck,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Latência Cloud:",
                                fontSize = 13.sp,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (state.pingMs > 0) "${state.pingMs} ms" else "Calculando...",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (state.pingMs in 1..45) SuccessGreen else if (state.pingMs in 46..85) RocketOrange else NeonCyan
                            )
                        }
                        IconButton(
                            onClick = { viewModel.checkLatency() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Testar Ping",
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.triggerReload()
                                onClose()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Recarregar", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                onExitGame()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RocketOrange),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sair do Jogo", fontSize = 12.sp, color = DarkBackground, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
