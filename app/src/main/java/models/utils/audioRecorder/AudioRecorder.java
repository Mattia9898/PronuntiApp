package models.utils.audioRecorder;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioRecorder {

    private static final int SAMPLE_RATE = 16000;

    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;

    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);

    private AudioRecord audioRecord;
    private boolean isRecording = false;

    private File audioFile;

    @SuppressLint("MissingPermission")
    public AudioRecorder( File audioFile) {
        this.audioFile = audioFile;
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);
    }

    public File getAudioFile() {
        return audioFile;
    }

    public void startRecording() {
        audioRecord.startRecording();
        isRecording = true;
        byte[] audioBuffer = new byte[BUFFER_SIZE];

        new Thread(() -> {
            try {
                FileOutputStream outputStream = new FileOutputStream(audioFile);
                while (isRecording) {
                    int bytesRead = audioRecord.read(audioBuffer, 0, BUFFER_SIZE);
                    outputStream.write(audioBuffer, 0, bytesRead);
                }
                outputStream.close();
            } catch (IOException e) {
                Log.e("AudioRecorder.startRecording()", "Errore nella scrittura del file audio", e);
            }
        }).start();
    }

    public void stopRecording() {
        if (!isRecording)
            return;

        isRecording = false;
        audioRecord.stop();
    }

}

