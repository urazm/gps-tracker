package com.grnl.gpstracker.helpers

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.checkPermissions(p: String): Boolean {
    return when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.  checkSelfPermission(activity as AppCompatActivity, p) -> true
        else -> false
    }
}