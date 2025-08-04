package models.domain.esercizi;


import models.database.costantiDB.CostantiDBTemplateEsercizioDenominazioneImmagini;

import android.util.Log;

import java.util.Map;


public class TemplateEsercizioDenominazioneImmagini extends AbstractEsercizio implements Esercizio {


    protected String audioAiuto;

    protected String parolaEsercizioDenominazioneImmagini;

    protected String immagineEsercizioDenominazioneImmagini;


    public TemplateEsercizioDenominazioneImmagini(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        TemplateEsercizioDenominazioneImmagini templateEsercizioDenominazioneImmagini = this.fromMap(fromDatabaseMap);
        this.idEsercizio = fromDatabaseKey;
        this.ricompensaRispostaCorretta = templateEsercizioDenominazioneImmagini.getRicompensaCorretto();
        this.ricompensaRispostaErrata = templateEsercizioDenominazioneImmagini.getRicompensaErrato();
        this.immagineEsercizioDenominazioneImmagini = templateEsercizioDenominazioneImmagini.getImmagineEsercizioDenominazioneImmagini();
        this.parolaEsercizioDenominazioneImmagini = templateEsercizioDenominazioneImmagini.getParolaEsercizioDenominazioneImmagini();
        this.audioAiuto = templateEsercizioDenominazioneImmagini.getAudioAiuto();
    }

    public TemplateEsercizioDenominazioneImmagini(int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String immagineEsercizioDenominazioneImmagini, String parolaEsercizioDenominazioneImmagini, String audioAiuto) {
        super(ricompensaRispostaCorretta, ricompensaRispostaErrata);
        this.immagineEsercizioDenominazioneImmagini = immagineEsercizioDenominazioneImmagini;
        this.parolaEsercizioDenominazioneImmagini = parolaEsercizioDenominazioneImmagini;
        this.audioAiuto = audioAiuto;
    }

    public TemplateEsercizioDenominazioneImmagini(String idEsercizio, int ricompensaRispostaCorretta, int ricompensaRispostaErrata, String immagineEsercizioDenominazioneImmagini, String parolaEsercizioDenominazioneImmagini, String audioAiuto) {
        super(idEsercizio, ricompensaRispostaCorretta, ricompensaRispostaErrata);
        this.immagineEsercizioDenominazioneImmagini = immagineEsercizioDenominazioneImmagini;
        this.parolaEsercizioDenominazioneImmagini = parolaEsercizioDenominazioneImmagini;
        this.audioAiuto = audioAiuto;
    }

    public TemplateEsercizioDenominazioneImmagini() {}


    public String getAudioAiuto() {
        return audioAiuto;
    }

    public String getParolaEsercizioDenominazioneImmagini() {
        return parolaEsercizioDenominazioneImmagini;
    }

    public String getImmagineEsercizioDenominazioneImmagini() {
        return immagineEsercizioDenominazioneImmagini;
    }


    @Override
    public String toString() {
        return "TemplateEsercizioDenominazioneImmagine{" +
                "idEsercizio='" + idEsercizio + '\'' +
                ", ricompensaCorretto=" + ricompensaRispostaCorretta +
                ", ricompensaErrato=" + ricompensaRispostaErrata +
                ", immagineEsercizio='" + immagineEsercizioDenominazioneImmagini + '\'' +
                ", parolaEsercizio='" + parolaEsercizioDenominazioneImmagini + '\'' +
                ", audioAiuto='" + audioAiuto + '\'' +
                '}';
    }

    @Override
    public TemplateEsercizioDenominazioneImmagini fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("TemplateEsercizioDenominazioneImmagine.fromMap()", fromDatabaseMap.toString());
        return new TemplateEsercizioDenominazioneImmagini(
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBTemplateEsercizioDenominazioneImmagini.RICOMPENSA_RISPOSTA_CORRETTA)),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBTemplateEsercizioDenominazioneImmagini.RICOMPENSA_RISPOSTA_ERRATA)),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioDenominazioneImmagini.IMMAGINE_ESERCIZIO_DENOMINAZIONE_IMMAGINI),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioDenominazioneImmagini.PAROLA_ESERCIZIO_DENOMINAZIONE_IMMAGINI),
                (String) fromDatabaseMap.get(CostantiDBTemplateEsercizioDenominazioneImmagini.AUDIO_AIUTO)
        );
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put(CostantiDBTemplateEsercizioDenominazioneImmagini.IMMAGINE_ESERCIZIO_DENOMINAZIONE_IMMAGINI, this.immagineEsercizioDenominazioneImmagini);
        map.put(CostantiDBTemplateEsercizioDenominazioneImmagini.PAROLA_ESERCIZIO_DENOMINAZIONE_IMMAGINI, this.parolaEsercizioDenominazioneImmagini);
        map.put(CostantiDBTemplateEsercizioDenominazioneImmagini.AUDIO_AIUTO, this.audioAiuto);
        return map;
    }


}

