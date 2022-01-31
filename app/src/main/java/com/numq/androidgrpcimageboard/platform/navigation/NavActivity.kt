package com.numq.androidgrpcimageboard.platform.navigation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.numq.androidgrpcimageboard.platform.exception.AppExceptions
import com.numq.androidgrpcimageboard.platform.extension.openSettings
import com.numq.androidgrpcimageboard.platform.service.network.NetworkHandler
import com.numq.androidgrpcimageboard.presentation.features.failure.ShowException
import com.numq.androidgrpcimageboard.presentation.theme.AndroidGrpcImageboardTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NavActivity : ComponentActivity() {

    @Inject
    lateinit var networkHandler: NetworkHandler

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissions = listOf(
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.INTERNET
        )

        setContent {

            val permissionsState = rememberMultiplePermissionsState(permissions)

            PermissionsRequired(
                multiplePermissionsState = permissionsState,
                permissionsNotGrantedContent = {
                    permissionsState.launchMultiplePermissionRequest()
                },
                permissionsNotAvailableContent = {
                    val scaffoldState = rememberScaffoldState()
                    Scaffold {
                        Box(modifier = Modifier.padding(it)) {
                            Icon(Icons.Rounded.Error, "")
                        }
                        ShowException(
                            scaffoldState,
                            AppExceptions.ServiceException.PermissionException
                        ) {
                            applicationContext.openSettings()
                        }
                    }
                }) {
                AndroidGrpcImageboardTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        AppRouter(networkHandler)
                    }
                }
            }
        }
    }
}
