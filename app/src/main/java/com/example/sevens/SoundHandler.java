package com.example.sevens;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

public class SoundHandler {


    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 2;
    final int MUSIC_POOL_MAX = 2;
    private static MediaPlayer mediaPlayer;
    private static SoundPool soundPool;
    private static SoundPool musicPool;
    private float volume = 1f;
    private static int jumpSound;

    private boolean isBackSoundPlaying;

    public SoundHandler(Context context) {

        // SoundPool is deprecated in API level 21. (Lollipop)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();
            musicPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();

        } else {
            soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
            musicPool = new SoundPool(MUSIC_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        }



        jumpSound = soundPool.load(context, R.raw.snd_bubble_bue, 1);

        mediaPlayer = MediaPlayer.create(context, R.raw.bamboo);
        mediaPlayer.setLooping(true);
        isBackSoundPlaying = false;

    }

    public void playJumpSound() {
        if (soundPool == null)
            return;
        soundPool.play(jumpSound, SettingsHandler.getSoundVolume(),SettingsHandler.getSoundVolume(), 1, 0, 1.0f);
    }

    public void playPuzzleRotateSound() {
        if (soundPool == null)
            return;
    }

    public void playPuzzleTouchSound() {
        if (soundPool == null)
            return;
    }

    public void playPuzzleDropSound() {
        if (soundPool == null)
            return;
    }

    public void playMergerSound() {
        if (soundPool == null)
            return;
    }

    public void playExplosionSound() {
        if (soundPool == null)
            return;
    }


    public void pauseAllSound() {
        if (soundPool == null)
            return;
        soundPool.autoPause();
    }

    public void resumeAllSound() {
        if (soundPool == null)
            return;
        soundPool.autoResume();
    }

    public void pauseAllMusic() {
        if (musicPool == null)
            return;
        musicPool.autoPause();
    }

    public void resumeAllMusic() {
        if (musicPool == null)
            return;
        musicPool.autoResume();
    }


    public void playBackSound() {
        if (!isBackSoundPlaying) {
            System.out.println("PLAY");

            mediaPlayer.start();
            mediaPlayer.setLooping(true);
            isBackSoundPlaying = true;

        }
    }

    public void stopBackSound() {
        if (isBackSoundPlaying) {
            System.out.println("STOP");
            mediaPlayer.stop();
            isBackSoundPlaying = false;
        }
    }

    public void resumeBackSound() {
        if (!isBackSoundPlaying) {
            System.out.println("RESUME");

            mediaPlayer.start();
            isBackSoundPlaying = true;
        }
    }

    public void pauseBackSound() {
        if (isBackSoundPlaying) {
            System.out.println("PAUSE");

            mediaPlayer.pause();
            isBackSoundPlaying = false;
        }
    }

    public void stopAllSound()
    {
        if (soundPool == null)
            return;
        for(int i = 1; i < SOUND_POOL_MAX; i++)
        {
            soundPool.stop(i);
        }
    }

    public void stopAllMusic() {
        if (musicPool == null)
            return;

        for (int i = 1; i < MUSIC_POOL_MAX; i++) {
            musicPool.stop(i);
        }
    }

    public void resetSoundVolume()
    {
        if (soundPool == null)
            return;
        for(int i = 1; i < SOUND_POOL_MAX; i++)
        {
            soundPool.setVolume(1,  SettingsHandler.getSoundVolume(), SettingsHandler.getSoundVolume());
        }
    }

    public void resetMusicVolume()
    {
        mediaPlayer.setVolume(SettingsHandler.getMusicVolume(), SettingsHandler.getMusicVolume());
        if (musicPool != null) {

            for (int i = 1; i < MUSIC_POOL_MAX; i++) {
                musicPool.setVolume(1, SettingsHandler.getMusicVolume(), SettingsHandler.getMusicVolume());
            }
        }

    }

    public void stop()
    {
        stopBackSound();
        stopAllMusic();
        stopAllSound();
        release();
    }

    public void pause()
    {
        pauseBackSound();
        pauseAllMusic();
        pauseAllSound();
    }

    public void resume()
    {
        resumeBackSound();
        resumeAllMusic();
        resumeAllSound();
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        if (musicPool != null) {
            musicPool.release();
            musicPool = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}


