package models.domain.profili.classifica;


import models.domain.DataPersistenza;
import models.database.costantiDB.CostantiDBClassifica;

import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;

import android.util.Log;


public class Classifica implements DataPersistenza<Classifica> {

    private TreeMap<String, Integer> classificaTreeMap;


    public TreeMap<String, Integer> getClassificaTreeMap() {
        return classificaTreeMap;
    }

    public Classifica(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        Classifica classifica = this.fromMap(fromDatabaseMap);
        this.classificaTreeMap = classifica.getClassificaTreeMap();
    }

    public Classifica(TreeMap<String, Integer> classificaTreeMap) {
        this.classificaTreeMap = classificaTreeMap;
    }


    @Override
    public String toString() {
        return "Classifica{" +
                "classifica=" + classificaTreeMap +
                '}';
    }

    @Override
    public Classifica fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("Classifica.fromMap()", fromDatabaseMap.toString());
        return new Classifica(
                new TreeMap<String, Integer>((Map<String, Integer>) fromDatabaseMap.get(CostantiDBClassifica.CLASSIFICA))
        );
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(CostantiDBClassifica.CLASSIFICA, this.classificaTreeMap);
        return map;
    }


}
