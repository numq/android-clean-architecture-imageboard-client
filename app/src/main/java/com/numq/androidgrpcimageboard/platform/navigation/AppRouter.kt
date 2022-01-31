package com.numq.androidgrpcimageboard.platform.navigation

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.numq.androidgrpcimageboard.platform.constant.AppConstants
import com.numq.androidgrpcimageboard.platform.exception.AppExceptions
import com.numq.androidgrpcimageboard.platform.extension.isCurrent
import com.numq.androidgrpcimageboard.platform.extension.navigateSafe
import com.numq.androidgrpcimageboard.platform.extension.prefs
import com.numq.androidgrpcimageboard.platform.service.network.NetworkHandler
import com.numq.androidgrpcimageboard.presentation.common.extension.context
import com.numq.androidgrpcimageboard.presentation.features.board.BoardScreen
import com.numq.androidgrpcimageboard.presentation.features.failure.ShowException
import com.numq.androidgrpcimageboard.presentation.features.home.HomeScreen
import com.numq.androidgrpcimageboard.presentation.features.thread.ThreadScreen
import java.util.*

@Composable
fun AppRouter(networkHandler: NetworkHandler) {

    val navController = rememberNavController()
    val prefs = context.prefs

    val scaffoldState = rememberScaffoldState()
    var connected by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        connected = networkHandler.isNetworkAvailable
    }

    Scaffold(scaffoldState = scaffoldState, bottomBar = {

        BuildBottomBar(prefs, navController) {
            if (networkHandler.isNetworkAvailable) navController.navigateSafe(it.destination)
        }

    }) {

        if (!connected) {
            ShowException(scaffoldState, AppExceptions.ServiceException.NetworkException)
        }
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (connected) {
                NavHost(navController = navController, startDestination = AppConstants.Nav.HOME) {
                    composable(AppConstants.Nav.HOME) { HomeScreen(scaffoldState, navController) }
                    composable(AppConstants.Nav.BOARD) { BoardScreen(scaffoldState, navController) }
                    composable(AppConstants.Nav.THREAD) {
                        ThreadScreen(
                            scaffoldState,
                            navController
                        )
                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}

sealed class BottomMenu(val destination: String, val icon: ImageVector) {
    object Home : BottomMenu(AppConstants.Nav.HOME, Icons.Rounded.Home)
    object Board : BottomMenu(AppConstants.Nav.BOARD, Icons.Rounded.ViewList)
    object Thread : BottomMenu(AppConstants.Nav.THREAD, Icons.Rounded.Article)
}

@Composable
fun BuildBottomBar(
    prefs: SharedPreferences,
    navController: NavController,
    onItemClick: (BottomMenu) -> Unit
) {
    BottomAppBar() {
        BottomNavigationItem(
            selected = navController.isCurrent(AppConstants.Nav.HOME),
            onClick = { onItemClick(BottomMenu.Home) },
            icon = {
                Icon(imageVector = BottomMenu.Home.icon, BottomMenu.Board.destination)
            },
            label = {
                Text(BottomMenu.Home.destination.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                })
            })
        BottomNavigationItem(
            selected = navController.isCurrent(AppConstants.Nav.BOARD),
            onClick = {
                if (prefs.contains(AppConstants.Args.ACTIVE_BOARD_ID)) {
                    onItemClick(BottomMenu.Board)
                }
            }, icon = {
                Icon(imageVector = BottomMenu.Board.icon, BottomMenu.Board.destination)
            },
            label = {
                Text(BottomMenu.Board.destination.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                })
            })
        BottomNavigationItem(
            selected = navController.isCurrent(AppConstants.Nav.THREAD),
            onClick = {
                if (prefs.contains(AppConstants.Args.ACTIVE_THREAD_ID)) {
                    onItemClick(BottomMenu.Thread)
                }
            }, icon = {
                Icon(imageVector = BottomMenu.Thread.icon, BottomMenu.Thread.destination)
            },
            label = {
                Text(BottomMenu.Thread.destination.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                })
            })
    }
}
