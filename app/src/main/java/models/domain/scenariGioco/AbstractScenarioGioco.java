package models.domain.scenariGioco;


import models.domain.DataPersistenza;
import models.database.costantiDB.CostantiDBTemplateScenarioGioco;

import java.util.Map;
import java.util.HashMap;


public abstract class AbstractScenarioGioco implements DataPersistenza<AbstractScenarioGioco> {

    protected String sfondoImmagine;

    protected String immagine1;

    protected String immagine2;

    protected String immagine3;


    public AbstractScenarioGioco(String sfondoImmagine, String immagine1, String immagine2, String immagine3) {
        this.sfondoImmagine = sfondoImmagine;
        this.immagine1 = immagine1;
        this.immagine2 = immagine2;
        this.immagine3 = immagine3;
    }

    public AbstractScenarioGioco() {}


    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put(CostantiDBTemplateScenarioGioco.SFONDO_IMMAGINE, this.sfondoImmagine);
        map.put(CostantiDBTemplateScenarioGioco.IMMAGINE_1, this.immagine1);
        map.put(CostantiDBTemplateScenarioGioco.IMMAGINE_2, this.immagine2);
        map.put(CostantiDBTemplateScenarioGioco.IMMAGINE_3, this.immagine3);
        return map;
    }


    public String getSfondoImmagine() {
        return sfondoImmagine;
    }

    public String getImmagine1() {
        return immagine1;
    }

    public String getImmagine2() {
        return immagine2;
    }

    public String getImmagine3() {
        return immagine3;
    }


}
