package com.example.dogtranslator.audio;

import java.io.File;

public interface AudioPlayer {
    void playFile(File file);
    void stop();
}
