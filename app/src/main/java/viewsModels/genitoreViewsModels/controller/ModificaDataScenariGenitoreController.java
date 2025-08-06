package viewsModels.genitoreViewsModels.controller;

import android.util.Log;

import java.time.LocalDate;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;

public class ModificaDataScenariGenitoreController implements ModificaDataScenariController {

    private GenitoreViewsModels mGenitoreViewsModels;

    public ModificaDataScenariGenitoreController(GenitoreViewsModels mGenitoreViewsModels) {
        this.mGenitoreViewsModels = mGenitoreViewsModels;
    }

    public void modificaDataScenari(LocalDate date, int indiceTerapia,int position, String idPaziente, int indicePaziente){
        mGenitoreViewsModels.getPazienteLiveData().getValue().getTerapie().get(indiceTerapia).getScenariGioco().get(position).setDataInizioScenarioGioco(date);
        Log.d("ScenarioAdapter",""+ mGenitoreViewsModels.getPazienteLiveData().getValue().getTerapie().get(indiceTerapia).getScenariGioco().get(position).toString());
        mGenitoreViewsModels.aggiornaPazienteRemoto();
    }

}
