package com.sprite.spritephotobooth.ui

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.sprite.spritephotobooth.R


class ProgressDialogClass(context: Context?) :
    Dialog(context!!, R.style.my_dialog_theme) {
    init {
        window!!.setBackgroundDrawableResource(R.color.transparent)
        val wlmp = window!!.attributes
        wlmp.gravity = Gravity.CENTER_HORIZONTAL
        window!!.attributes = wlmp
        setTitle(null)
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.progress_bar, null
        )
        setContentView(view)
    }
}