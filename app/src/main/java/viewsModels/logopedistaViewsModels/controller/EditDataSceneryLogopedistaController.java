package viewsModels.logopedistaViewsModels.controller;


import java.time.LocalDate;

import models.domain.profili.Paziente;

import viewsModels.genitoreViewsModels.controller.EditDataSceneryController;
import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;


public class EditDataSceneryLogopedistaController implements EditDataSceneryController {

    private LogopedistaViewsModels logopedistaViewsModels;

    public EditDataSceneryLogopedistaController(LogopedistaViewsModels logopedistaViewsModels) {
        this.logopedistaViewsModels = logopedistaViewsModels;
    }

    @Override
    public void editDataScenery(LocalDate localDate, int therapy, int position, String idPatient, int indexPatient) {

        for (Paziente paziente: logopedistaViewsModels.getLogopedistaLiveData().getValue().getListaPazienti()) {

            if(paziente.getIdProfilo().equals(idPatient)){

                logopedistaViewsModels.getLogopedistaLiveData().getValue().
                        getListaPazienti().get(indexPatient).getTerapie().
                        get(therapy).getListScenariGioco().
                        get(position).setDataInizioScenarioGioco(localDate);

                logopedistaViewsModels.updateLogopedistaRemoto();

                break;
            }
        }
    }

}
