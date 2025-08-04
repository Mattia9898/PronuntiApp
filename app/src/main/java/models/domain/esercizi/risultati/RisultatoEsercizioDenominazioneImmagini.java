package models.domain.esercizi.risultati;


import models.database.costantiDB.CostantiDBRisultato;

import java.util.Map;
import java.io.File;

import android.util.Log;


public class RisultatoEsercizioDenominazioneImmagini extends AbstractRisultatoEsercizioConAudio {

    private int counterAiutiUtilizzati;


    public RisultatoEsercizioDenominazioneImmagini(Map<String, Object> fromDatabaseMap) {
        RisultatoEsercizioDenominazioneImmagini risultatoEsercizioDenominazioneImmagini = this.fromMap(fromDatabaseMap);
        this.risultatoGiusto = risultatoEsercizioDenominazioneImmagini.isEsitoCorretto();
        this.audioRegistrato = risultatoEsercizioDenominazioneImmagini.getAudioRegistrato();
        this.counterAiutiUtilizzati = risultatoEsercizioDenominazioneImmagini.getCounterAiutiUtilizzati();
    }

    public RisultatoEsercizioDenominazioneImmagini(boolean esitoCorretto, String audioRegistrato, int counterAiutiUtilizzati) {
        super(esitoCorretto, audioRegistrato);
        this.counterAiutiUtilizzati = counterAiutiUtilizzati;
    }


    public int getCounterAiutiUtilizzati() {
        return counterAiutiUtilizzati;
    }


    @Override
    public RisultatoEsercizioDenominazioneImmagini fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("RisultatoEsercizioDenominazioneImmagine.fromMap()", fromDatabaseMap.toString());
        return new RisultatoEsercizioDenominazioneImmagini(
                (boolean) fromDatabaseMap.get(CostantiDBRisultato.RISULTATO_GIUSTO),
                (String) fromDatabaseMap.get(CostantiDBRisultato.AUDIO_REGISTRATO),
                Math.toIntExact((long) fromDatabaseMap.get(CostantiDBRisultato.COUNTER_AIUTI_UTILIZZATI))
        );
    }

    @Override
    public String toString() {
        return "RisultatoEsercizioDenominazioneImmagine{" +
                "esitoCorretto=" + risultatoGiusto +
                ", audioRegistrato='" + audioRegistrato + '\'' +
                ", countAiuti=" + counterAiutiUtilizzati +
                '}';
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = super.toMap();
        entityMap.put(CostantiDBRisultato.COUNTER_AIUTI_UTILIZZATI, this.counterAiutiUtilizzati);
        return entityMap;
    }


}
