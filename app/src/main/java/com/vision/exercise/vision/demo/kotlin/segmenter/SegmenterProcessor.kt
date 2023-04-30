package com.vision.exercise.vision.demo.kotlin.segmenter

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.Segmentation
import com.google.mlkit.vision.segmentation.SegmentationMask
import com.google.mlkit.vision.segmentation.Segmenter
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import com.vision.exercise.vision.demo.kotlin.VisionProcessorBase

/** A processor to run Segmenter.  */
class SegmenterProcessor :
  VisionProcessorBase<SegmentationMask> {
  private val segmenter: Segmenter

  constructor(context: Context) : this(context, /* isStreamMode= */ true)

  constructor(
    context: Context,
    isStreamMode: Boolean
  ) : super(
    context
  ) {
    val optionsBuilder = SelfieSegmenterOptions.Builder()
    optionsBuilder.setDetectorMode(
      if(isStreamMode) SelfieSegmenterOptions.STREAM_MODE
      else SelfieSegmenterOptions.SINGLE_IMAGE_MODE
    )
    if (com.vision.exercise.vision.demo.preference.PreferenceUtils.shouldSegmentationEnableRawSizeMask(context)) {
      optionsBuilder.enableRawSizeMask()
    }

    val options = optionsBuilder.build()
    segmenter = Segmentation.getClient(options)
    Log.d(TAG, "SegmenterProcessor created with option: " + options)
  }

  override fun detectInImage(image: InputImage): Task<SegmentationMask> {
    return segmenter.process(image)
  }

  override fun onSuccess(
    segmentationMask: SegmentationMask,
    graphicOverlay: com.vision.exercise.vision.demo.GraphicOverlay
  ) {
    graphicOverlay.add(
      SegmentationGraphic(
        graphicOverlay,
        segmentationMask
      )
    )
  }

  override fun onFailure(e: Exception) {
    Log.e(TAG, "Segmentation failed: $e")
  }

  companion object {
    private const val TAG = "SegmenterProcessor"
  }
}
