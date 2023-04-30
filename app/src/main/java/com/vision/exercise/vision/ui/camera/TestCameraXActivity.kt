package com.vision.exercise.vision.ui.camera

import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.vision.exercise.vision.base.BaseActivity
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.ViewModelProviderFactory
import com.vision.exercise.databinding.ActivityTestCameraBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TestCameraXActivity: BaseActivity<ActivityTestCameraBinding, TestCameraXViewModel>() {
    override fun getLazyBinding() = lazy { ActivityTestCameraBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<TestCameraXViewModel> { ViewModelProviderFactory(
        BaseInput.NoInput) }

    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    override fun setupInit() {
        initView()
        initCamera()
    }

    private fun initCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            val imageCaptureBuilder = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)

            imageCapture = imageCaptureBuilder.build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                // Handle errors
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun initView() {
//        TODO("Not yet implemented")
    }
}