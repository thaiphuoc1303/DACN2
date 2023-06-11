package com.vision.exercise.vision.ui.camera

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.mlkit.vision.common.InputImage
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.BaseViewModel
import java.util.concurrent.ExecutionException

class TestCameraXViewModel(private val input: BaseInput.NormalInput) : BaseViewModel(input) {
    private var cameraProviderLiveData = MutableLiveData<ProcessCameraProvider>()

    fun getProcessCameraProvider(): LiveData<ProcessCameraProvider> {
        cameraProviderLiveData = MutableLiveData()
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(input.application)
        cameraProviderFuture.addListener(
            {
                try {
                    cameraProviderLiveData.setValue(cameraProviderFuture.get())
                } catch (e: ExecutionException) {
                    // Handle any errors (including cancellation) here.
                    Log.e(
                        "VIEW MODEL ERROR",
                        "Unhandled exception",
                        e
                    )
                } catch (e: InterruptedException) {
                    Log.e(
                        "VIEW MODEL ERROR",
                        "Unhandled exception",
                        e
                    )
                }
            },
            ContextCompat.getMainExecutor(input.application)
        )
        return cameraProviderLiveData
    }
}

private class YourImageAnalyzer : ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // TODO: Pass image to an ML Kit Vision API
            // ...
        }
    }
}