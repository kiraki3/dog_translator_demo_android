package com.example.dogtranslator.audio;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;

public class AudioRecorderImpl implements AudioRecorder {

    private Context context;
    private MediaRecorder recorder;


    public AudioRecorderImpl(Context context) {
        this.context = context;
    }

    private MediaRecorder createRecorder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return new MediaRecorder(context);
        } else {
            return new MediaRecorder();
        }
    }

    @Override
    public void start(File outputFile) {
        MediaRecorder record = createRecorder();
        record.setAudioSource(MediaRecorder.AudioSource.MIC);
        record.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        record.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            record.setOutputFile(new FileOutputStream(outputFile).getFD());
            record.prepare();
            record.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        recorder = record;
    }

    @Override
    public void stop() {
        recorder.stop();
        recorder.reset();
        recorder = null;
    }
}
