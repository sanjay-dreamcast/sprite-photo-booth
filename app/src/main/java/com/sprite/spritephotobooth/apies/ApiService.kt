package com.app.autofacedetectdemo.ui.apies


import com.sprite.spritephotobooth.data.model.UploadPhoto
import com.sprite.spritephotobooth.data.model.VerifyModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.HashMap

interface ApiService {



    @Multipart
    @POST("{api}")
    fun SendPhoto(@Path("api") id: String?,@PartMap map: HashMap<String, RequestBody>?,): Call<UploadPhoto>?
    @Multipart
    @POST("{api}")
    fun Sendwhatsapp(@Path("api") id: String?,@PartMap map: HashMap<String, RequestBody>?,): Call<VerifyModel>?





}