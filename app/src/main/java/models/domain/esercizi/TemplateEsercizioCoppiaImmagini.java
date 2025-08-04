package models.domain.esercizi;


import models.domain.esercizi.risultati.RisultatoEsercizioSequenzaParole;
import models.database.costantiDB.CostantiDBTemplateEsercizioCoppiaImmagini;
import models.database.costantiDB.CostantiDBRisultato;

import java.util.Map;
import java.io.File;

import android.util.Log;


public class TemplateEsercizioCoppiaImmagini extends AbstractEsercizio implements Esercizio {


    protected String immagineErrataEsercizioCoppiaImmagini;

    protected String immagineCorrettaEsercizioCoppiaImmagini;

    protected String audioEsercizioCoppiaImmagini;


    public TemplateEsercizioCoppiaImmagini(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        TemplateEsercizioCoppiaImmagini templateEsercizioCoppiaImmagini = this.fromMap(fromDatabaseMap);
        this.idEsercizio = fromDatabaseKey;
        this.ricompensaRispostaCorretta = templateEsercizioCoppiaImmagini.getRicompensaCorretto();
        this.ricompensaRispostaErrata = templateEsercizioCoppiaImmagini.getRicompensaErrato();
        this.audioEsercizioCoppiaImmagini = templateEsercizioCoppiaImmagini.getAudioEsercizioCoppiaImmagini();
        this.immagineCorrettaEsercizioCoppiaImmagini = templateEsercizioCoppiaImmagini.getImmagineCorrettaEsercizioCoppiaImmagini();
        this.immagineErrataEsercizioCoppiaImmagini = templateEsercizioCoppiaImmagini.getImmagineErrataEsercizioCoppiaImmagini();
    }

    public TemplateEsercizioCoppiaImmagini(int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String audioEsercizioCoppiaImmagini, String immagineCorrettaEsercizioCoppiaImmagini, String immagineErrataEsercizioCoppiaImmagini) {
        super(ricompensaRispostaCorretta, ricompensaRispostaErrata);
        this.audioEsercizioCoppiaImmagini = audioEsercizioCoppiaImmagini;
        this.immagineCorrettaEsercizioCoppiaImmagini = immagineCorrettaEsercizioCoppiaImmagini;
        this.immagineErrataEsercizioCoppiaImmagini = immagineErrataEsercizioCoppiaImmagini;
    }

    public TemplateEsercizioCoppiaImmagini(String idEsercizio, int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String audioEsercizioCoppiaImmagini, String immagineCorrettaEsercizioCoppiaImmagini, String immagineErrataEsercizioCoppiaImmagini) {
        super(idEsercizio, ricompensaRispostaCorretta, ricompensaRispostaErrata);
        this.audioEsercizioCoppiaImmagini = audioEsercizioCoppiaImmagini;
        this.immagineCorrettaEsercizioCoppiaImmagini = immagineCorrettaEsercizioCoppiaImmagini;
        this.immagineErrataEsercizioCoppiaImmagini = immagineErrataEsercizioCoppiaImmagini;
    }

    public TemplateEsercizioCoppiaImmagini() {}


    public String getImmagineErrataEsercizioCoppiaImmagini() {
        return immagineErrataEsercizioCoppiaImmagini;
    }

    public String getImmagineCorrettaEsercizioCoppiaImmagini() {
        return immagineCorrettaEsercizioCoppiaImmagini;
    }

    public String getAudioEsercizioCoppiaImmagini() {
        return audioEsercizioCoppiaImmagini;
    }


    @Override
    public String toString() {
        return "TemplateEsercizioCoppiaImmagini{" +
                "idEsercizio='" + idEsercizio + '\'' +
                ", ricompensaCorretto=" + ricompensaRispostaCorretta +
                ", ricompensaErrato=" + ricompensaRispostaErrata +
                ", audioEsercizio='" + audioEsercizioCoppiaImmagini + '\'' +
                ", immagineEsercizioCorretta='" + immagineCorrettaEsercizioCoppiaImmagini + '\'' +
                ", immagineEsercizioErrata='" + immagineErrataEsercizioCoppiaImmagini + '\'' +
                '}';
    }

    @Override
    public TemplateEsercizioCoppiaImmagini fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("TemplateEsercizioCoppiaImmagini.fromMap()", fromDatabaseMap.toString());
        return new TemplateEsercizioCoppiaImmagini(
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBTemplateEsercizioCoppiaImmagini.RICOMPENSA_RISPOSTA_CORRETTA)),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBTemplateEsercizioCoppiaImmagini.RICOMPENSA_RISPOSTA_ERRATA)),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioCoppiaImmagini.AUDIO_ESERCIZIO_COPPIA_IMMAGINI),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioCoppiaImmagini.IMMAGINE_ESERCIZIO_CORRETTA),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioCoppiaImmagini.IMMAGINE_ESERCIZIO_ERRATA)
        );
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put(CostantiDBTemplateEsercizioCoppiaImmagini.AUDIO_ESERCIZIO_COPPIA_IMMAGINI, this.audioEsercizioCoppiaImmagini);
        map.put(CostantiDBTemplateEsercizioCoppiaImmagini.IMMAGINE_ESERCIZIO_CORRETTA, this.immagineCorrettaEsercizioCoppiaImmagini);
        map.put(CostantiDBTemplateEsercizioCoppiaImmagini.IMMAGINE_ESERCIZIO_ERRATA, this.immagineErrataEsercizioCoppiaImmagini);
        return map;
    }


}
