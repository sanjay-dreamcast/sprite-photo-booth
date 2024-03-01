package com.sprite.spritephotobooth.ui

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.sprite.spritephotobooth.Base.BaseActivity
import com.sprite.spritephotobooth.R

import com.sprite.spritephotobooth.databinding.AcivitySendWhatsappBinding

class SendWhatsappActivity : BaseActivity() {
    private var binding: AcivitySendWhatsappBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.acivity_send_whatsapp)
        init()
    }

    override fun initArguments() {

    }

    override fun initViews() {


        binding!!.btnSubmit.setOnClickListener {
            if (binding!!.editTextName.text.toString().isEmpty()) {
                binding!!.editTextName.error = "Name cannot be empty"
                return@setOnClickListener
            }
            if (binding!!.editText.text.toString().isEmpty()) {
                binding!!.editText!!.error = "WhatsApp number cannot be empty"
                return@setOnClickListener
            }
            if (binding!!.editText!!.text.toString().length < 10) {
                binding!!.editText.error = "WhatsApp number should be at least 10 digits"
                return@setOnClickListener
            }

        }

    }



    fun setWhatsAppEditTextProperties(editText: EditText) {
         val MAX_WHATSAPP_NUMBER_LENGTH: Int =10
        editText.inputType = InputType.TYPE_CLASS_PHONE
        editText.filters = arrayOf(InputFilter.LengthFilter(MAX_WHATSAPP_NUMBER_LENGTH))
    }



    private fun validateWhatsAppNumber(number: String,editText: EditText) {
        if (number.isEmpty()) {
            editText.error = "WhatsApp number cannot be empty"
        } else if (number.length < 10) {
            editText.error = "WhatsApp number should be at least 10 digits"
        }
    }
    override fun setupListener() {
    }

    override fun loadData() {
    }

}