package models.API;


import android.content.Context;
import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.protobuf.ByteString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SpeechToText {

    private static final int CAMPIONE_RATE = 16000;

    private static final String CODE_LANGUAGE_ITA = "it-IT";
    
    private static final RecognitionConfig RECOGNITION_CONFIGURATION = RecognitionConfig.newBuilder()
            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
            .setLanguageCode(CODE_LANGUAGE_ITA)
            .setSampleRateHertz(CAMPIONE_RATE)
            .build();

    public static List<String> callAPI(Context context, File audioSpeech) {

        GoogleCredentials googleCredentials = ProviderGoogleCredentials.getInstance(context).getCredentials();

        try {
            SpeechClient speechClient = SpeechClient.create(SpeechSettings.newBuilder().setCredentialsProvider(() -> googleCredentials).build());

            byte[] data = Files.readAllBytes(audioSpeech.toPath());
            ByteString byteString = ByteString.copyFrom(data);

            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder().setContent(byteString).build();
            RecognizeResponse recognizeResponse = speechClient.recognize(RECOGNITION_CONFIGURATION, recognitionAudio);

            List<SpeechRecognitionResult> speechRecognitionResults = recognizeResponse.getResultsList();
            List<String> listWords = new ArrayList<>();

            for (SpeechRecognitionResult result : speechRecognitionResults) {
                String word = result.getAlternativesList().get(0).getTranscript();
                listWords.addAll(Arrays.asList(word.split("\\s+")));
            }
            return listWords;

        } catch (IOException e) {
            Log.e("SpeechToTextAPI.callAPI()", "Errore lettura audio file", e);
        }

        return null;
    }

}
