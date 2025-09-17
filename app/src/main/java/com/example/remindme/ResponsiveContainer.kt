package com.example.remindme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.window.core.layout.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker

import androidx.compose.ui.platform.LocalContext
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ResponsiveContainer(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    content: @Composable () -> Unit,
    extraContents: List<@Composable () -> Unit> = emptyList(),
    foldingFeature: FoldingFeature? = null
) {
    // Checking screen size
    println("Window Size Class Details of current screen")
    println(windowSizeClass)
    // Checking folding feature
    println("Current folding feature is:")
    println(foldingFeature)

//    val context = LocalContext.current
//    val windowInfo by WindowInfoTracker.getOrCreate(context)
//        .windowLayoutInfo(context as ComponentActivity)
//        .collectAsState(initial = null)
    // Observe WindowLayoutInfo as state
//    val windowInfo by produceState(initialValue = null) {
//        val tracker = WindowInfoTracker.getOrCreate(context)
//        tracker.windowLayoutInfo(context as ComponentActivity)
//            .collect { value = it }
//    }
//    val foldingFeature = windowInfo?.displayFeatures
//        ?.filterIsInstance<FoldingFeature>()
//        ?.firstOrNull()
//    println(foldingFeature)

    LaunchedEffect(Unit) {
        delay(2000)
    }

    // TODO:
    // 1. Different Screen Sizes
    // Based on WindowSizeClass width and height values --> design panes for the Composables (1 or 2)
    // 2. If foldable device, then:
    // Use WindowInfoTracker to expose window layout information (Kotlin Flow)

    if (foldingFeature == null) {
        println("Not a foldable device")
        when (windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.COMPACT,
            WindowWidthSizeClass.MEDIUM -> {
                // Show a single pane
                println("Showing single pane")
                Box(modifier = modifier.fillMaxSize()) {
                    content()
                }
            }

            // For EXPANDED and larger widths
            else -> {
                // Show multiple panes side by side, going through the list of composables
                // if the list is empty, just show a single pane for content composable
                val allContents = listOf(content) + extraContents
                println("ELSE: Showing multiple panes")
                Row(modifier = modifier.fillMaxSize()) {
                    allContents.forEach {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            it()
                        }
                    }
                }
            }
        }
    } else {
        when (foldingFeature.state) {
            // Opened up halfway, like a book -> show up to 2 panes max
            FoldingFeature.State.HALF_OPENED -> {
                when (foldingFeature.orientation) {
                    // TABLETOP MODE
                    FoldingFeature.Orientation.HORIZONTAL -> {
                        // Show a single pane
                        println("Showing single pane")
                        Box(modifier = modifier.fillMaxSize()) {
                            content()
                        }
                    }
                    // BOOK MODE
                    FoldingFeature.Orientation.VERTICAL -> {
//                        BoxWithConstraints (Modifier.fillMaxSize()) {
//                            val hingeBounds = foldingFeature.bounds
//                            val screenWidth = constraints.maxWidth
//                        }

                        if (extraContents.isEmpty()) {
                            Box(modifier = modifier.fillMaxSize()) {
                                content()
                            }
                        } else {
                            Row(modifier = modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                )
                            }

                            val allContents = listOf(content) + extraContents[0]
                            Row(modifier = modifier.fillMaxSize()) {
                                allContents.forEach {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                    ) {
                                        it()
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Opened up fully -> should show 1 full pane
            FoldingFeature.State.FLAT -> {
                // Show a single pane
                println("Showing single pane")
                Box(modifier = modifier.fillMaxSize()) {
                    content()
                }
            }
        }
    }


}