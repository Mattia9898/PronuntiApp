package models.utils.audioPlayer;


import java.io.IOException;

import android.util.Log;
import android.media.MediaPlayer;


public class AudioPlayerLink extends AbstractAudioPlayer {

    private String audioPlayerLink;


    public AudioPlayerLink(String audioPlayerLink) {
        this.mediaPlayer = new MediaPlayer();
        this.audioPlayerLink = audioPlayerLink;
    }

    public void playAudio() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioPlayerLink);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e("AudioPlayerLink.playAudio()", "Errore nel setting Datasource o prepare() dell'audio");
        }
    }

}
