package com.android.example.pruebaprueba.ui.activitys

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import com.android.example.pruebaprueba.R
import com.android.example.pruebaprueba.data.service.mindee.ImageUtils
import com.android.example.pruebaprueba.ui.viewmodels.CameraViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {
    private lateinit var previewView: PreviewView
    private lateinit var captureButton: Button
    private lateinit var selectionButton: Button
    private lateinit var outputDirectory: File
    private lateinit var loading: ProgressBar

    private val viewModel by viewModels<CameraViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_camera)

        previewView = findViewById(R.id.viewFinder)
        captureButton = findViewById(R.id.image_capture_button)
        selectionButton = findViewById(R.id.select_from_gallery_button)
        loading = findViewById(R.id.pbCamara)
        outputDirectory = ImageUtils.getOutputDirectory(this)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        captureButton.setOnClickListener {
            takePhoto()
        }

        selectionButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val inputStream = contentResolver.openInputStream(uri)
                val tempFile = File.createTempFile("selected_image", ".jpg", cacheDir)
                tempFile.outputStream().use { fileOut ->
                    inputStream?.copyTo(fileOut)
                }

                viewModel.enqueueAndObserve(image = tempFile, modelId = "6218b2d4-f5ef-46ec-86c4-6f0dcd449ead")
            }
        }
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageCapture = ImageCapture.Builder().build()
            CameraXHolder.imageCapture = imageCapture

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Toast.makeText(this, "Error inicializando cámara", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        captureButton.isEnabled = false
        selectionButton.isEnabled = false

        loading.visibility = View.VISIBLE

        val imageCapture = CameraXHolder.imageCapture ?: return
        val photoFile = File(outputDirectory, "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        applicationContext,
                        "Error al guardar la imagen",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {

                    viewModel.enqueueAndObserve(image = photoFile, modelId = "6218b2d4-f5ef-46ec-86c4-6f0dcd449ead")
                }
            }
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val REQUEST_CODE_PICK_IMAGE = 20
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}

object CameraXHolder {
    var imageCapture: ImageCapture? = null
}
