package com.vision.exercise.vision.ui.exercise_details

import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.MimeTypes
import com.vision.exercise.databinding.ActivityExerciseDetailBinding
import com.vision.exercise.vision.base.BaseActivity
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.ViewModelProviderFactory
import com.vision.exercise.vision.common.EXERCISE_KEY
import com.vision.exercise.vision.common.REPEAT_KEY
import com.vision.exercise.vision.extension.parcelableExtra
import com.vision.exercise.vision.ui.exercise.ExerciseActivity

class ExerciseDetailActivity: BaseActivity<ActivityExerciseDetailBinding, ExerciseDetailViewModel>() {
    private var exoPlayer: ExoPlayer? = null


    override fun getLazyBinding() = lazy { ActivityExerciseDetailBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<ExerciseDetailViewModel> {
        ViewModelProviderFactory(BaseInput.ExerciseDetailInput(application, intent.parcelableExtra(EXERCISE_KEY)))
    }

    override fun setupInit() {
        initView()
        initListener()
        observe()
    }

    private fun initListener() {
        binding.btnStart.setOnClickListener {
            startExercise()
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun startExercise() {
        Intent(this, ExerciseActivity::class.java).apply {
            val repeat = binding.npRepetition.value
            putExtra(EXERCISE_KEY, viewModel.getExercise().value)
            putExtra(REPEAT_KEY, repeat)
            startActivity(this)
        }
    }

    private fun initView() {
//        TODO("Not yet implemented")
    }

    private fun initializePlayer(videoUrl: String) {
        val trackSelector = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                3000, // Min buffer duration
                10000, // Max buffer duration
                1500, // Buffer for playback duration
                2000 // Buffer for playback after rebuffer
            )
            .build()
        exoPlayer = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
//            .setLoadControl(loadControl)
            .build()
            .also { player ->
                binding.playerView.player = player
                player.playWhenReady = true
                player.repeatMode = Player.REPEAT_MODE_ONE
                player.addListener(playbackStateListener())
                val videoItem = MediaItem.Builder()
                    .setUri(Uri.parse(videoUrl))
                    .setMimeType(MimeTypes.APPLICATION_MP4)
                    .build()
                player.setMediaItem(videoItem)
                player.prepare()
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
//                    exoPlayer?.seekTo(0)
//                    exoPlayer?.let {
//                        if (!it.isPlaying) {
//                            it.play()
//                        }
//                    }
                }
                else -> {
                    //no-ops
                }
            }
        }

        override fun onEvents(player: Player, events: Player.Events) {

        }
    }

    private fun observe() {
        viewModel.getExercise().observe(this) {
            binding.apply {
                tvExerciseName.text = it.name
                tvDescription.text = it.description
                tvLevel.text = String.format("Easy | %d Calories Burn", it.calo)
            }
            initializePlayer(it.videoUrl)
        }
    }
}
