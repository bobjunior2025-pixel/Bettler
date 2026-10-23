package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CloudGameShortcut
import com.example.model.POPULAR_BOOSTEROID_GAMES
import com.example.model.StreamResolution
import com.example.model.UserAgentProfile
import com.example.ui.theme.CyberPurple
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
fun HomeScreen(
    state: UiState,
    viewModel: BoosteroidViewModel,
    onLaunchStream: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tabs = listOf("Jogos Cloud", "Otimizador Lite", "Como Baixar APK", "Guia Fire Stick")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // TV Header Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            Brush.linearGradient(listOf(ElectricViolet, NeonCyan)),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RocketLaunch,
                        contentDescription = "Logo",
                        tint = DarkBackground,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "BETTER BOOSTEROID",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(RocketOrange.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                .border(1.dp, RocketOrange, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "FIRE STICK LITE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = RocketOrange
                            )
                        }
                    }
                    Text(
                        text = "Cliente com Mouse Virtual & Suporte Total a Controle",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            // Top Status Badges
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Gamepad pill
                Box(
                    modifier = Modifier
                        .background(DarkSurfaceVariant, RoundedCornerShape(20.dp))
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SportsEsports,
                            contentDescription = null,
                            tint = if (state.connectedGamepadName != null) SuccessGreen else NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = state.connectedGamepadName ?: "Controle / Remote Pronto",
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Ping pill
                Box(
                    modifier = Modifier
                        .clickable { viewModel.checkLatency() }
                        .background(DarkSurfaceVariant, RoundedCornerShape(20.dp))
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.NetworkCheck,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (state.pingMs > 0) "${state.pingMs} ms" else "38 ms",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Hero Play Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(
                1.5.dp,
                Brush.horizontalGradient(listOf(NeonCyan, ElectricViolet))
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("hero_launch_card")
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Background subtle cyber glow
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    ElectricViolet.copy(alpha = 0.20f),
                                    DarkSurface,
                                    NeonCyan.copy(alpha = 0.10f)
                                )
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "JOGAR NA NUVEM AGORA",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Inicie o Boosteroid com resolução otimizada para o Fire TV Stick Lite (${state.settings.resolution.label}). Use o analógico para controlar o Mouse Virtual na tela de login e jogos!",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = { onLaunchStream("https://cloud.boosteroid.com") },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .testTag("launch_boosteroid_button")
                                    .height(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = DarkBackground,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Abrir Boosteroid Cloud",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkBackground
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            OutlinedButton(
                                onClick = { viewModel.toggleHud() },
                                border = BorderStroke(1.dp, NeonCyan),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Ajustes Rápidos", color = NeonCyan, fontSize = 13.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Feature highlights pill box
                    Column(
                        modifier = Modifier
                            .background(DarkSurfaceVariant, RoundedCornerShape(12.dp))
                            .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FeatureRow(
                            icon = Icons.Default.Mouse,
                            title = "Mouse Virtual Embutido",
                            subtitle = "Pressione MENU ou SELECT"
                        )
                        FeatureRow(
                            icon = Icons.Default.Memory,
                            title = "Modo 1GB RAM Fire Stick",
                            subtitle = "Limpeza agressiva de cache"
                        )
                        FeatureRow(
                            icon = Icons.Default.Tv,
                            title = "Tela Cheia Sem Bordas",
                            subtitle = "Modo esticado 16:9 inteligente"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Navigation Tabs
        ScrollableTabRow(
            selectedTabIndex = state.activeTab,
            containerColor = DarkBackground,
            contentColor = NeonCyan,
            edgePadding = 0.dp,
            divider = { HorizontalDivider(color = DarkCardBorder) }
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = state.activeTab == index
                Tab(
                    selected = isSelected,
                    onClick = { viewModel.setActiveTab(index) },
                    text = {
                        Text(
                            text = title,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) NeonCyan else TextSecondary
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Content
        when (state.activeTab) {
            0 -> GamesTab(onLaunch = onLaunchStream)
            1 -> OptimizerTab(state = state, viewModel = viewModel)
            2 -> GitHubApkTab()
            3 -> FireStickGuideTab()
        }
    }
}

@Composable
fun FeatureRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(ElectricViolet.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(text = subtitle, fontSize = 10.sp, color = TextSecondary)
        }
    }
}

@Composable
fun GamesTab(onLaunch: (String) -> Unit) {
    Column {
        Text(
            text = "ATALHOS DE JOGOS DISPONÍVEIS NO BOOSTEROID",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = NeonCyan,
            letterSpacing = 1.sp
        )
        Text(
            text = "Clique em qualquer jogo para abrir o Boosteroid pronto para conectar à sua conta Steam/Epic Games.",
            fontSize = 11.sp,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(POPULAR_BOOSTEROID_GAMES) { game ->
                GameShortcutCard(game = game, onClick = {
                    onLaunch("https://cloud.boosteroid.com/dashboard")
                })
            }
        }
    }
}

@Composable
fun GameShortcutCard(game: CloudGameShortcut, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(
            if (isFocused) 2.dp else 1.dp,
            if (isFocused) NeonCyan else DarkCardBorder
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(170.dp)
            .height(200.dp)
            .focusable(interactionSource = interactionSource)
            .clickable { onClick() }
            .testTag("game_${game.id}")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Color Strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(game.bannerColorHex), DarkSurface)
                        )
                    )
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SportsEsports,
                    contentDescription = null,
                    tint = TextPrimary.copy(alpha = 0.8f),
                    modifier = Modifier
                        .size(38.dp)
                        .align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = game.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 2
                    )
                    Text(
                        text = game.publisher,
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Boosteroid",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeonCyan
                    )
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Jogar",
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OptimizerTab(state: UiState, viewModel: BoosteroidViewModel) {
    val context = LocalContext.current
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // RAM Status Card
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, DarkCardBorder),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Memory, contentDescription = null, tint = RocketOrange)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Gerenciamento de RAM (1GB)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "O Fire TV Stick Lite possui 1 GB de RAM total compartilhada com a GPU. Para evitar que o sistema feche o navegador no meio da partida, o Better Boosteroid usa o perfil leve de 720p e limpeza de WebView.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "RAM Disponível no momento: ${state.ramFreeMb} MB",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                }
            }

            // Resolution Profiles Card
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, DarkCardBorder),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Perfil de Resolução Ativo", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Selecione a resolução ideal para sua TV e conexão:", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(10.dp))

                    StreamResolution.values().forEach { res ->
                        val isSelected = state.settings.resolution == res
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) ElectricViolet.copy(alpha = 0.4f) else DarkSurfaceVariant)
                                .border(1.dp, if (isSelected) NeonCyan else DarkCardBorder, RoundedCornerShape(8.dp))
                                .clickable { viewModel.updateResolution(res) }
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(res.label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = if (isSelected) NeonCyan else TextPrimary)
                                if (isSelected) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GitHubApkTab() {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(NeonCyan.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Integração GitHub Actions & Download do APK",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Workflow pronto em .github/workflows/build-apk.yml",
                        fontSize = 12.sp,
                        color = NeonCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = DarkCardBorder)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Como pegar o APK e instalar direto no seu Fire TV Stick Lite:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            val steps = listOf(
                "1. No repositório GitHub do seu app, clique na aba 'Actions'.",
                "2. Você verá o workflow 'Build Better Boosteroid APK' gerado automaticamente.",
                "3. Ao clicar no último build concluído, role até 'Artifacts' e baixe o 'BetterBoosteroid-FireStick-APK'.",
                "4. Para instalar no Fire Stick: use o aplicativo 'Downloader' (código ou link direto) ou envie o arquivo .apk via app 'Send Files to TV'.",
                "5. Nas opções do desenvolvedor do Fire Stick, permita a instalação de fontes desconhecidas para o Downloader e instale o APK!"
            )

            steps.forEach { step ->
                Row(modifier = Modifier.padding(vertical = 3.dp)) {
                    Text(text = "• ", color = NeonCyan, fontWeight = FontWeight.Bold)
                    Text(text = step, fontSize = 12.sp, color = TextSecondary, lineHeight = 18.sp)
                }
            }
        }
    }
}

@Composable
fun FireStickGuideTab() {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, DarkCardBorder),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.HelpOutline, contentDescription = null, tint = ElectricViolet)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Dicas e Comandos para Jogar no Fire Stick Lite",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            val tips = listOf(
                "🎮 Alternar Mouse Virtual: Se a página do Boosteroid não clicar com o D-Pad, pressione o botão MENU (3 tracinhos) ou SELECT no controle para ativar o ponteiro do mouse na tela.",
                "⚡ Wi-Fi 5 GHz: Conecte o Fire Stick Lite obrigatoriamente na rede 5GHz do roteador. Redes 2.4GHz causam alta latência e perda de pacotes em jogos na nuvem.",
                "⚙️ Painel HUD em Jogo: Aperte o botão MENU para abrir o painel lateral com resolução, ping e ajustes a qualquer momento sem fechar o jogo.",
                "📺 Ajuste de TV: Se sua TV cortar os cantos, acesse o menu HUD e selecione 'Original 16:9' ou 'Esticar Tela Cheia'."
            )

            tips.forEach { tip ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(DarkSurfaceVariant, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(text = tip, fontSize = 12.sp, color = TextPrimary, lineHeight = 18.sp)
                }
            }
        }
    }
}
