package models.utils.audioPlayer;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayerRaw extends AbstractAudioPlayer {

    private int rawAudio;

    public AudioPlayerRaw(Context context, int rawAudio) {
        this.mediaPlayer = MediaPlayer.create(context, rawAudio);
        this.rawAudio = rawAudio;
    }

    public int getRawAudio() {
        return rawAudio;
    }

    public void playAudio() {
        mediaPlayer.start();
    }

}
