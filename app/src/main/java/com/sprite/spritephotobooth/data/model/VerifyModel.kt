package com.sprite.spritephotobooth.data.model
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class VerifyModel {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null


    fun getStatus(): Boolean? {
        return success
    }

    fun setStatus(status: Boolean?) {
        this.success = status
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }




}