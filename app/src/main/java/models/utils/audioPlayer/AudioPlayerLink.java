package models.utils.audioPlayer;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class AudioPlayerLink extends AbstractAudioPlayer {

    private String linkAudio;

    public AudioPlayerLink(String linkAudio) {
        this.mediaPlayer = new MediaPlayer();
        this.linkAudio = linkAudio;
    }

    public String getLinkAudio() {
        return linkAudio;
    }

    public void playAudio() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(linkAudio);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e("AudioPlayerLink.playAudio()", "Errore nel setting Datasource o prepare() dell'audio");
        }
    }

}
