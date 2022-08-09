package com.dracoo.medicinemanagement.utils

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

object MedicalUtil {

    fun snackBarMessage(snackMessage: String, activity: Activity, action: Int) {
        val rootView = activity.window.decorView.findViewById<View>(android.R.id.content)
        when(action) {
            ConstantsObject.vSnackBarWithTombol -> {
                val snackBar = Snackbar.make(rootView, snackMessage, Snackbar.LENGTH_INDEFINITE)
                snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5
                snackBar.setActionTextColor(Color.WHITE)
                snackBar.setAction("OK") {
                    snackBar.dismiss()
                }
                snackBar.show()
            }
            else -> {
                val snackBar = Snackbar.make(rootView, snackMessage, Snackbar.LENGTH_INDEFINITE)
                snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5
                snackBar.setActionTextColor(Color.WHITE)
                snackBar.show()
            }
        }
    }
}