package com.sprite.spritephotobooth.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sprite.spritephotobooth.Base.BaseActivity
import com.sprite.spritephotobooth.databinding.ActivityStrikePoseBinding
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sprite.spritephotobooth.utils.afterMeasured
import com.sprite.spritephotobooth.utils.getOutputDirectory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.view.WindowManager
class StrikePoseActivity : BaseActivity() {
    private var binding: ActivityStrikePoseBinding? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    var camera: Camera? = null
    private var flash: Boolean? = null
    private var lensFacing = CameraSelector.DEFAULT_BACK_CAMERA
    private var savedFileName = ""

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, StrikePoseActivity::class.java).apply {
            }
            context.startActivity(starter)
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityStrikePoseBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(this.root)
            lifecycleOwner = this@StrikePoseActivity
            executePendingBindings()
        }
        init()
        binding!!.layout.tvTitle.text="Strike a Pose"
        binding!!.layout.ivBack.setOnClickListener {
            finish()
        }

    }

    override fun initArguments() {
//        implement the outputDirectory and cameraExecutor
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun initViews() {
    }

    override fun setupListener() {
        // to take photo
        binding?.btnTakePicture?.setOnClickListener {
            takePhoto()
        }
        binding?.btnSwitchCamera?.setOnClickListener {
            flipCamera()
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun loadData() {
        binding?.viewFinderPreview?.afterMeasured {
            binding?.viewFinderPreview?.setOnTouchListener { _, event ->
                return@setOnTouchListener when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        val width = binding?.viewFinderPreview?.width?.toFloat()
                        val height = binding?.viewFinderPreview?.height?.toFloat()
                        var factory: MeteringPointFactory? = null
                        if (width != null && height != null) {
                            factory = SurfaceOrientedMeteringPointFactory(width, height)
                        }

                        val autoFocusPoint =
                            factory?.createPoint(event.x, event.y) // to create point
                        try {
                            autoFocusPoint?.let {
                                FocusMeteringAction.Builder(it, FocusMeteringAction.FLAG_AE)
                                    .apply {
                                        //focus only when the user tap the preview
                                        disableAutoCancel()
                                    }.build()
                            }?.let {
                                camera?.cameraControl?.startFocusAndMetering(
                                    it
                                )
                            }
                        } catch (e: CameraInfoUnavailableException) {
                            Log.d("ERROR", "cannot access camera", e)
                        }
                        true
                    }

                    else -> false // Unhandled event.
                }
            }
        }

        // Request Camera Permission
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    private fun flipCamera() {
        if (lensFacing === CameraSelector.DEFAULT_FRONT_CAMERA)
            lensFacing = CameraSelector.DEFAULT_BACK_CAMERA
        else if (lensFacing === CameraSelector.DEFAULT_BACK_CAMERA)
            lensFacing = CameraSelector.DEFAULT_FRONT_CAMERA
        startCamera()
    }

    //Implement Preview use case
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            //a singleton Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            //viewfinder is used to let the user preview the photo they will be taking. can  viewfinder using the CameraX Preview class.
            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding?.viewFinderPreview?.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build() // use cases
//            videoCapture = VideoCapture.Builder().build()


            // Select back camera as a default
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA




            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(this, lensFacing, preview, imageCapture);

//                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                // auto focus functionality in every x seconds
                binding?.viewFinderPreview?.afterMeasured {
                    val autoFocusPoint = SurfaceOrientedMeteringPointFactory(1f, 1f)
                        .createPoint(.5f, .5f)
                    try {
                        val autoFocusAction = FocusMeteringAction.Builder(
                            autoFocusPoint,
                            FocusMeteringAction.FLAG_AF
                        ).apply {
                            //start auto-focusing after 2 seconds
                            setAutoCancelDuration(2, java.util.concurrent.TimeUnit.SECONDS)
                        }.build()
                        camera?.cameraControl?.startFocusAndMetering(autoFocusAction)
                    } catch (e: CameraInfoUnavailableException) {
                        Log.d("ERROR", "cannot access camera", e)
                    }
                }



            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

    }

    //    defined a configuration object that is used to instantiate the actual use case object
    //    Implement ImageCapture use case
    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        // it the use case is null exit to the function
//       This will be null If you tap the photo button before image capture is set up. Without the return statement, the app would crash if it was null.
        val imageCapture = imageCapture ?: return

        //create timestamp photo file to hold image(for unique)
        val photoFile = File(outputDirectory, SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg")

        // create outputOption object which contains file + metadata
        //  OutputFileOptions object where u can specify things about how your output would be
        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // flash on when image click
        flash?.let { camera?.cameraControl?.enableTorch(it) } // enable torch


        // set up image capture listener , which is triggered after photo has been taken
        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(this),
            object :ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    Log.e("@@savedUri",""+savedUri)
                    val msg = "Photo capture succeeded: $savedUri"
                    savedFileName = photoFile.name
                    if (savedUri!=null)
                    {

                        val intent1 = Intent(this@StrikePoseActivity, PreviewFrameActivity::class.java)
                        intent1.putExtra("gender", intent.getStringExtra("gender").toString())
                        intent1.putExtra("savedFileName", savedFileName)
                        intent1.putExtra("imageuri", savedUri.toString())
                        intent1.putExtra("template", intent.getIntExtra("template",0))
                        startActivity(intent1)

                    }
                //    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                 //   Log.d(TAG, msg)
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

            })

    }

}