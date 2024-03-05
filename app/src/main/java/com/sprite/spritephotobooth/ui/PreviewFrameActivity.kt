package com.sprite.spritephotobooth.ui

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Transition
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.autofacedetectdemo.ui.apies.ApiService
import com.app.autofacedetectdemo.ui.apies.NetworkAlertUtility
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.sprite.spritephotobooth.Base.BaseActivity
import com.sprite.spritephotobooth.data.model.UploadPhoto
import com.sprite.spritephotobooth.databinding.ActivityPreviewFrameBinding
import com.sprite.spritephotobooth.utils.appUtils
import com.sprite.spritephotobooth.utils.deleteFileFromDirectory
import com.sprite.spritephotobooth.utils.loadImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.FileInputStream
import java.io.IOException

class PreviewFrameActivity : BaseActivity() {
    private var binding: ActivityPreviewFrameBinding? = null
    private var mProgressDialog: ProgressDialogClass? = null
    companion object {
        const val SAVED_FILE_NAME = "savedFileName"
        const val SAVED_URI = "savedFileName"

        @JvmStatic
        fun start(context: Context, savedFileName: String, savedUri: Uri) {
            val starter = Intent(context, PreviewFrameActivity::class.java).apply {
                putExtra(SAVED_FILE_NAME,savedFileName)
                putExtra(SAVED_URI,savedUri)
            }
            context.startActivity(starter)
        }

    }
    private val savedFileName by lazy {
        intent.getStringExtra(SAVED_FILE_NAME)
    }
    private val savedUri by lazy {
        intent.getStringExtra(SAVED_FILE_NAME)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityPreviewFrameBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(this.root)
            lifecycleOwner = this@PreviewFrameActivity
            executePendingBindings()
        }
        init()
        binding!!.layout.tvTitle.text="Preview Frame"
        binding!!.layout.ivBack.setOnClickListener {
            finish()
        }

    }

    override fun initArguments() {
    }

    override fun initViews() {
        binding!!.btnRetake.setOnClickListener {
            savedFileName?.let { it1 -> deleteFileFromDirectory(it1) }
            finish()
        }
        Log.e("@@URI",""+intent.getStringExtra("imageuri"))
  /*      Glide.with(this)
            .asBitmap()
            .load(intent.getStringExtra("imageuri"))
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // Check if the image is mirrored
                    val matrix = Matrix()
                    matrix.preScale(-1f, 1f) // Flip horizontally

                    val mirroredBitmap = Bitmap.createBitmap(resource, 0, 0, resource.width, resource.height, matrix, false)

                    // Compare original and mirrored Bitmaps
                    val isMirrored = areBitmapsEqual(resource, mirroredBitmap)

                    // Use isMirrored as needed
                    if (isMirrored) {
                        // The image is mirrored
                        // Handle accordingly
                        Log.e("@true","1")
                    } else {
                        Log.e("@true","2")
                        // The image is not mirrored
                        // Handle accordingly
                    }
                }
            })*/
        Glide.with(this).load(intent.getStringExtra("imageuri")).into(binding!!.recyScene)
        binding!!.btnConfirm.setOnClickListener {

            binding!!.btnConfirm.isEnabled=false

            Handler(Looper.getMainLooper()).postDelayed({

                binding!!.btnConfirm.isEnabled=true

                //Do something after 100ms
            }, 1500)
            if (NetworkAlertUtility.isConnectingToInternet(this)) {
                ApiHitQr()
            }
            else{
                Toast.makeText(this,"No internet connection",Toast.LENGTH_SHORT).show()
            }

           /* val intent = Intent(this@PreviewFrameActivity, PreviewWhatappActivity::class.java)
            intent.putExtra("gender", intent.getStringExtra("gender"))
            intent.putExtra("savedFileName", intent.getStringExtra("savedFileName"))
            intent.putExtra("imageuri", intent.getStringExtra("imageuri"))
            intent.putExtra("template", intent.getIntExtra("template",0))
            startActivity(intent)*/


        }
    }

    private fun areBitmapsEqual(bitmap1: Bitmap, bitmap2: Bitmap): Boolean {
        val width = bitmap1.width
        val height = bitmap1.height

        if (width != bitmap2.width || height != bitmap2.height) {
            return false
        }

        for (x in 0 until width) {
            for (y in 0 until height) {
                if (bitmap1.getPixel(x, y) != bitmap2.getPixel(x, y)) {
                    return false
                }
            }
        }

        return true
    }
    private fun getRetrofitInstance(baseUrl: String): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val timeoutSeconds: Long = 150
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .hostnameVerifier { hostname, session ->
                val hv = HttpsURLConnection.getDefaultHostnameVerifier()
                true
            }
            .addInterceptor(logging)
            .build()




        return Retrofit.Builder().client(okHttpClient)
            .baseUrl(baseUrl) // Pass the dynamic base URL here
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    /*  override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
          Log.e("@@key",""+event?.action)
          if (event?.action == KeyEvent.ACTION_DOWN) {
              Log.e("@@key22",""+event.keyCode)
              when (event.keyCode) {
                  KeyEvent.KEYCODE_MENU -> {
                      Log.e("@@Scandata1",""+scannedData)
                      edtext.setText(scannedData)
                     // handleBarcodeScan(scannedData)
                      scannedData = ""

                      if(!mQrCodeValue)
                      {
                          mQrCodeValue=true
                      }

                      return true
                  }
                  KeyEvent.KEYCODE_A -> {

                      return true
                  }
                  else -> {
                     val keyChar = event.unicodeChar.toChar()
                     // val realString = String(Character.toChars(event.unicodeChar))
                      val unicodeChar = event.unicodeChar.toChar()
                      val realString = unicodeChar.toString()
                     //  val realString = String(Character.toChars(event.unicodeChar))
                      scannedData += realString

                      return true
                  }
              }
          }
          return super.dispatchKeyEvent(event)
      }*/


    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
    private fun getBase64ForUriAndPossiblyCrash(uri: Uri): String {

        try {
            val bytes = contentResolver.openInputStream(uri)!!.readBytes()

            return Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (error: IOException) {
            error.printStackTrace() // This exception always occurs
        }
        return "".toString()
    }
    fun fileToBase64(context: Context, fileUri: Uri): String? {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(fileUri)
            val bytes = readBytes(inputStream!!)
            inputStream?.close()

            if (bytes != null) {
                val base64 = "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.DEFAULT)
                return base64
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun readBytes(inputStream: InputStream): ByteArray? {
        try {
            val byteBuffer = ByteArrayOutputStream()
            val bufferSize = 4096
            val buffer = ByteArray(bufferSize)

            var len: Int=0
            while (inputStream?.read(buffer)?.also { len = it } != -1) {
                byteBuffer.write(buffer, 0, len)
            }

            return byteBuffer.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
    public fun dismissProgressBar(context: Context?) {
        try {
            if (mProgressDialog != null) {
                if (context != null && !(context as AppCompatActivity).isFinishing && mProgressDialog!!.isShowing) {
                    mProgressDialog!!.dismiss()
                }
                mProgressDialog = null
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    public fun loadProgressBar(context: Context?, message: String?) {
        try {
            if (mProgressDialog != null) {
                if (context != null && !(context as AppCompatActivity).isFinishing && mProgressDialog!!.isShowing) {
                    mProgressDialog!!.dismiss()
                }
            }
            mProgressDialog = ProgressDialogClass(context)
            mProgressDialog!!.setCancelable(false)
            if (context != null && !(context as AppCompatActivity).isFinishing && !mProgressDialog!!.isShowing) mProgressDialog!!.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    fun ApiHitQr() {


        loadProgressBar(this@PreviewFrameActivity,"")

         var gender=intent.getStringExtra("gender").toString()
       // var savedFileName=  intent.getStringExtra("savedFileName")
        var imageuri=   intent.getStringExtra("imageuri").toString()
        var template= intent.getIntExtra("template",0)+1
        val uri: Uri = Uri.parse(imageuri)
        Log.e("@@@uri",""+uri)
       var base64= getBase64ForUriAndPossiblyCrash(uri)!!
        Log.e("@@base64",base64.toString())

        val BASE_URL = "https://engagement.vehub.live/sprite2024/api/"
        val zapping_user_search = "send_data_new"

        var url=BASE_URL.toString()

        if(!url.endsWith("/"))
        {
            url=url+"/"
        }
        val retrofit: Retrofit = getRetrofitInstance(url)

        val apiService: ApiService = retrofit.create(ApiService::class.java)

        val map: HashMap<String, RequestBody> = HashMap<String, RequestBody>()

        map["image"] = RequestBody.create("text/plain".toMediaTypeOrNull(), base64)
        map["template"] = RequestBody.create("text/plain".toMediaTypeOrNull(), if(gender.equals("male",ignoreCase = true)) "M"+template.toString() else "F"+template.toString())
        map["gender"] = RequestBody.create("text/plain".toMediaTypeOrNull(), gender.toString())
        val call = apiService.SendPhoto(zapping_user_search.toString(),map)

        call!!.enqueue(object : Callback<UploadPhoto> {
            override fun onResponse(call: Call<UploadPhoto>, response: Response<UploadPhoto>) {

                dismissProgressBar(this@PreviewFrameActivity)

                if(response.code()==200 && response.body()!=null)
                {
                    if (response!!.body()!!.success!!) {
                        val intent = Intent(this@PreviewFrameActivity, PreviewWhatappActivity::class.java)

                        intent.putExtra("id", response!!.body()!!.data.id.toString())
                        intent.putExtra("ai_img", response!!.body()!!.data.aiImg.toString())
                        intent.putExtra("ai_qr", response!!.body()!!.data.aiQr.toString())
                        startActivity(intent)

                    }else{
                        Toast.makeText(this@PreviewFrameActivity,response!!.body()!!.message.toString(),Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    appUtils.ConstantError(response.errorBody()!!.string(),this@PreviewFrameActivity)
                }


            }

            override fun onFailure(call: Call<UploadPhoto>, t: Throwable) {
                Toast.makeText(this@PreviewFrameActivity,"Something Went wrong",Toast.LENGTH_LONG).show()
                //  CommandSend("F")

                dismissProgressBar(this@PreviewFrameActivity)

                // handlerOpenPaymentrefresh.removeCallbacks(OpenRunnablerefresh(this@MainActivityKotlin))
                // handlerOpenPaymentrefresh.postDelayed(OpenRunnablerefresh(this@MainActivityKotlin), 500)
            }
        })
    }

    override fun setupListener() {
    }

    override fun loadData() {
       // binding?.recyScene?.loadImage(savedUri)

    }

    override fun onDestroy() {
        savedFileName?.let { it1 -> deleteFileFromDirectory(it1) }
        super.onDestroy()
    }
}