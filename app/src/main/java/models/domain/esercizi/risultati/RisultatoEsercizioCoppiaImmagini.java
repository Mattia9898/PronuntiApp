package models.domain.esercizi.risultati;


import models.database.costantiDB.CostantiDBRisultato;

import java.util.Map;
import android.util.Log;


public class RisultatoEsercizioCoppiaImmagini extends AbstractRisultatoEsercizio {


    public RisultatoEsercizioCoppiaImmagini(Map<String, Object> fromDatabaseMap) {
        RisultatoEsercizioCoppiaImmagini risultatoEsercizioCoppiaImmagini = this.fromMap(fromDatabaseMap);
        this.risultatoGiusto = risultatoEsercizioCoppiaImmagini.isEsitoCorretto();
    }

    public RisultatoEsercizioCoppiaImmagini(boolean risultatoGiusto) {
        super(risultatoGiusto);
    }


    @Override
    public RisultatoEsercizioCoppiaImmagini fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("RisultatoEsercizioCoppiaImmagini.fromMap()", fromDatabaseMap.toString());
        return new RisultatoEsercizioCoppiaImmagini(
                (boolean) fromDatabaseMap.get(CostantiDBRisultato.RISULTATO_GIUSTO)
        );
    }

    @Override
    public String toString() {
        return "RisultatoEsercizioCoppiaImmagini{" +
                "esitoCorretto=" + risultatoGiusto +
                '}';
    }

    @Override
    public Map<String, Object> toMap() {
        return super.toMap();
    }


}
