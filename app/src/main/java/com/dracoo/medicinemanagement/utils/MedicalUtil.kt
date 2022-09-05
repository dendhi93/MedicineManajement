package com.dracoo.medicinemanagement.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

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
                val snackBar = Snackbar.make(rootView, snackMessage, Snackbar.LENGTH_LONG)
                snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5
                snackBar.setActionTextColor(Color.WHITE)
                snackBar.show()
            }
        }
    }

    fun alertDialogDismiss(alertMessage : String,
                           alertTitle : String,context: Context,
                           isDismiss : Boolean){
        val alertDialog = AlertDialog.Builder(context).setTitle(alertTitle)
        alertDialog.setMessage(alertMessage)
        alertDialog.setPositiveButton(android.R.string.ok){
                dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.setCancelable(false)
        val alert: AlertDialog = alertDialog.create()
        when(isDismiss){
            true -> {
                if(alert.isShowing){
                    alert.dismiss()
                }
            }
            else ->{
                if(!alert.isShowing){
                    alert.show()
                }
            }
        }
    }

    fun showDialogConfirmation(
        context: Context,
        title: String,
        msg: String,
        callback: () -> Unit
    ) {
        val dialogConfirmation =  MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(msg)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") { _, _ -> callback() }
            .setCancelable(false)
            .create()
            .apply {
                setCanceledOnTouchOutside(false)
            }

        when {
            !dialogConfirmation.isShowing -> {
                dialogConfirmation.show()
            }
            else -> {
                dialogConfirmation.cancel()
                dialogConfirmation.dismiss()
            }
        }
    }


    fun getCurrentDateTime(dateTimeFormat : String): String =
        SimpleDateFormat(dateTimeFormat, Locale.getDefault()).format(Date())

    fun getChangeDateFormat(stDateTime: String, stSourceDateTimeFormat : String, stResultDateFormat : String): String? {
        return try {
            val source = SimpleDateFormat(stSourceDateTimeFormat, Locale.getDefault())

            val dateSource = source.parse(stDateTime.trim())
            val dateFormat = SimpleDateFormat(stResultDateFormat, Locale.getDefault())

            if (dateSource != null) {
                dateFormat.format(dateSource)
            } else { "" }
        } catch (e: Exception) { "" }
    }

    fun moneyFormat(input : Double) : String?{
        return try{
//            val format: NumberFormat = NumberFormat.getCurrencyInstance()
//            format.maximumFractionDigits = 0
//            format.currency = Currency.getInstance("IDR")
//
//            format.format(input).toString()
            val formatter: NumberFormat = DecimalFormat("#,###")
            formatter.format(input)
        }catch (e:Exception){ "" }
    }
}