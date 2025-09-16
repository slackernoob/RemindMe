package com.example.remindme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.window.core.layout.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ResponsiveContainer(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    content: @Composable () -> Unit
) {
    println("Window Size Class Details of current screen")
    println(windowSizeClass)
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    // TODO:
    // 1. Different Screen Sizes
    // Based on WindowSizeClass width and height values --> design panes for the Composables (1 or 2)
    // 2. If foldable device, then:
    // Use WindowInfoTracker to expose window layout information (Kotlin Flow)
    Box(modifier = modifier.fillMaxSize()) {
        content()
    }
}