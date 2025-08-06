package models.utils.audioPlayer;


import android.media.MediaPlayer;


public abstract class AbstractAudioPlayer {

    protected MediaPlayer mediaPlayer;


    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void stopAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }


}
