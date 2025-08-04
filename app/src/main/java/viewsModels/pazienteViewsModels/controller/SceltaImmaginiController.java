package viewsModels.pazienteViewsModels.controller;

import android.content.Context;

import java.io.File;

import java.util.List;

import models.domain.esercizi.EsercizioDenominazioneImmagini;

import models.API.FFmpegKitAPI.AudioConverter;

import models.API.SpeechToTextAPI.SpeechToTextAPI;

public class SceltaImmaginiController{

    private EsercizioDenominazioneImmagini mEsercizioDenominazioneImmagini;

    public EsercizioDenominazioneImmagini getEsercizioDenominazioneImmagine() {
        return mEsercizioDenominazioneImmagini;
    }

    public void setEsercizioDenominazioneImmagini(EsercizioDenominazioneImmagini esercizioDenominazioneImmagine) {
        this.mEsercizioDenominazioneImmagini = esercizioDenominazioneImmagine;
    }


    public boolean verificaAudio(File audioRegistrato, Context context) {
        List<String> paroleRegistrate = SpeechToTextAPI.callAPI(context, audioRegistrato);

        if (paroleRegistrate == null || paroleRegistrate.isEmpty()) {
            return false;
        }
        else {
            boolean check = true;
            for (String parola : paroleRegistrate) {
                if (!(parola.toLowerCase().equals(mEsercizioDenominazioneImmagini.getParolaEsercizio().toLowerCase()))) {
                    check = false;
                }

            }
            return check;
        }
    }

    public File convertiAudio(File audioRegistrato, File outputFile) {
        return AudioConverter.convertiAudio(audioRegistrato, outputFile);
    }

}

