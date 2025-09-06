package viewsModels.pazienteViewsModels.controller;


import models.API.SpeechToText;

import java.io.File;
import java.util.List;

import android.content.Context;

import models.domain.esercizi.EsercizioDenominazioneImmagini;


public class EsercizioDenominazioneImmaginiController {

    private EsercizioDenominazioneImmagini esercizioDenominazioneImmagini;

    public void setEsercizioDenominazioneImmagini(EsercizioDenominazioneImmagini esercizioDenominazioneImmagini) {
        this.esercizioDenominazioneImmagini = esercizioDenominazioneImmagini;
    }


    // metodo per controllare parola per parola l'audio registrato nell'esercizio dall'utente
    public boolean checkAudio(File audioRecorded, Context context) {

        List<String> wordsRecorded = SpeechToText.callAPI(context, audioRecorded);

        if (wordsRecorded == null || wordsRecorded.isEmpty()) {
            return false;
        } else {
            boolean methodCheck = true;
            for (String word : wordsRecorded) {
                if (!(word.toLowerCase().equals(esercizioDenominazioneImmagini.getParolaEsercizioDenominazioneImmagini().toLowerCase()))) {
                    methodCheck = false;
                }

            }
            return methodCheck;
        }
    }

}

