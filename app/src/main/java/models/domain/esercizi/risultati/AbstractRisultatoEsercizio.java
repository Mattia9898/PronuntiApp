package models.domain.esercizi.risultati;


import models.database.costantiDB.CostantiDBRisultato;

import java.util.Map;
import java.util.HashMap;


public abstract class AbstractRisultatoEsercizio implements RisultatoEsercizio {

    protected boolean risultatoGiusto;

    public AbstractRisultatoEsercizio(boolean risultatoGiusto) {
        this.risultatoGiusto = risultatoGiusto;
    }

    public AbstractRisultatoEsercizio() {}


    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(CostantiDBRisultato.RISULTATO_GIUSTO, this.risultatoGiusto);
        return entityMap;
    }

    public boolean isEsitoCorretto() {
        return risultatoGiusto;
    }


}
