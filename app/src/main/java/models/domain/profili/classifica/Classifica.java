package models.domain.profili.classifica;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import models.database.costantiDB.CostantiDBClassifica;
import models.domain.DataPersistenza;

public class Classifica implements DataPersistenza<Classifica> {

    private TreeMap<String, Integer> classifica;

    public Classifica(TreeMap<String, Integer> classifica) {
        this.classifica = classifica;
    }

    public Classifica(Map<String, Object> fromDatabaseMap, String fromDatabaseKey) {
        Classifica c = this.fromMap(fromDatabaseMap);

        this.classifica = c.getClassifica();
    }

    public TreeMap<String, Integer> getClassifica() {
        return classifica;
    }

    public void setClassifica(TreeMap<String, Integer> classifica) {
        this.classifica = classifica;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = new HashMap<>();

        entityMap.put(CostantiDBClassifica.CLASSIFICA, this.classifica);
        return entityMap;
    }

    @Override
    public Classifica fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("Classifica.fromMap()", fromDatabaseMap.toString());
        return new Classifica(
                new TreeMap<String, Integer>((Map<String, Integer>) fromDatabaseMap.get(CostantiDBClassifica.CLASSIFICA))
        );
    }

    @Override
    public String toString() {
        return "Classifica{" +
                "classifica=" + classifica +
                '}';
    }

}
