package com.sprite.spritephotobooth.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.sprite.spritephotobooth.Base.BaseActivity
import com.sprite.spritephotobooth.R

import com.sprite.spritephotobooth.databinding.AcivitySendWhatsappBinding
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.autofacedetectdemo.ui.apies.ApiService
import com.app.autofacedetectdemo.ui.apies.NetworkAlertUtility
import com.bumptech.glide.Glide
import com.sprite.spritephotobooth.MainActivity
import com.sprite.spritephotobooth.data.model.UploadPhoto
import com.sprite.spritephotobooth.data.model.VerifyModel
import com.sprite.spritephotobooth.utils.appUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.commons.lang.WordUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection

class SendWhatsappActivity : BaseActivity() {
    private var binding: AcivitySendWhatsappBinding? = null
    private var mProgressDialog: ProgressDialogClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = DataBindingUtil.setContentView(this,R.layout.acivity_send_whatsapp)
        init()
        setupUI(binding!!.Mainparent)
        binding!!.layout.tvTitle.setText("")
        binding!!.layout.ivBack.setOnClickListener {
            finish()
        }
        binding!!.editText.addTextChangedListener(object : TextWatcher {
            var mStart = 0
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                mStart = i + i3
            }

            override fun afterTextChanged(editable: Editable) {
                val capitalizedText = WordUtils.capitalize(binding!!.editText.text.toString())
                if (capitalizedText != binding!!.editText.text.toString()) {
                    binding!!.editText.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                        }

                        override fun afterTextChanged(s: Editable) {
                            binding!!.editText.setSelection(mStart)
                            binding!!.editText.removeTextChangedListener(this)
                        }
                    })
                    binding!!.editText.setText(capitalizedText)
                }
            }
        })
        Log.e("@@id1",""+intent.getStringExtra("id"))
        Log.e("@@ai_img2",""+intent.getStringExtra("ai_img"))
        Glide.with(this).load(intent.getStringExtra("ai_img").toString()).into(binding!!.recyScene)
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputManager = activity.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        val focusedView = activity.currentFocus
        if (focusedView != null) {
            try {
                assert(inputManager != null)
                inputManager.hideSoftInputFromWindow(
                    focusedView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            } catch (e: AssertionError) {
                e.printStackTrace()
            }
        }
    }
    private fun setupUI(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(this@SendWhatsappActivity)
                false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    override fun initArguments() {

    }

    override fun initViews() {

        binding!!.appCompatButton3.setOnClickListener{

            finish()
        }
        binding!!.btnSubmit.setOnClickListener {
            if (binding!!.editText.text.toString().isEmpty()) {
                binding!!.editText.error = "Name cannot be empty"
                return@setOnClickListener
            }
            if (binding!!.editTextName.text.toString().isEmpty()) {
                binding!!.editTextName!!.error = "WhatsApp number cannot be empty"
                return@setOnClickListener
            }
            if (binding!!.editTextName!!.text.toString().length < 10) {
                binding!!.editTextName.error = "WhatsApp number should be at least 10 digits"
                return@setOnClickListener
            }
            binding!!.btnSubmit.isEnabled=false

            Handler(Looper.getMainLooper()).postDelayed({

                binding!!.btnSubmit.isEnabled=true

                //Do something after 100ms
            }, 1500)
            if (NetworkAlertUtility.isConnectingToInternet(this)) {
                ApiHitQr()
            }
            else{
                Toast.makeText(this,"No internet connection", Toast.LENGTH_SHORT).show()
            }

        }

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
        loadProgressBar(this@SendWhatsappActivity,"")
        var id=intent.getStringExtra("id")

        val BASE_URL = "https://engagement.vehub.live/sprite2024/api/"
        val zapping_user_search = "send_whatsapp"

        var url=BASE_URL.toString()

        if(!url.endsWith("/"))
        {
            url=url+"/"
        }
        val retrofit: Retrofit = getRetrofitInstance(url)

        val apiService: ApiService = retrofit.create(ApiService::class.java)

        val map: HashMap<String, RequestBody> = HashMap<String, RequestBody>()

        map["name"] = RequestBody.create("text/plain".toMediaTypeOrNull(), binding!!.editText.text.toString().trim())
        map["id"] = RequestBody.create("text/plain".toMediaTypeOrNull(), id.toString())
        map["mobile"] = RequestBody.create("text/plain".toMediaTypeOrNull(), binding!!.editTextName.text.toString().trim().toString())

        val call = apiService.Sendwhatsapp(zapping_user_search.toString(),map)


        call!!.enqueue(object : Callback<VerifyModel> {
            override fun onResponse(call: Call<VerifyModel>, response: Response<VerifyModel>) {

                dismissProgressBar(this@SendWhatsappActivity)

                if(response.code()==200 && response.body()!=null)
                {
                    if (response!!.body()!!.success!!) {
                        val intent = Intent(this@SendWhatsappActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this@SendWhatsappActivity,response!!.body()!!.getMessage().toString(),Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    appUtils.ConstantError(response.errorBody()!!.string(),this@SendWhatsappActivity)
                }

            }

            override fun onFailure(call: Call<VerifyModel>, t: Throwable) {
                dismissProgressBar(this@SendWhatsappActivity)
                Toast.makeText(this@SendWhatsappActivity,"Something Went wrong",Toast.LENGTH_LONG).show()
                //  CommandSend("F")
                // handlerOpenPaymentrefresh.removeCallbacks(OpenRunnablerefresh(this@MainActivityKotlin))
                // handlerOpenPaymentrefresh.postDelayed(OpenRunnablerefresh(this@MainActivityKotlin), 500)
            }
        })
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