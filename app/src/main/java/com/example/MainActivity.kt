package com.example

import android.content.Context
import android.content.res.Configuration
import android.hardware.input.InputManager
import android.os.Bundle
import android.os.SystemClock
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.view.WindowCompat
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.StreamScreen
import com.example.ui.theme.BetterBoosteroidTheme
import com.example.viewmodel.BoosteroidViewModel
import kotlin.math.abs

class MainActivity : ComponentActivity(), InputManager.InputDeviceListener {

    private val viewModel: BoosteroidViewModel by viewModels()
    private var activeWebView: WebView? = null
    private var isSimulatedTouchDown = false
    private var downTimeMs = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep TV screen awake during cloud gaming
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Enable immersive fullscreen for TV
        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemUI()

        // Register gamepad listeners
        val inputManager = getSystemService(Context.INPUT_SERVICE) as? InputManager
        inputManager?.registerInputDeviceListener(this, null)
        detectConnectedGamepads()

        // Update memory stats for Fire Stick Lite
        viewModel.updateMemoryStats(this)

        setContent {
            BetterBoosteroidTheme {
                val state by viewModel.uiState.collectAsState()

                BackHandler(enabled = true) {
                    if (state.isHudOpen) {
                        viewModel.closeHud()
                    } else if (state.cursorState.isEnabled) {
                        viewModel.setVirtualCursor(false)
                    } else if (state.isStreaming) {
                        viewModel.exitStream()
                    } else {
                        finish()
                    }
                }

                if (state.isStreaming) {
                    StreamScreen(
                        state = state,
                        viewModel = viewModel,
                        onExitGame = { viewModel.exitStream() },
                        onWebViewReady = { webView -> activeWebView = webView },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    HomeScreen(
                        state = state,
                        viewModel = viewModel,
                        onLaunchStream = { url -> viewModel.openStream(url) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    private fun hideSystemUI() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        detectConnectedGamepads()
        viewModel.updateMemoryStats(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        val inputManager = getSystemService(Context.INPUT_SERVICE) as? InputManager
        inputManager?.unregisterInputDeviceListener(this)
        activeWebView = null
    }

    // ==========================================
    // Gamepad & Fire TV Remote Input Handling
    // ==========================================

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val state = viewModel.uiState.value

        // Quick button shortcuts:
        // Menu button (Fire TV remote or Xbox / PS controller Menu / Start)
        if (event.keyCode == KeyEvent.KEYCODE_MENU && event.action == KeyEvent.ACTION_UP) {
            viewModel.toggleHud()
            return true
        }

        // Thumbstick click (L3/R3) or Select / Back long-press toggles Virtual Mouse
        if ((event.keyCode == KeyEvent.KEYCODE_BUTTON_THUMBL || event.keyCode == KeyEvent.KEYCODE_BUTTON_THUMBR) &&
            event.action == KeyEvent.ACTION_UP
        ) {
            viewModel.toggleVirtualCursor()
            return true
        }

        // Handle Virtual Mouse clicking when enabled
        if (state.isStreaming && state.cursorState.isEnabled) {
            val isClickKey = event.keyCode == KeyEvent.KEYCODE_BUTTON_A ||
                event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER ||
                event.keyCode == KeyEvent.KEYCODE_ENTER

            if (isClickKey) {
                val cx = state.cursorState.x
                val cy = state.cursorState.y
                val wv = activeWebView

                if (event.action == KeyEvent.ACTION_DOWN) {
                    if (!isSimulatedTouchDown) {
                        isSimulatedTouchDown = true
                        downTimeMs = SystemClock.uptimeMillis()
                        val motionEvent = MotionEvent.obtain(
                            downTimeMs,
                            downTimeMs,
                            MotionEvent.ACTION_DOWN,
                            cx,
                            cy,
                            0
                        )
                        wv?.dispatchTouchEvent(motionEvent)
                        motionEvent.recycle()
                        viewModel.triggerCursorClickAnimation()
                    }
                    return true
                } else if (event.action == KeyEvent.ACTION_UP) {
                    if (isSimulatedTouchDown) {
                        isSimulatedTouchDown = false
                        val upTimeMs = SystemClock.uptimeMillis()
                        val motionEvent = MotionEvent.obtain(
                            downTimeMs,
                            upTimeMs,
                            MotionEvent.ACTION_UP,
                            cx,
                            cy,
                            0
                        )
                        wv?.dispatchTouchEvent(motionEvent)
                        motionEvent.recycle()
                    }
                    return true
                }
            }

            // D-Pad movement for virtual mouse when joystick is not available (e.g. Fire TV basic remote)
            if (event.action == KeyEvent.ACTION_DOWN) {
                val dm = resources.displayMetrics
                val w = dm.widthPixels.toFloat()
                val h = dm.heightPixels.toFloat()

                when (event.keyCode) {
                    KeyEvent.KEYCODE_DPAD_LEFT -> {
                        viewModel.moveCursor(-2.5f, 0f, w, h)
                        return true
                    }
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        viewModel.moveCursor(2.5f, 0f, w, h)
                        return true
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        viewModel.moveCursor(0f, -2.5f, w, h)
                        return true
                    }
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        viewModel.moveCursor(0f, 2.5f, w, h)
                        return true
                    }
                }
            }
        }

        return super.dispatchKeyEvent(event)
    }

    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
        val state = viewModel.uiState.value

        // If in Virtual Mouse mode, read analog sticks to move pointer and scroll
        if (state.isStreaming && state.cursorState.isEnabled) {
            val axisX = event.getAxisValue(MotionEvent.AXIS_X)
            val axisY = event.getAxisValue(MotionEvent.AXIS_Y)

            // Deadzone filtering
            val deadzone = 0.15f
            val dx = if (abs(axisX) > deadzone) axisX else 0f
            val dy = if (abs(axisY) > deadzone) axisY else 0f

            if (dx != 0f || dy != 0f) {
                val dm = resources.displayMetrics
                viewModel.moveCursor(dx, dy, dm.widthPixels.toFloat(), dm.heightPixels.toFloat())
            }

            // Right stick scrolling
            val axisZ = event.getAxisValue(MotionEvent.AXIS_Z)
            val axisRZ = event.getAxisValue(MotionEvent.AXIS_RZ)
            val scrollDelta = if (abs(axisRZ) > 0.2f) axisRZ else if (abs(axisZ) > 0.2f) axisZ else 0f
            if (scrollDelta != 0f) {
                activeWebView?.scrollBy(0, (scrollDelta * 24f).toInt())
            }

            return true
        }

        // In gaming mode, pass motion events to WebView for HTML5 Gamepad API
        activeWebView?.dispatchGenericMotionEvent(event)
        return super.dispatchGenericMotionEvent(event)
    }

    // ==========================================
    // Gamepad detection
    // ==========================================

    private fun detectConnectedGamepads() {
        val deviceIds = InputDevice.getDeviceIds()
        var foundGamepad: String? = null
        for (id in deviceIds) {
            val device = InputDevice.getDevice(id) ?: continue
            val sources = device.sources
            if ((sources and InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD ||
                (sources and InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK
            ) {
                foundGamepad = device.name
                break
            }
        }
        viewModel.setGamepadConnected(foundGamepad)
    }

    override fun onInputDeviceAdded(deviceId: Int) {
        detectConnectedGamepads()
    }

    override fun onInputDeviceRemoved(deviceId: Int) {
        detectConnectedGamepads()
    }

    override fun onInputDeviceChanged(deviceId: Int) {
        detectConnectedGamepads()
    }
}
