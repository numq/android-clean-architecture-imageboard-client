package com.numq.androidgrpcimageboard.presentation.features.failure

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.numq.androidgrpcimageboard.platform.exception.AppExceptions

@Composable
fun ShowException(
    scaffoldState: ScaffoldState,
    exception: Exception,
    action: () -> Unit = {}
) {

    suspend fun show(
        msg: String?,
        label: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) = scaffoldState.snackbarHostState.showSnackbar(
        message = msg ?: AppExceptions.DEFAULT_MESSAGE,
        actionLabel = label,
        duration = duration
    )

    LaunchedEffect(exception) {
        when (exception) {
            AppExceptions.ServiceException.NetworkException -> {
                show(exception.message, duration = SnackbarDuration.Indefinite)
            }
            AppExceptions.ServiceException.PermissionException -> {
                show(exception.message, "Grant", SnackbarDuration.Indefinite)
            }
            else -> {
                show(exception.message)
            }
        }
        action()
    }
}