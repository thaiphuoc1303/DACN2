package com.vision.exercise.vision.ui.camera

import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.common.MlKitException
import com.vision.exercise.databinding.ActivityTestCameraBinding
import com.vision.exercise.vision.base.BaseActivity
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.ViewModelProviderFactory
import com.vision.exercise.vision.demo.VisionImageProcessor
import com.vision.exercise.vision.demo.kotlin.posedetector.PoseDetectorProcessor
import com.vision.exercise.vision.util.PreferenceUtils
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TestCameraXActivity : BaseActivity<ActivityTestCameraBinding, TestCameraXViewModel>() {
    override fun getLazyBinding() = lazy { ActivityTestCameraBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<TestCameraXViewModel> {
        ViewModelProviderFactory(
            BaseInput.NormalInput(application)
        )
    }

    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraProvider: ProcessCameraProvider
    private val lensFacing = CameraSelector.LENS_FACING_BACK
    private lateinit var cameraSelector: CameraSelector
    private lateinit var imageProcessor: VisionImageProcessor
    private var previewUseCase: Preview? = null

    override fun setupInit() {
        initView()
        initCamera()
        observe()
    }

    private fun observe() {
        viewModel.getProcessCameraProvider().observe(this) {
            cameraProvider = it
            bindAllCameraUseCases()
        }
    }

    private fun initCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor()

        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProvider = cameraProviderFuture.get()
        cameraProviderFuture.addListener({
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            val imageCaptureBuilder = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)

            imageCapture = imageCaptureBuilder.build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
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

        // paste
        val poseDetectorOptions =
            PreferenceUtils.getPoseDetectorOptionsForLivePreview(this)
        val shouldShowInFrameLikelihood =
            PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this)
        val visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this)
        val rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this)
        val runClassification = true
        imageProcessor = PoseDetectorProcessor(
            this,
            poseDetectorOptions,
            shouldShowInFrameLikelihood,
            visualizeZ,
            rescaleZ,
            runClassification,
            true
        ) {
            it.pose

        }


        val builder = ImageAnalysis.Builder()
        val targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing)
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution)
        }
        val analysisUseCase = builder.build()

        var needUpdateGraphicOverlayImageSourceInfo = true

        analysisUseCase.setAnalyzer(
            // imageProcessor.processImageProxy will use another thread to run the detection underneath,
            // thus we can just runs the analyzer itself on main thread.
            ContextCompat.getMainExecutor(this)
        ) { imageProxy: ImageProxy ->
            if (needUpdateGraphicOverlayImageSourceInfo) {
                val isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                if (rotationDegrees == 0 || rotationDegrees == 180) {
                    binding.graphicOverlay.setImageSourceInfo(
                        imageProxy.width,
                        imageProxy.height,
                        isImageFlipped
                    )
                } else {
                    binding.graphicOverlay.setImageSourceInfo(
                        imageProxy.height,
                        imageProxy.width,
                        isImageFlipped
                    )
                }
                needUpdateGraphicOverlayImageSourceInfo = false
            }
            try {
                imageProcessor.processImageProxy(imageProxy, binding.graphicOverlay)
            } catch (e: MlKitException) {
                Log.e("TAG", "Failed to process image. Error: " + e.localizedMessage)
                Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        cameraProvider.bindToLifecycle(/* lifecycleOwner = */ this, cameraSelector, analysisUseCase)
    }

    private fun initView() {
//        TODO("Not yet implemented")
    }


    public override fun onResume() {
        super.onResume()
        bindAllCameraUseCases()
    }

    override fun onPause() {
        super.onPause()

        imageProcessor.run { this.stop() }
    }

    public override fun onDestroy() {
        super.onDestroy()
        imageProcessor.run { this.stop() }
    }

    private fun bindAllCameraUseCases() {
        cameraProvider.unbindAll()
        bindPreviewUseCase()
        startCamera()
    }

    private fun bindPreviewUseCase() {
        if (!PreferenceUtils.isCameraLiveViewportEnabled(this)) {
            return
        }
        if (previewUseCase != null) {
            cameraProvider.unbind(previewUseCase)
        }

        val builder = Preview.Builder()
        val targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing)
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution)
        }
        previewUseCase = builder.build()
        previewUseCase!!.setSurfaceProvider(binding.previewView.surfaceProvider)
        cameraProvider.bindToLifecycle(/* lifecycleOwner= */ this, cameraSelector, previewUseCase)
    }
}