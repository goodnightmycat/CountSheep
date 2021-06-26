package com.example.countsheep;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class MediaPlayManager {

    private static MediaPlayManager mManager;

    private MediaPlayer mPlayer;

    private MediaPlayManager() {

    }

    public void play(Context context, int resId) {
        try {
            stop();
            mPlayer = null;
            mPlayer = MediaPlayer.create(context, resId);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mPlayer != null) {
                        mPlayer.release();
                        mPlayer = null;
                    }
                }
            });
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MediaPlayManager getInstance() {
        if (mManager == null) {
            mManager = new MediaPlayManager();
        }
        return mManager;
    }

    public void stop() {
        try {
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                }
                mPlayer.reset();
                mPlayer.release();
                mPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
