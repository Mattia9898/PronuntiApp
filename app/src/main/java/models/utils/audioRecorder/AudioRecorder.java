package models.utils.audioRecorder;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

import android.annotation.SuppressLint;

import android.util.Log;
import android.media.MediaRecorder;
import android.media.AudioRecord;
import android.media.AudioFormat;
import android.content.Context;


public class AudioRecorder {


    private static final int FORMATO_AUDIO = AudioFormat.ENCODING_PCM_16BIT;

    private static final int CANALE_CONFIGURAZIONE = AudioFormat.CHANNEL_IN_MONO;

    private static final int SAMPLE_RATE = 16000;

    private static final int AUDIO_BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CANALE_CONFIGURAZIONE, FORMATO_AUDIO);

    private boolean onRegistrazione = false;

    private File fileAudioRecorder;

    private AudioRecord audioRecord;


    @SuppressLint("MissingPermission")
    public AudioRecorder(File fileAudioRecorder) {
        this.fileAudioRecorder = fileAudioRecorder;
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CANALE_CONFIGURAZIONE, FORMATO_AUDIO, AUDIO_BUFFER_SIZE);
    }


    public void stopRecording() {
        if (!onRegistrazione)
            return;

        onRegistrazione = false;
        audioRecord.stop();
    }

    public void startRecording() {

        audioRecord.startRecording();
        onRegistrazione = true;

        byte[] audioBufferSize = new byte[AUDIO_BUFFER_SIZE];

        new Thread(() -> {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileAudioRecorder);
                while (onRegistrazione) {
                    int bytesRead = audioRecord.read(audioBufferSize, 0, AUDIO_BUFFER_SIZE);
                    fileOutputStream.write(audioBufferSize, 0, bytesRead);
                }
                fileOutputStream.close();
            } catch (IOException e) {
                Log.e("AudioRecorder.startRecording()", "Errore nella scrittura del file audio", e);
            }
        }).start();
    }

    public File getAudioRecorder() {
        return fileAudioRecorder;
    }


}

