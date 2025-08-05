package models.domain.scenariGioco;

import android.util.Log;

import java.util.Map;

import models.database.costantiDB.CostantiDBTemplateScenarioGioco;

public class TemplateScenarioGioco extends AbstractScenarioGioco {

    private String idTemplateScenarioGioco;

    public TemplateScenarioGioco() {}

    public TemplateScenarioGioco(String idTemplateScenarioGioco, String immagineSfondo, String immagineTappa1, String immagineTappa2, String immagineTappa3) {
        super(immagineSfondo, immagineTappa1, immagineTappa2, immagineTappa3);
        this.idTemplateScenarioGioco = idTemplateScenarioGioco;
    }

    public TemplateScenarioGioco(String immagineSfondo, String immagineTappa1, String immagineTappa2, String immagineTappa3) {
        super(immagineSfondo, immagineTappa1, immagineTappa2, immagineTappa3);
    }

    public TemplateScenarioGioco(Map<String,Object> fromDatabaseMap, String fromDatabaseKey){
        TemplateScenarioGioco t = this.fromMap(fromDatabaseMap);

        this.idTemplateScenarioGioco = fromDatabaseKey;
        this.sfondoImmagine = t.getSfondoImmagine();
        this.immagine1 = t.getImmagine1();
        this.immagine2 = t.getImmagine2();
        this.immagine3 = t.getImmagine3();
    }

    public final String getIdTemplateScenarioGioco() {
        return idTemplateScenarioGioco;
    }

    public void setIdTemplateScenarioGioco(String idTemplateScenarioGioco) {
        this.idTemplateScenarioGioco = idTemplateScenarioGioco;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = super.toMap();

        //entityMap.put(CostantiDBTemplateScenarioGioco.ID_TEMPLATE_SCENARIOGIOCO, this.idTemplateScenarioGioco);
        return entityMap;
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
    public String toString() {
        return "TemplateScenarioGioco{" +
                "idTemplateScenarioGioco='" + idTemplateScenarioGioco + '\'' +
                ", immagineSfondo='" + sfondoImmagine + '\'' +
                ", immagineTappa1='" + immagine1 + '\'' +
                ", immagineTappa2='" + immagine2 + '\'' +
                ", immagineTappa3='" + immagine3 + '\'' +
                '}';
    }

}
