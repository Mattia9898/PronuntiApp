package viewsModels.pazienteViewsModels.controller;


import models.domain.esercizi.EsercizioSequenzaParole;
import models.API.AudioConverter;
import models.API.SpeechToText;

import android.content.Context;

import java.util.Arrays;
import java.util.List;
import java.io.File;


public class EsercizioSequenzaParoleController {

    private EsercizioSequenzaParole esercizioSequenzaParole;


    public void setEsercizioSequenzaParole(EsercizioSequenzaParole esercizioSequenzaParole) {
        this.esercizioSequenzaParole = esercizioSequenzaParole;
    }


    // metodo per controllare l'audio registrato nell'esercizio dall'utente
    public boolean checkAudio(File audioRecorded, Context context) {

        List<String> listOfCorrectWords = Arrays.asList(
                esercizioSequenzaParole.getParolaDaIndovinare1().toLowerCase(),
                esercizioSequenzaParole.getParolaDaIndovinare2().toLowerCase(),
                esercizioSequenzaParole.getParolaDaIndovinare3().toLowerCase());

        List<String> wordsRecorded = SpeechToText.callAPI(context, audioRecorded);

        if (wordsRecorded == null || listOfCorrectWords.size() != wordsRecorded.size()) {
            return false;
        }

        for (int i = 0; i < listOfCorrectWords.size(); i++) {
            if (!listOfCorrectWords.get(i).equals(wordsRecorded.get(i).toLowerCase())) {
                return false;
            }
        }

        return true;
    }


}

