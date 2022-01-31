package com.numq.androidgrpcimageboard.platform.extension

import android.content.*
import android.net.Uri
import android.provider.Settings
import com.numq.androidgrpcimageboard.platform.constant.AppConstants

fun Context.prefs(key: String = AppConstants.Prefs.APP_PREFS): SharedPreferences =
    getSharedPreferences(key, Context.MODE_PRIVATE)

val Context.prefs get() = prefs()

fun Context.copyText(text: String, label: String = "default") {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
}

fun Context.openSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        data = Uri.fromParts("package", packageName, null)
    }
    startActivity(intent)
}