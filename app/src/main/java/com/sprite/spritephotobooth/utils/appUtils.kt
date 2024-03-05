package com.sprite.spritephotobooth.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sprite.spritephotobooth.R
import com.sprite.spritephotobooth.databinding.BottomSheetDialogErrorBinding

object appUtils {

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }
    fun ConstantError(Error: String, contxt: Context) {

        try{
            val context = contxt.applicationContext
            var bottomSheetDialog = BottomSheetDialog(contxt)
            val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            bottomSheetDialog.setCancelable(false)
            var bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_dialog_error, null)
            var bottomSheetBinding = BottomSheetDialogErrorBinding.bind(bottomSheetView)
            bottomSheetDialog!!.setContentView(bottomSheetView)

            bottomSheetDialog!!.setOnShowListener {
                val bottomSheetDialog = it as BottomSheetDialog
                val parentLayout =
                    bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                parentLayout?.let { it ->
                    val behaviour = BottomSheetBehavior.from(it)
                    setupFullHeight(it)
                    behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
            bottomSheetBinding!!.txtError.setText( ""+Error)
            bottomSheetBinding!!.tvProceedToRedeem.setOnClickListener{
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog!!.show()
        }
        catch (e:Exception)
        {}


    }

}