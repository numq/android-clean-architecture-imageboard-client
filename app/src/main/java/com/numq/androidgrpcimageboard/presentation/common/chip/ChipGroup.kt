package com.numq.androidgrpcimageboard.presentation.common.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.unit.dp

/**
 * Simple "chip group" implementation
 */

@Composable
fun ChipGroup(strings: List<String>, onSelect: (String) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        LazyRow() {
            items(strings) { s ->
                Chip(s) {
                    onSelect(it)
                }
            }
        }
    }
}

@Composable
fun Chip(
    title: String,
    onSelect: (String) -> Unit
) {

    var isSelected by remember {
        mutableStateOf(false)
    }

    val color by remember {
        mutableStateOf(if (isSelected) LightGray else DarkGray)
    }

    Card(modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
        Column(
            modifier = Modifier
                .height(24.dp)
                .width(64.dp)
                .background(color)
                .clickable {
                    isSelected = !isSelected
                    onSelect(title)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = title)
        }
    }
}
