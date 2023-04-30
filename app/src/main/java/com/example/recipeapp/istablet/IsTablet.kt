package com.example.recipeapp.istablet

import android.content.Context
import android.content.res.Configuration

enum class Device {
    DEVICE_TYPE
}

private fun isTablet(context: Context): Boolean {
    return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

fun getDeviceInfo (context: Context, device: Device?): String {
    try {
        when (device) {
            Device.DEVICE_TYPE -> return if (isTablet(context)) {
                if (getDevice5inch(context)) {
                    "Tablet"
                } else {
                    "Mobile"
                }
            } else {
                "Mobile"
            } else -> {

        }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

private fun getDevice5inch(context: Context): Boolean {
    return try {
        val displayMetrics = context.resources.displayMetrics
        val yinch = displayMetrics.heightPixels / displayMetrics.ydpi
        val xinch = displayMetrics.widthPixels / displayMetrics.xdpi
        val diagonalinch = Math.sqrt(xinch * xinch + yinch * yinch.toDouble())

        diagonalinch >= 7

    } catch (e: Exception) {
        false
    }
}