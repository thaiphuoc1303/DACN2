package com.vision.exercise.vision.ui.exercise

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.MimeTypes
import com.google.mlkit.common.MlKitException
import com.vision.exercise.databinding.ActivityExerciseBinding
import com.vision.exercise.vision.base.BaseActivity
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.ViewModelProviderFactory
import com.vision.exercise.vision.common.DEFAULT_REPEAT
import com.vision.exercise.vision.common.EXERCISE_KEY
import com.vision.exercise.vision.common.REPEAT_KEY
import com.vision.exercise.vision.demo.VisionImageProcessor
import com.vision.exercise.vision.demo.kotlin.posedetector.PoseDetectorProcessor
import com.vision.exercise.vision.extension.parcelableExtra
import com.vision.exercise.vision.model.ExerciseExt
import com.vision.exercise.vision.util.PreferenceUtils
import com.vision.exercise.vision.util.TimeUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import com.vision.exercise.R
import com.vision.exercise.vision.ui.home.HomeActivity
import com.vision.exercise.vision.widget.AlertDialog
import com.vision.exercise.vision.widget.ConfirmDialog
import com.vision.exercise.vision.widget.DoneDialog

class ExerciseActivity : BaseActivity<ActivityExerciseBinding, ExerciseViewModel>() {
    companion object{
        private const val POSE_DETECTION = "Pose Detection"
        private const val STATE_SELECTED_MODEL = "selected_model"
        private const val TAG = "ExerciseActivity"
    }

    private var finishDialog: DoneDialog? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null
    private var imageProcessor: VisionImageProcessor? = null
    private var needUpdateGraphicOverlayImageSourceInfo = false
    private var selectedModel = POSE_DETECTION
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var cameraSelector: CameraSelector? = null
    private var exoPlayer: ExoPlayer? = null
    private var isReady = false

    // override
    override fun getLazyBinding() = lazy { ActivityExerciseBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<ExerciseViewModel> {
        ViewModelProviderFactory(
            BaseInput.ExerciseInput(
                application,
                intent.parcelableExtra(EXERCISE_KEY),
                intent.getIntExtra(REPEAT_KEY, DEFAULT_REPEAT)
            )
        )
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

    override fun setupInit() {
        intent.parcelableExtra<ExerciseExt>(EXERCISE_KEY)?.let {
            initializePlayer(it.videoUrl)
        }
        initView()
        initListener()
        observe()
        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    }

    private fun initView() {
        binding.apply {
            tvTimeCountdown.text = TimeUtils.EMPTY_TIME
        }
    }

    private fun initListener() {
        binding.apply {
            btnBack.setOnClickListener {
                onBackPressed()
            }
            btnFacingSwitch.setOnClickListener {
                switchCamera()
            }
            btnPause.setOnClickListener {
                viewModel.changePause()
            }
        }
    }

    private fun switchCamera() {
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
    }

    private fun observe() {
        viewModel.getExercise().observe(this) {
            binding.apply {
                tvPose.text = it.name

            }
        }
        viewModel.getProcessCameraProvider().observe(this) {
            cameraProvider = it
            bindAllCameraUseCases()
        }
        observeCount()
        observeIsReady()
        observeTimer()
        observePause()
        observeFinish()
    }

    private fun observeFinish() {
        viewModel.getIsFinish().observe(this) {
            if (it) {
                viewModel.setPause()
                showFinishDialog()
            }
        }
    }

    private fun observePause() {
        viewModel.getIsPause().observe(this) {
            if(it) {
                binding.btnPause.text = getString(R.string.resume)
            }
            else {
                binding.btnPause.text = getString(R.string.pause)
            }
        }
    }

    private fun observeTimer() {
        viewModel.elapsedTime.observe(this) {
            binding.tvTimeCountdown.text = TimeUtils.getTimeFromSecond(it)
        }
    }

    private fun observeIsReady() {
        viewModel.getIsReady().observe(this) {
            isReady = it
            if (it) {
                viewModel.startTimer()
                exoPlayer?.let { player ->
                    if (!player.isPlaying) {
                        player.play()
                    }
                }
            }
        }
    }

    private fun observeCount() {
        viewModel.getCount().observe(this) {
            binding.tvCount.text = "$it"
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

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putString(STATE_SELECTED_MODEL, selectedModel)
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
            if (!isReady) viewModel.setIsReady(true)
            if(it.classificationResult.isNotEmpty() && it.classificationResult[0].isNotEmpty()) {
                Log.e("POSE", it.classificationResult[0])
                viewModel.checkPose(it.classificationResult[0])
            }
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

    private fun showFinishDialog() {
        finishDialog = DoneDialog.newInstance(
            getString(R.string.finish),
            getString(R.string.you_have_completed_the_exercise),
            getString(R.string.ok)
        ) {
            viewModel.insertPractice()
            Intent(this, HomeActivity::class.java).apply {
                finishAffinity()
                startActivity(this)
            }
        }
        finishDialog?.let {
            if (!it.isAdded) {
                it.show(supportFragmentManager, AlertDialog.TAG)
            }
        }
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
                player.playWhenReady = false
                player.repeatMode = Player.REPEAT_MODE_ONE
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
                    exoPlayer?.let {
                        if (!it.isPlaying) {
                            it.play()
                        }
                    }
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

    override fun onBackPressed() {
        finishDialog?.let {
            if (it.isAdded) {
                it.closeDialog()
                return
            }
        }
        super.onBackPressed()
    }
}