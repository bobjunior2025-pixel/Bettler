package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Better Boosteroid", appName)
  }

  @Test
  fun `verify default stream settings are optimized for Fire Stick Lite`() {
    val viewModel = com.example.viewmodel.BoosteroidViewModel()
    val state = viewModel.uiState.value
    // Fire Stick Lite defaults to 720p for 1GB RAM stability
    assertEquals(com.example.model.StreamResolution.RES_720P, state.settings.resolution)
    assertEquals(true, state.settings.fireStickLiteMode)
  }
}
