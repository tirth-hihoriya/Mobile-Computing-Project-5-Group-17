package com.example.guardianangel;

// This class computes the Heart rate

import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraWorking {

    public static String TAG = "Debug";

    Algorithms algorithms = new Algorithms();

    public String measure_heart_rate(String videoPath, String videoName) throws IOException {
        Log.d("OPENCV",String.valueOf(OpenCVLoader.initDebug()));
        VideoCapture videoCapture = new VideoCapture();
        Log.i("CameraWorking", "CameraWorking started");
        if (new File(videoPath + videoName).exists()) {
            videoCapture.open(videoPath + videoName);
            Log.i("CameraWorking", "CameraWorking video path exists");
            if (videoCapture.isOpened()) {
                Log.i("CameraWorking", "CameraWorking videoCapture is opened");
                List<Double> extremes = new ArrayList<Double>();
                List<Double> list = new ArrayList<Double>();
                List<Double> new_list = new ArrayList<Double>();
                Mat current_frame = new Mat();
                Mat next_frame = new Mat();
                Mat diff_frame = new Mat();

                int video_length = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_COUNT);
                int fps = (int) videoCapture.get(Videoio.CAP_PROP_FPS);


                videoCapture.read(current_frame);
                Log.i("CameraWorking", "CameraWorking videoCapture.read");
                for (int k = 0; k < video_length - 1; k++) {
                    Log.i("CameraWorking", "CameraWorking videoCapture.read for");
                    videoCapture.read(next_frame);
                    Core.subtract(next_frame, current_frame, diff_frame);
                    next_frame.copyTo(current_frame);
                    list.add(Core.mean(diff_frame).val[0] + Core.mean(diff_frame).val[1] + Core.mean(diff_frame).val[2]);
                }

                for (int i = 0; i < (list.size() / 5) - 1; i++) {
                    List<Double> sublist = list.subList(i * 5, (i + 1) * 5);
                    double sum = 0.0;
                    for (int j = 0; j < sublist.size(); j++) {
                        sum += sublist.get(j);
                    }

                    new_list.add(sum / 5);
                }

                int mov_period = 50;

                List<Double> avg_data = algorithms.calcMovingAvg(mov_period, new_list);
                Log.i("CameraWorking", "CameraWorking avg_data");
                int peakCounts = algorithms.countZeroCrossings(avg_data);

                double fpsSec = (video_length / fps);
                double count_heart_rate = (peakCounts / 2) * (60) / fpsSec;
                Log.i("CameraWorking", "CameraWorking count_heart_rate "+String.valueOf(count_heart_rate));
                return "" + (int) count_heart_rate;

            } else {
                return "";
            }
        } else {

            return "";
        }
    }
}
