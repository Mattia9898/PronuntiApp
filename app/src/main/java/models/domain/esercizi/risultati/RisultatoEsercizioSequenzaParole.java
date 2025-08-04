package models.domain.esercizi.risultati;


import models.database.costantiDB.CostantiDBRisultato;

import java.util.Map;
import java.io.File;

import android.util.Log;


public class RisultatoEsercizioSequenzaParole extends AbstractRisultatoEsercizioConAudio {


    public RisultatoEsercizioSequenzaParole(Map<String, Object> fromDatabaseMap) {
        RisultatoEsercizioSequenzaParole risultatoEsercizioSequenzaParole = this.fromMap(fromDatabaseMap);
        this.risultatoGiusto = risultatoEsercizioSequenzaParole.isEsitoCorretto();
        this.audioRegistrato = risultatoEsercizioSequenzaParole.getAudioRegistrato();
    }

    public RisultatoEsercizioSequenzaParole(boolean risultatoGiusto, String audioRegistrato) {
        super(risultatoGiusto, audioRegistrato);
    }


    @Override
    public RisultatoEsercizioSequenzaParole fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("RisultatoEsercizioSequenzaParole.fromMap()", fromDatabaseMap.toString());
        return new RisultatoEsercizioSequenzaParole(
                (boolean) fromDatabaseMap.get(CostantiDBRisultato.RISULTATO_GIUSTO),
                (String) fromDatabaseMap.get(CostantiDBRisultato.AUDIO_REGISTRATO)
        );
    }

    @Override
    public String toString() {
        return "RisultatoEsercizioSequenzaParole{" +
                "esitoCorretto=" + risultatoGiusto +
                ", audioRegistrato='" + audioRegistrato + '\'' +
                '}';
    }

    @Override
    public Map<String, Object> toMap() {
        return super.toMap();
    }


}
