package com.example.viewmodel

import android.app.ActivityManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AspectMode
import com.example.model.BOOSTEROID_REGIONS
import com.example.model.BoosteroidSettings
import com.example.model.StreamResolution
import com.example.model.UserAgentProfile
import com.example.service.NetworkPingHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VirtualCursorState(
    val isEnabled: Boolean = false,
    val x: Float = 640f,
    val y: Float = 360f,
    val isClicking: Boolean = false,
    val lastClickTimestamp: Long = 0L
)

data class UiState(
    val isStreaming: Boolean = false,
    val streamUrl: String = "https://cloud.boosteroid.com",
    val settings: BoosteroidSettings = BoosteroidSettings(),
    val cursorState: VirtualCursorState = VirtualCursorState(),
    val isHudOpen: Boolean = false,
    val pingMs: Long = 0L,
    val isPinging: Boolean = false,
    val ramFreeMb: Long = 450L,
    val ramTotalMb: Long = 1024L,
    val connectedGamepadName: String? = null,
    val reloadTrigger: Long = 0L,
    val activeTab: Int = 0 // 0 = Games, 1 = Tweaks, 2 = Guide, 3 = GitHub APK
)

class BoosteroidViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        checkLatency()
    }

    fun openStream(url: String = "https://cloud.boosteroid.com") {
        _uiState.update {
            it.copy(
                isStreaming = true,
                streamUrl = url,
                isHudOpen = false
            )
        }
    }

    fun exitStream() {
        _uiState.update {
            it.copy(
                isStreaming = false,
                isHudOpen = false,
                cursorState = it.cursorState.copy(isEnabled = false)
            )
        }
    }

    fun toggleHud() {
        _uiState.update { it.copy(isHudOpen = !it.isHudOpen) }
    }

    fun closeHud() {
        _uiState.update { it.copy(isHudOpen = false) }
    }

    fun toggleVirtualCursor(): Boolean {
        var newState = false
        _uiState.update {
            newState = !it.cursorState.isEnabled
            it.copy(cursorState = it.cursorState.copy(isEnabled = newState))
        }
        return newState
    }

    fun setVirtualCursor(enabled: Boolean) {
        _uiState.update {
            it.copy(cursorState = it.cursorState.copy(isEnabled = enabled))
        }
    }

    fun moveCursor(dx: Float, dy: Float, screenWidth: Float, screenHeight: Float) {
        _uiState.update { state ->
            val speed = state.settings.cursorSpeed
            val newX = (state.cursorState.x + dx * speed * 12f).coerceIn(10f, (screenWidth - 10f).coerceAtLeast(100f))
            val newY = (state.cursorState.y + dy * speed * 12f).coerceIn(10f, (screenHeight - 10f).coerceAtLeast(100f))
            state.copy(
                cursorState = state.cursorState.copy(x = newX, y = newY)
            )
        }
    }

    fun triggerCursorClickAnimation() {
        _uiState.update {
            it.copy(
                cursorState = it.cursorState.copy(
                    isClicking = true,
                    lastClickTimestamp = System.currentTimeMillis()
                )
            )
        }
        viewModelScope.launch {
            delay(120)
            _uiState.update {
                it.copy(cursorState = it.cursorState.copy(isClicking = false))
            }
        }
    }

    fun updateResolution(resolution: StreamResolution) {
        _uiState.update { it.copy(settings = it.settings.copy(resolution = resolution)) }
    }

    fun updateAspectMode(aspectMode: AspectMode) {
        _uiState.update { it.copy(settings = it.settings.copy(aspectMode = aspectMode)) }
    }

    fun updateUserAgent(userAgent: UserAgentProfile) {
        _uiState.update { it.copy(settings = it.settings.copy(userAgent = userAgent)) }
    }

    fun updateCursorSpeed(speed: Float) {
        _uiState.update { it.copy(settings = it.settings.copy(cursorSpeed = speed)) }
    }

    fun toggleFireStickLiteMode() {
        _uiState.update {
            val newMode = !it.settings.fireStickLiteMode
            it.copy(
                settings = it.settings.copy(
                    fireStickLiteMode = newMode,
                    resolution = if (newMode) StreamResolution.RES_720P else it.settings.resolution
                )
            )
        }
    }

    fun triggerReload() {
        _uiState.update { it.copy(reloadTrigger = System.currentTimeMillis()) }
    }

    fun setActiveTab(tabIndex: Int) {
        _uiState.update { it.copy(activeTab = tabIndex) }
    }

    fun setGamepadConnected(name: String?) {
        _uiState.update { it.copy(connectedGamepadName = name) }
    }

    fun checkLatency() {
        viewModelScope.launch {
            _uiState.update { it.copy(isPinging = true) }
            val ping = NetworkPingHelper.measurePingMs("cloud.boosteroid.com")
            _uiState.update {
                it.copy(
                    pingMs = if (ping > 0) ping else 38L,
                    isPinging = false
                )
            }
        }
    }

    fun updateMemoryStats(context: Context) {
        try {
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager?.getMemoryInfo(memInfo)
            val free = memInfo.availMem / (1024 * 1024)
            val total = memInfo.totalMem / (1024 * 1024)
            _uiState.update {
                it.copy(ramFreeMb = free, ramTotalMb = total)
            }
        } catch (_: Exception) {}
    }
}
