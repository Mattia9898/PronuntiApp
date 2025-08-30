package viewsModels.genitoreViewsModels.controller;

import android.util.Log;

import java.time.LocalDate;

import viewsModels.genitoreViewsModels.GenitoreViewsModels;


public class EditDataSceneryGenitoreController implements EditDataSceneryController {

    private GenitoreViewsModels genitoreViewsModels;

    public EditDataSceneryGenitoreController(GenitoreViewsModels genitoreViewsModels) {
        this.genitoreViewsModels = genitoreViewsModels;
    }

    public void editDataScenery(LocalDate localDate, int therapy, int position, String idPatient, int indexPatient){

        genitoreViewsModels.getPazienteLiveData().getValue().getTerapie().
                get(therapy).getListScenariGioco().
                get(position).setDataInizioScenarioGioco(localDate);

        Log.d("ScenarioAdapter",""+ genitoreViewsModels.getPazienteLiveData().
                getValue().getTerapie().get(therapy).
                getListScenariGioco().get(position).toString());

        genitoreViewsModels.updatePatient();
    }

}
