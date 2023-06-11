package com.vision.exercise.vision.ui.pose

import com.vision.exercise.R
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.gms.common.annotation.KeepName
import com.google.mlkit.common.MlKitException
import com.vision.exercise.databinding.ActivityVisionCameraxLivePreviewBinding
import com.vision.exercise.vision.base.BaseActivity
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.common.EXERCISE_KEY
import com.vision.exercise.vision.demo.VisionImageProcessor
import com.vision.exercise.vision.demo.kotlin.posedetector.PoseDetectorProcessor
import com.vision.exercise.vision.extension.dpToPx
import com.vision.exercise.vision.extension.parcelableExtra
import com.vision.exercise.vision.util.PreferenceUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

@KeepName
class CameraXLivePreviewActivity :
    BaseActivity<ActivityVisionCameraxLivePreviewBinding, CameraXLiveViewModel>(),
    CompoundButton.OnCheckedChangeListener {

    private var cameraProvider: ProcessCameraProvider? = null
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null
    private var imageProcessor: VisionImageProcessor? = null
    private var needUpdateGraphicOverlayImageSourceInfo = false
    private var selectedModel = POSE_DETECTION
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var cameraSelector: CameraSelector? = null
    private var exoPlayer: ExoPlayer? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.camera_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_change) {
            resizeLayout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun resizeLayout() {
        binding.apply {
            val marginLeft = 200.dpToPx(this@CameraXLivePreviewActivity)
            val margin = (8).dpToPx(this@CameraXLivePreviewActivity)
            cardView.setPadding(marginLeft, margin, margin, marginLeft)
            cardView.elevation = 5f
        }
    }

    private val playbackProgressObservable = Observable.interval(
        200,
        TimeUnit.MILLISECONDS,
        AndroidSchedulers.mainThread()
    )
        .map { exoPlayer?.currentPosition ?: 0L }
    private val bufferProgressObservable = Observable.interval(
        500,
        TimeUnit.MILLISECONDS,
        AndroidSchedulers.mainThread()
    )
        .map { exoPlayer?.bufferedPosition ?: 0L }

    override fun getLazyBinding() =
        lazy { ActivityVisionCameraxLivePreviewBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() =
        lazy { CameraXLiveViewModel(BaseInput.ExerciseDetailInput(
            application,
            intent.parcelableExtra(EXERCISE_KEY)
        )) }

    override fun setupInit() {
        initActionBar()
        binding.previewView.isVisible = false
        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        binding.facingSwitch.setOnCheckedChangeListener(this)
        observe()

        // TODO
//        initializePlayer()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    private fun observe() {
        viewModel.getProcessCameraProvider().observe(this) {
            cameraProvider = it
            bindAllCameraUseCases()
        }
        viewModel.getCount().observe(this){
            val text = "Count: $it"
            binding.tvCount.text = text
        }

        viewModel.getExercise().observe(this) {

        }
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putString(STATE_SELECTED_MODEL, selectedModel)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (cameraProvider == null) {
            return
        }
        val newLensFacing =
            if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                CameraSelector.LENS_FACING_BACK
            } else {
                CameraSelector.LENS_FACING_FRONT
            }
        val newCameraSelector = CameraSelector.Builder().requireLensFacing(newLensFacing).build()
        try {
            if (cameraProvider!!.hasCamera(newCameraSelector)) {
                lensFacing = newLensFacing
                cameraSelector = newCameraSelector
                bindAllCameraUseCases()
                return
            }
        } catch (e: CameraInfoUnavailableException) {
            // Falls through
        }
        Toast.makeText(
            applicationContext,
            "This device does not have lens with facing: $newLensFacing",
            Toast.LENGTH_SHORT
        )
            .show()
    }

    public override fun onResume() {
        super.onResume()
        bindAllCameraUseCases()
    }

    override fun onPause() {
        super.onPause()

        imageProcessor?.run { this.stop() }
    }

    public override fun onDestroy() {
        super.onDestroy()
        imageProcessor?.run { this.stop() }
    }

    private fun bindAllCameraUseCases() {
        if (cameraProvider != null) {
            // As required by CameraX API, unbinds all use cases before trying to re-bind any of them.
            cameraProvider!!.unbindAll()
            bindPreviewUseCase()
            bindAnalysisUseCase()
        }
    }

    private fun bindPreviewUseCase() {
        if (!PreferenceUtils.isCameraLiveViewportEnabled(this)) {
            return
        }
        if (cameraProvider == null) {
            return
        }
        if (previewUseCase != null) {
            cameraProvider!!.unbind(previewUseCase)
        }

        val builder = Preview.Builder()
        val targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing)
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution)
        }
        previewUseCase = builder.build()
        previewUseCase!!.setSurfaceProvider(binding.previewView.surfaceProvider)
        cameraProvider!!.bindToLifecycle(/* lifecycleOwner = */ this,
            cameraSelector!!,
            previewUseCase
        )
    }

    private fun bindAnalysisUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (analysisUseCase != null) {
            cameraProvider!!.unbind(analysisUseCase)
        }
        if (imageProcessor != null) {
            imageProcessor!!.stop()
        }
        val poseDetectorOptions =
            PreferenceUtils.getPoseDetectorOptionsForLivePreview(this)
        val shouldShowInFrameLikelihood =
            PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this)
        val visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this)
        val rescaleZ =
            PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this)
        val runClassification = true
        imageProcessor = PoseDetectorProcessor(
            this,
            poseDetectorOptions,
            shouldShowInFrameLikelihood,
            visualizeZ,
            rescaleZ,
            runClassification,
            /* isStreamMode = */ true
        ) {
            if(it.classificationResult.size > 0 && it.classificationResult[0].isNotEmpty()) {
                Log.e(TAG, it.classificationResult.toString())
                binding.tvPose.text = it.classificationResult[0]
                viewModel.setPose(it.classificationResult[0])
            }
//            Log.e(TAG, "${it.classificationResult[1]}")
            // TODO: Update UI
            // Process pose result
        }

        val builder = ImageAnalysis.Builder()
        val targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing)
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution)
        }
        analysisUseCase = builder.build()

        needUpdateGraphicOverlayImageSourceInfo = true

        analysisUseCase?.setAnalyzer(
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
                imageProcessor!!.processImageProxy(imageProxy, binding.graphicOverlay)
            } catch (e: MlKitException) {
                Log.e(TAG, "Failed to process image. Error: " + e.localizedMessage)
                Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
        cameraProvider!!.bindToLifecycle(
            /* lifecycleOwner = */
            this,
            cameraSelector!!,
            analysisUseCase
        )
    }

    private fun initializePlayer(url: String) {
        val videoUrl = url

        val trackSelector = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        exoPlayer = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()
            .also { player ->
                val videoItem = MediaItem.Builder()
                    .setUri(Uri.parse(videoUrl))
                    .setMimeType(MimeTypes.APPLICATION_MP4)
                    .build()
                player.setMediaItem(videoItem)
                player.playWhenReady = true
//                player.seekTo(0, viewModel.playbackPosition)
                player.addListener(playbackStateListener())
                player.prepare()
                binding.playerView.player = player
            }
    }
    private fun playbackStateListener() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                ExoPlayer.STATE_IDLE -> {

                }
                ExoPlayer.STATE_BUFFERING -> {

                }
                ExoPlayer.STATE_READY -> {

                }
                ExoPlayer.STATE_ENDED -> {

                }
                else -> {
                    //no-ops
                }
            }
        }

        override fun onEvents(player: Player, events: Player.Events) {

        }
    }

    companion object {
        private const val TAG = "CameraXLivePreview"
        private const val POSE_DETECTION = "Pose Detection"
        private const val STATE_SELECTED_MODEL = "selected_model"
    }
}
