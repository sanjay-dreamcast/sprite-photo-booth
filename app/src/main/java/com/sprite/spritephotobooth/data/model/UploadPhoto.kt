package com.sprite.spritephotobooth.data.model
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadPhoto(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: Data
)

data class Data(
    @SerializedName("gender")
    val gender: String,

    @SerializedName("template")
    val template: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("ai_img")
    val aiImg: String?,

    @SerializedName("ai_qr")
    val aiQr: String?,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("id")
    val id: Int
)