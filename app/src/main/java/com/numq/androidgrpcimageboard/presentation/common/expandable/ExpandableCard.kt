package com.numq.androidgrpcimageboard.presentation.common.expandable

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

/**
 * Card which expands and shows some additional content.
 */

@Composable
fun ExpandableCard(
    modifier: Modifier = Modifier,
    expandable: Boolean = true,
    onItemClick: () -> Unit = {},
    expandedContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Card(modifier = (if (expandable) modifier.pointerInput(Unit) {
        detectDragGestures(onDragStart = {
            expanded = true
        }, onDrag = { change, dragAmount ->
            if (dragAmount.isValid() && change.uptimeMillis > 1000) {
                expanded = true
            }
        }, onDragEnd = {
            expanded = false
        })
    } else modifier)
        .padding(4.dp)
        .animateContentSize()
        .clickable {
            onItemClick()
        }, elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            content()
            if (expanded) {
                expandedContent()
            }
        }
    }
}