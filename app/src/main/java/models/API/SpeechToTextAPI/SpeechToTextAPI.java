package models.API.SpeechToTextAPI;


import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;

import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.protobuf.ByteString;
import com.google.auth.oauth2.GoogleCredentials;


public class SpeechToTextAPI {

    private static final int CAMPIONE_RATE = 16000;

    private static final String LINGUAGGIO_CODICE_ITA = "it-IT";
    
    private static final RecognitionConfig RECOGNITION_CONFIG = RecognitionConfig.newBuilder()
            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
            .setLanguageCode(LINGUAGGIO_CODICE_ITA)
            .setSampleRateHertz(CAMPIONE_RATE)
            .build();

    public static List<String> callAPI(Context context, File audioSpeech) {

        GoogleCredentials googleCredentials = ProviderCredenzialiGoogle.getInstance(context).getCredentials();

        try {
            SpeechClient speechClient = SpeechClient.create(SpeechSettings.newBuilder().setCredentialsProvider(() -> googleCredentials).build());

            byte[] data = Files.readAllBytes(audioSpeech.toPath());
            ByteString byteString = ByteString.copyFrom(data);

            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder().setContent(byteString).build();
            RecognizeResponse recognizeResponse = speechClient.recognize(RECOGNITION_CONFIG, recognitionAudio);

            List<SpeechRecognitionResult> speechRecognitionResults = recognizeResponse.getResultsList();
            List<String> words = new ArrayList<>();

            for (SpeechRecognitionResult result : speechRecognitionResults) {
                String word = result.getAlternativesList().get(0).getTranscript();
                words.addAll(Arrays.asList(word.split("\\s+")));
            }
            return words;

        } catch (IOException e) {
            Log.e("SpeechToTextAPI.callAPI()", "Errore lettura audio file", e);
        }

        return null;
    }

}
