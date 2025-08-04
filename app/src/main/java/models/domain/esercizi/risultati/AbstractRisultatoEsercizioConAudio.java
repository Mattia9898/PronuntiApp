package models.domain.esercizi.risultati;


import models.database.costantiDB.CostantiDBRisultato;

import java.util.HashMap;
import java.util.Map;
import java.io.File;


public abstract class AbstractRisultatoEsercizioConAudio extends AbstractRisultatoEsercizio {

    protected String audioRegistrato;

    public AbstractRisultatoEsercizioConAudio(boolean risultatoGiusto, String audioRegistrato) {
        super(risultatoGiusto);
        this.audioRegistrato = audioRegistrato;
    }

    public AbstractRisultatoEsercizioConAudio() {}


    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = super.toMap();

        entityMap.put(CostantiDBRisultato.AUDIO_REGISTRATO, this.audioRegistrato);
        return entityMap;
    }

    public String getAudioRegistrato() {
        return audioRegistrato;
    }


}
