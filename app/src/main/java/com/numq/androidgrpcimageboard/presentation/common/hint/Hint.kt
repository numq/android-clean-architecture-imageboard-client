package com.numq.androidgrpcimageboard.presentation.common.hint

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * Component which shows on-screen buttons hints.
 */

data class Hint(val icon: @Composable () -> Unit, val description: @Composable () -> Unit)

@Composable
fun HintsDialog(hints: List<Hint>, onDismiss: (Boolean) -> Unit) {
    Dialog(onDismissRequest = {
        onDismiss(false)
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onDismiss(false) },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(hints) { hint ->
                    HintItem(hint)
                }
            }
            IconButton(onClick = { onDismiss(false) }) {
                Icon(Icons.Rounded.Done, "")
            }
        }
    }
}

@Composable
fun HintItem(hint: Hint) {
    Card {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(.7f),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            hint.icon()
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                hint.description()
            }
        }
    }
}