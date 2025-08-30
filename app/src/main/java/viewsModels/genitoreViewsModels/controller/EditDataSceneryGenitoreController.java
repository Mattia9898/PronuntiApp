package viewsModels.genitoreViewsModels.controller;

import android.util.Log;

import java.time.LocalDate;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;

public class EditDataSceneryGenitoreController implements EditDataSceneryController {

    private GenitoreViewsModels mGenitoreViewsModels;

    public EditDataSceneryGenitoreController(GenitoreViewsModels mGenitoreViewsModels) {
        this.mGenitoreViewsModels = mGenitoreViewsModels;
    }

    public void editDataScenery(LocalDate localDate, int therapy, int position, String idPatient, int indexPatient){

        mGenitoreViewsModels.getPazienteLiveData().getValue().getTerapie().
                get(therapy).getListScenariGioco().
                get(position).setDataInizioScenarioGioco(localDate);

        Log.d("ScenarioAdapter",""+ mGenitoreViewsModels.getPazienteLiveData().
                getValue().getTerapie().get(therapy).
                getListScenariGioco().get(position).toString());

        mGenitoreViewsModels.aggiornaPazienteRemoto();
    }

}
