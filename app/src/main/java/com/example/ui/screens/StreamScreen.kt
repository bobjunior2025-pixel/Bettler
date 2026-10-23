package com.example.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.components.HudDrawer
import com.example.ui.components.VirtualCursorOverlay
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.RocketOrange
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.viewmodel.BoosteroidViewModel
import com.example.viewmodel.UiState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun StreamScreen(
    state: UiState,
    viewModel: BoosteroidViewModel,
    onExitGame: () -> Unit,
    onWebViewReady: (WebView) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var showQuickFloatingBar by remember { mutableStateOf(true) }

    // Reapply CSS aspect / zoom when aspectMode changes
    LaunchedEffect(state.settings.aspectMode, state.reloadTrigger) {
        webViewRef?.let { wv ->
            if (state.reloadTrigger > 0) {
                wv.reload()
            }
            val aspectCss = state.settings.aspectMode.scaleCss
            val js = """
                (function() {
                    let customStyle = document.getElementById('better-boosteroid-style');
                    if (!customStyle) {
                        customStyle = document.createElement('style');
                        customStyle.id = 'better-boosteroid-style';
                        document.head.appendChild(customStyle);
                    }
                    customStyle.textContent = `
                        body, html {
                            margin: 0 !important;
                            padding: 0 !important;
                            background: #000000 !important;
                            overflow: hidden !important;
                        }
                        video {
                            $aspectCss
                        }
                    `;
                })();
            """.trimIndent()
            wv.evaluateJavascript(js, null)
        }
    }

    Box(modifier = modifier.fillMaxSize().background(Color.Black)) {
        // Main Cloud Gaming WebView
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    isFocusable = true
                    isFocusableInTouchMode = true
                    setLayerType(View.LAYER_TYPE_HARDWARE, null)

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        allowContentAccess = true
                        allowFileAccess = true
                        userAgentString = state.settings.userAgent.userAgentString
                        cacheMode = if (state.settings.fireStickLiteMode) {
                            WebSettings.LOAD_DEFAULT
                        } else {
                            WebSettings.LOAD_DEFAULT
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onPermissionRequest(request: PermissionRequest?) {
                            request?.grant(request.resources)
                        }
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            isLoading = true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            isLoading = false
                            // Inject TV-friendly layout optimizations
                            val aspectCss = state.settings.aspectMode.scaleCss
                            val injectScript = """
                                (function() {
                                    const style = document.createElement('style');
                                    style.id = 'better-boosteroid-style';
                                    style.textContent = `
                                        body, html {
                                            margin: 0 !important;
                                            padding: 0 !important;
                                            background: #000000 !important;
                                            overflow: hidden !important;
                                        }
                                        video {
                                            $aspectCss
                                        }
                                    `;
                                    document.head.appendChild(style);
                                })();
                            """.trimIndent()
                            view?.evaluateJavascript(injectScript, null)
                        }

                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                            return false // Keep everything inside the optimized player
                        }
                    }

                    loadUrl(state.streamUrl)
                    webViewRef = this
                    onWebViewReady(this)
                }
            },
            update = { wv ->
                // Update User-Agent if changed
                if (wv.settings.userAgentString != state.settings.userAgent.userAgentString) {
                    wv.settings.userAgentString = state.settings.userAgent.userAgentString
                    wv.reload()
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Loading spinner overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBackground.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .background(DarkBackground, RoundedCornerShape(12.dp))
                        .border(1.dp, NeonCyan, RoundedCornerShape(12.dp))
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        color = NeonCyan,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Conectando ao Boosteroid Cloud Gaming...",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Quick On-Screen Floating Pill for Fire Stick Remote (top right)
        AnimatedVisibility(
            visible = showQuickFloatingBar && !state.isHudOpen,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.70f), RoundedCornerShape(20.dp))
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(20.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Virtual Mouse Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { viewModel.toggleVirtualCursor() }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Mouse,
                        contentDescription = "Mouse",
                        tint = if (state.cursorState.isEnabled) SuccessGreen else TextPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (state.cursorState.isEnabled) "Mouse ON" else "Mouse OFF",
                        fontSize = 11.sp,
                        color = if (state.cursorState.isEnabled) SuccessGreen else TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // HUD Button
                IconButton(
                    onClick = { viewModel.toggleHud() },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "HUD",
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Exit Stream Button
                IconButton(
                    onClick = onExitGame,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Sair",
                        tint = RocketOrange,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Virtual Mouse Pointer Overlay
        VirtualCursorOverlay(cursorState = state.cursorState)

        // Slide-out HUD Drawer
        HudDrawer(
            state = state,
            viewModel = viewModel,
            onClose = { viewModel.closeHud() },
            onExitGame = onExitGame
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            webViewRef?.destroy()
        }
    }
}
