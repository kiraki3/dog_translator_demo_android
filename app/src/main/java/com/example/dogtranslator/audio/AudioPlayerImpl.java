package com.example.dogtranslator.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import java.io.File;

public class AudioPlayerImpl implements AudioPlayer {

    private Context context;
    private MediaPlayer player;

    public AudioPlayerImpl(Context context) {
        this.context = context;
    }
    @Override
    public void playFile(File file) {
        MediaPlayer play = MediaPlayer.create(context, Uri.fromFile(file));
        player = play;
        play.start();
    }

    @Override
    public void stop() {
        player.stop();
        player.release();
        player = null;
    }
}
