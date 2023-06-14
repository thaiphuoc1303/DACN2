package com.vision.exercise.vision.demo.kotlin.posedetector.classification;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.google.common.base.Preconditions;
import com.google.mlkit.vision.pose.Pose;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PoseClassifierProcessor {
  private static final String TAG = "PoseClassifierProcessor";
  private static final String POSE_SAMPLES_FILE = "pose/fitness_pose.csv";


  public static final String PUSHUPS_CLASS = "pushups_down";
  public static final String SQUATS_CLASS = "squats_down";
  private static final String[] POSE_CLASSES = {
    PUSHUPS_CLASS, SQUATS_CLASS
  };

  private final boolean isStreamMode;

  private EMASmoothing emaSmoothing;
  private List<RepetitionCounter> repCounters;
  private PoseClassifier poseClassifier;
  private String lastRepResult;

  @WorkerThread
  public PoseClassifierProcessor(Context context, boolean isStreamMode) {
    Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
    this.isStreamMode = isStreamMode;
    if (isStreamMode) {
      emaSmoothing = new EMASmoothing();
      repCounters = new ArrayList<>();
      lastRepResult = "";
    }
    loadPoseSamples(context);
  }

  private void loadPoseSamples(Context context) {
    List<PoseSample> poseSamples = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(context.getAssets().open(POSE_SAMPLES_FILE)));
      String csvLine = reader.readLine();
      while (csvLine != null) {
        // If line is not a valid {@link PoseSample}, we'll get null and skip adding to the list.
        PoseSample poseSample = PoseSample.getPoseSample(csvLine, ",");
        if (poseSample != null) {
          poseSamples.add(poseSample);
        }
        csvLine = reader.readLine();
      }
    } catch (IOException e) {
      Log.e(TAG, "Error when loading pose samples.\n" + e);
    }
    poseClassifier = new PoseClassifier(poseSamples);
    if (isStreamMode) {
      for (String className : POSE_CLASSES) {
        repCounters.add(new RepetitionCounter(className));
      }
    }
  }


  @WorkerThread
  public List<String> getPoseResult(Pose pose) {
    Preconditions.checkState(Looper.myLooper() != Looper.getMainLooper());
    List<String> result = new ArrayList<>();
    ClassificationResult classification = poseClassifier.classify(pose);


    if (!pose.getAllPoseLandmarks().isEmpty()) {
      String maxConfidenceClass = classification.getMaxConfidenceClass();
      float confidence = classification.getClassConfidence(maxConfidenceClass) / poseClassifier.confidenceRange();

      result.add(maxConfidenceClass);
      result.add(Float.toString(confidence));
    }

    return result;
  }

  class PoseResult {
    private int type;
    private float confidence;

    public PoseResult(int type, float confidence) {
      this.type = type;
      this.confidence = confidence;
    }

    public int getType() {
      return type;
    }

    public void setType(int type) {
      this.type = type;
    }

    public float getConfidence() {
      return confidence;
    }

    public void setConfidence(float confidence) {
      this.confidence = confidence;
    }
  }

}
