package com.numq.androidgrpcimageboard.presentation.common.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner

val context @Composable get() = LocalContext.current

val lifecycleOwner @Composable get() = LocalLifecycleOwner.current
