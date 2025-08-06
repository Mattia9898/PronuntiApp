package models.utils.audioPlayer;


import android.media.MediaPlayer;
import android.content.Context;


public class AudioPlayerRaw extends AbstractAudioPlayer {

    private int audioPlayerRaw;


    public void playAudio() {
        mediaPlayer.start();
    }

    public AudioPlayerRaw(Context context, int audioPlayerRaw) {
        this.mediaPlayer = MediaPlayer.create(context, audioPlayerRaw);
        this.audioPlayerRaw = audioPlayerRaw;
    }


}
