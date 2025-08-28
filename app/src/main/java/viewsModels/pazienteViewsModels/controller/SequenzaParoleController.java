package viewsModels.pazienteViewsModels.controller;

import android.content.Context;

import java.io.File;

import java.util.Arrays;

import java.util.List;

import models.domain.esercizi.EsercizioSequenzaParole;

import models.API.AudioConverter;

import models.API.SpeechToText;

public class SequenzaParoleController {

    private EsercizioSequenzaParole mEsercizioSequenzaParole;

    public void setEsercizioSequenzaParole(EsercizioSequenzaParole mEsercizioSequenzaParole) {
        this.mEsercizioSequenzaParole = mEsercizioSequenzaParole;
    }

    public boolean verificaAudio(File audioRegistrato, Context context) {

        List<String> paroleCorrette = Arrays.asList(
                mEsercizioSequenzaParole.getParolaDaIndovinare1().toLowerCase(),
                mEsercizioSequenzaParole.getParolaDaIndovinare2().toLowerCase(),
                mEsercizioSequenzaParole.getParolaDaIndovinare3().toLowerCase());

        List<String> paroleRegistrate = SpeechToText.callAPI(context, audioRegistrato);

        if (paroleRegistrate == null || paroleCorrette.size() != paroleRegistrate.size()) {
            return false;
        }

        for (int i = 0; i < paroleCorrette.size(); i++) {
            if (!paroleCorrette.get(i).equals(paroleRegistrate.get(i).toLowerCase())) {
                return false;
            }
        }

        return true;
    }

    public File convertiAudio(File audioRegistrato, File outputFile) {
        return AudioConverter.convertAudio(audioRegistrato, outputFile);
    }

}

