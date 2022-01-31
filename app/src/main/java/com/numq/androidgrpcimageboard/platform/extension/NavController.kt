package com.numq.androidgrpcimageboard.platform.extension

import androidx.navigation.NavController
import androidx.navigation.navOptions

fun NavController.isCurrent(destination: String) = currentDestination?.route == destination

fun NavController.navigateSafe(destination: String): Boolean {
    if (currentDestination?.route != destination) {
        navigate(destination, navOptions {
            popUpTo(destination) {
                inclusive = true
            }
        })
        return true
    }
    return false
}
