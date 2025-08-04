package models.domain.esercizi;


import models.database.costantiDB.CostantiDBTemplateEsercizioDenominazioneImmagini;

import android.util.Log;

import java.util.Map;


public class TemplateEsercizioDenominazioneImmagini extends AbstractEsercizio implements Esercizio {

    protected String immagineEsercizio;

    protected String parolaEsercizio;

    protected String audioAiuto;

    public TemplateEsercizioDenominazioneImmagini() {}

    public TemplateEsercizioDenominazioneImmagini(String idEsercizio, int ricompensaCorretto, int ricompensaErrato, String immagineEsercizio, String parolaEsercizio, String audioAiuto) {
        super(idEsercizio, ricompensaCorretto, ricompensaErrato);
        this.immagineEsercizio = immagineEsercizio;
        this.parolaEsercizio = parolaEsercizio;
        this.audioAiuto = audioAiuto;
    }

    public TemplateEsercizioDenominazioneImmagini(int ricompensaCorretto, int ricompensaErrato, String immagineEsercizio, String parolaEsercizio, String audioAiuto) {
        super(ricompensaCorretto, ricompensaErrato);
        this.immagineEsercizio = immagineEsercizio;
        this.parolaEsercizio = parolaEsercizio;
        this.audioAiuto = audioAiuto;
    }

    public TemplateEsercizioDenominazioneImmagini(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        TemplateEsercizioDenominazioneImmagini t = this.fromMap(fromDatabaseMap);

        this.idEsercizio = fromDatabaseKey;
        this.ricompensaRispostaCorretta = t.getRicompensaCorretto();
        this.ricompensaRispostaErrata = t.getRicompensaErrato();
        this.immagineEsercizio = t.getImmagineEsercizio();
        this.parolaEsercizio = t.getParolaEsercizio();
        this.audioAiuto = t.getAudioAiuto();
    }

    public String getImmagineEsercizio() {
        return immagineEsercizio;
    }

    public String getParolaEsercizio() {
        return parolaEsercizio;
    }

    public String getAudioAiuto() {
        return audioAiuto;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = super.toMap();
        entityMap.put(CostantiDBTemplateEsercizioDenominazioneImmagini.IMMAGINE_ESERCIZIO, this.immagineEsercizio);
        entityMap.put(CostantiDBTemplateEsercizioDenominazioneImmagini.PAROLA_ESERCIZIO, this.parolaEsercizio);
        entityMap.put(CostantiDBTemplateEsercizioDenominazioneImmagini.AUDIO_AIUTO, this.audioAiuto);
        return entityMap;
    }

    @Override
    public TemplateEsercizioDenominazioneImmagini fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("TemplateEsercizioDenominazioneImmagine.fromMap()", fromDatabaseMap.toString());
        return new TemplateEsercizioDenominazioneImmagini(
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBTemplateEsercizioDenominazioneImmagini.RICOMPENSA_CORRETTO)),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBTemplateEsercizioDenominazioneImmagini.RICOMPENSA_ERRATO)),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioDenominazioneImmagini.IMMAGINE_ESERCIZIO),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioDenominazioneImmagini.PAROLA_ESERCIZIO),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioDenominazioneImmagini.AUDIO_AIUTO)
        );
    }

    @Override
    public String toString() {
        return "TemplateEsercizioDenominazioneImmagine{" +
                "idEsercizio='" + idEsercizio + '\'' +
                ", ricompensaCorretto=" + ricompensaRispostaCorretta +
                ", ricompensaErrato=" + ricompensaRispostaErrata +
                ", immagineEsercizio='" + immagineEsercizio + '\'' +
                ", parolaEsercizio='" + parolaEsercizio + '\'' +
                ", audioAiuto='" + audioAiuto + '\'' +
                '}';
    }

}

