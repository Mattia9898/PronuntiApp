package models.domain.esercizi;

import android.util.Log;

import java.io.File;
import java.util.Map;

import models.database.costantiDB.CostantiDBRisultato;
import models.database.costantiDB.CostantiDBTemplateEsercizioCoppiaImmagini;
import models.domain.esercizi.risultati.RisultatoEsercizioSequenzaParole;

public class TemplateEsercizioCoppiaImmagini extends AbstractEsercizio implements Esercizio {

    protected String audioEsercizio;

    protected String immagineEsercizioCorretta;

    protected String immagineEsercizioErrata;

    public TemplateEsercizioCoppiaImmagini() {}

    public TemplateEsercizioCoppiaImmagini(String idEsercizio, int ricompensaCorretto, int ricompensaErrato, String audioEsercizio, String immagineEsercizioCorretta, String immagineEsercizioErrata) {
        super(idEsercizio, ricompensaCorretto, ricompensaErrato);
        this.audioEsercizio = audioEsercizio;
        this.immagineEsercizioCorretta = immagineEsercizioCorretta;
        this.immagineEsercizioErrata = immagineEsercizioErrata;
    }

    public TemplateEsercizioCoppiaImmagini(int ricompensaCorretto, int ricompensaErrato, String audioEsercizio, String immagineEsercizioCorretta, String immagineEsercizioErrata) {
        super(ricompensaCorretto, ricompensaErrato);
        this.audioEsercizio = audioEsercizio;
        this.immagineEsercizioCorretta = immagineEsercizioCorretta;
        this.immagineEsercizioErrata = immagineEsercizioErrata;
    }

    public TemplateEsercizioCoppiaImmagini(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        TemplateEsercizioCoppiaImmagini t = this.fromMap(fromDatabaseMap);

        this.idEsercizio = fromDatabaseKey;
        this.ricompensaRispostaCorretta = t.getRicompensaCorretto();
        this.ricompensaRispostaErrata = t.getRicompensaErrato();
        this.audioEsercizio = t.getAudioEsercizio();
        this.immagineEsercizioCorretta = t.getImmagineEsercizioCorretta();
        this.immagineEsercizioErrata = t.getImmagineEsercizioErrata();
    }

    public String getAudioEsercizio() {
        return audioEsercizio;
    }

    public String getImmagineEsercizioCorretta() {
        return immagineEsercizioCorretta;
    }

    public String getImmagineEsercizioErrata() {
        return immagineEsercizioErrata;
    }

    public void setAudioEsercizio(String audioEsercizio) {
        this.audioEsercizio = audioEsercizio;
    }

    public void setImmagineEsercizioCorretta(String immagineEsercizioCorretta) {
        this.immagineEsercizioCorretta = immagineEsercizioCorretta;
    }

    public void setImmagineEsercizioErrata(String immagineEsercizioErrata) {
        this.immagineEsercizioErrata = immagineEsercizioErrata;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = super.toMap();

        //entityMap.put(CostantiDBTemplateEsercizioCoppiaImmagini.ID_TEMPLATE_ESERCIZIO, this.idEsercizio);
        entityMap.put(CostantiDBTemplateEsercizioCoppiaImmagini.AUDIO_ESERCIZIO, this.audioEsercizio);
        entityMap.put(CostantiDBTemplateEsercizioCoppiaImmagini.IMMAGINE_ESERCIZIO_CORRETTA, this.immagineEsercizioCorretta);
        entityMap.put(CostantiDBTemplateEsercizioCoppiaImmagini.IMMAGINE_ESERCIZIO_ERRATA, this.immagineEsercizioErrata);
        return entityMap;
    }

    @Override
    public TemplateEsercizioCoppiaImmagini fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("TemplateEsercizioCoppiaImmagini.fromMap()", fromDatabaseMap.toString());
        return new TemplateEsercizioCoppiaImmagini(
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBTemplateEsercizioCoppiaImmagini.RICOMPENSA_CORRETTO)),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBTemplateEsercizioCoppiaImmagini.RICOMPENSA_ERRATO)),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioCoppiaImmagini.AUDIO_ESERCIZIO),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioCoppiaImmagini.IMMAGINE_ESERCIZIO_CORRETTA),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioCoppiaImmagini.IMMAGINE_ESERCIZIO_ERRATA)
        );
    }

    @Override
    public String toString() {
        return "TemplateEsercizioCoppiaImmagini{" +
                "idEsercizio='" + idEsercizio + '\'' +
                ", ricompensaCorretto=" + ricompensaRispostaCorretta +
                ", ricompensaErrato=" + ricompensaRispostaErrata +
                ", audioEsercizio='" + audioEsercizio + '\'' +
                ", immagineEsercizioCorretta='" + immagineEsercizioCorretta + '\'' +
                ", immagineEsercizioErrata='" + immagineEsercizioErrata + '\'' +
                '}';
    }

}
