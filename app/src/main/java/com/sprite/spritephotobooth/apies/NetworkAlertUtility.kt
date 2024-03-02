package com.app.autofacedetectdemo.ui.apies

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.sprite.spritephotobooth.R



object NetworkAlertUtility {
    var alert: AlertDialog? = null

    @JvmStatic
    fun isConnectingToInternet(context: Context?): Boolean {
        if (context != null) {
            val connectivity =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                val info = connectivity.allNetworkInfo
                if (info != null) for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun showNetworkFailureAlert(context: Context?) {
        if (context != null && !(context as Activity).isFinishing && (alert == null || alert != null && !alert!!.isShowing)) {
            val builder = AlertDialog.Builder(context, R.color.white)
            builder.setMessage("No Internet Connection")
                .setTitle("Alert!")
                .setCancelable(false)
                .setNegativeButton("OK") { dialog, id -> }
            alert = builder.create()
            alert!!.show()
            val nbutton = alert!!.getButton(DialogInterface.BUTTON_NEGATIVE)
            nbutton.setTextColor(context.getResources().getColor(R.color.black))
        }
    }
}