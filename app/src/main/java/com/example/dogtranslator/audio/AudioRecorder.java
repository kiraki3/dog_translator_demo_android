package com.example.dogtranslator.audio;

import java.io.File;

public interface AudioRecorder {
    void start(File outputFile);
    void stop();
}
