package models.utils.audioPlayer;

import android.media.MediaPlayer;

public abstract class AbstractAudioPlayer {

    protected MediaPlayer mediaPlayer;

    public void stopAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

}
