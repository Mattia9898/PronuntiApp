package models.domain.scenariGioco;


import models.database.costantiDB.CostantiDBTemplateScenarioGioco;

import java.util.Map;

import android.util.Log;


public class TemplateScenarioGioco extends AbstractScenarioGioco {

    private String idTemplateScenarioGioco;


    public TemplateScenarioGioco(Map<String,Object> fromDatabaseMap, String fromDatabaseKey){
        TemplateScenarioGioco t = this.fromMap(fromDatabaseMap);

        this.idTemplateScenarioGioco = fromDatabaseKey;
        this.sfondoImmagine = t.getSfondoImmagine();
        this.immagine1 = t.getImmagine1();
        this.immagine2 = t.getImmagine2();
        this.immagine3 = t.getImmagine3();
    }

    public TemplateScenarioGioco(String immagineSfondo, String immagineTappa1, String immagineTappa2, String immagineTappa3) {
        super(immagineSfondo, immagineTappa1, immagineTappa2, immagineTappa3);
    }

    public TemplateScenarioGioco() {}


    public final String getIdTemplateScenarioGioco() {
        return idTemplateScenarioGioco;
    }


    @Override
    public String toString() {
        return "TemplateScenarioGioco{" +
                "idTemplateScenarioGioco='" + idTemplateScenarioGioco + '\'' +
                ", immagineSfondo='" + sfondoImmagine + '\'' +
                ", immagineTappa1='" + immagine1 + '\'' +
                ", immagineTappa2='" + immagine2 + '\'' +
                ", immagineTappa3='" + immagine3 + '\'' +
                '}';
    }

    @Override
    public TemplateScenarioGioco fromMap(Map<String, Object> fromDatabaseMap) {
        Log.d("TemplateScenarioGioco.fromMap()", fromDatabaseMap.toString());
        return new TemplateScenarioGioco(
                (String) fromDatabaseMap.get(CostantiDBTemplateScenarioGioco.SFONDO_IMMAGINE),
                (String) fromDatabaseMap.get(CostantiDBTemplateScenarioGioco.IMMAGINE_1),
                (String) fromDatabaseMap.get(CostantiDBTemplateScenarioGioco.IMMAGINE_2),
                (String) fromDatabaseMap.get(CostantiDBTemplateScenarioGioco.IMMAGINE_3)
        );
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        return map;
    }


}
