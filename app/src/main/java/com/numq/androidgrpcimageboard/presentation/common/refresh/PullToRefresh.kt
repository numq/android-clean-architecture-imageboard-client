package com.numq.androidgrpcimageboard.presentation.common.refresh

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import kotlinx.coroutines.delay

/**
 * Simplest "pull to refresh" implementation.
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullToRefresh(
    action: () -> Boolean,
    content: @Composable () -> Unit
) {

    val repetitionDelay = 1000L

    val orientation = Orientation.values()[LocalConfiguration.current.orientation]

    var showBar by remember {
        mutableStateOf(false)
    }

    var position by remember {
        mutableStateOf(0f)
    }

    val state = rememberDraggableState {
        position = it
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .draggable(state, orientation = orientation, onDragStarted = {
                showBar = true
            }, onDragStopped = {
                showBar = action()
                delay(repetitionDelay)
                showBar = false
            })
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showBar) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            content()
        }
    }
}