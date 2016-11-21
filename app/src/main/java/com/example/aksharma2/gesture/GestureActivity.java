package com.example.aksharma2.gesture;//package pack.GestureApp;

import android.app.Activity;
import android.gesture.*;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import pack.GestureApp.R;

public class GestureActivity extends Activity {

    private GestureLibrary gesture_lib;
    private static final String TAG = "GestureActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        openOptionsMenu();
       gesture_lib = GestureLibraries.fromFile(getExternalFilesDir(null) + "/" + "gesture.txt");
       gesture_lib.load();

        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
        gestures.addOnGesturePerformedListener(gestureListener);
        gestures.setGestureStrokeAngleThreshold(90.0f); // to distinguish between vertical and horizontal lines
    }

    /**
     * our gesture listener
     */
    private OnGesturePerformedListener gestureListener = new OnGesturePerformedListener() {
        @Override
        public void onGesturePerformed(GestureOverlayView gestureView,
                                       Gesture gesture) {

            ArrayList<Prediction> predictions = gesture_lib.recognize(gesture);

            // one prediction needed
            if (predictions.size() > 0) {         // returns a length of predicted gestures
                Prediction prediction = predictions.get(0);

                // checking prediction
                if (prediction.score > 1.0) {      // very low threshold -> will lead to v high false positives
                    // and action
                    Toast.makeText(GestureActivity.this, prediction.name, Toast.LENGTH_SHORT).show();

                    double score = prediction.score;
                    String score_string = "Similarity score with " + prediction.name + ": " + new Double(score).toString();
                    Log.d(TAG, score_string);

                } else {
                    Log.d(TAG, "No gesture recognised");
                    Toast.makeText(GestureActivity.this,"No gesture recognised", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}