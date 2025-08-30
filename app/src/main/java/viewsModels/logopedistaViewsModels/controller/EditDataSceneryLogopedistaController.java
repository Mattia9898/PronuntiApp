package viewsModels.logopedistaViewsModels.controller;

import java.time.LocalDate;

import models.domain.profili.Paziente;

import viewsModels.genitoreViewsModels.controller.EditDataSceneryController;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

public class EditDataSceneryLogopedistaController implements EditDataSceneryController {

    private LogopedistaViewsModels mLogopedistaViewsModels;

    public EditDataSceneryLogopedistaController(LogopedistaViewsModels mLogopedistaViewModel) {
        this.mLogopedistaViewsModels = mLogopedistaViewModel;
    }

    @Override
    public void editDataScenery(LocalDate localDate, int therapy, int position, String idPatient, int indexPatient) {

        for (Paziente paziente: mLogopedistaViewsModels.getLogopedistaLiveData().getValue().getListaPazienti()) {

            if(paziente.getIdProfilo().equals(idPatient)){

                mLogopedistaViewsModels.getLogopedistaLiveData().getValue().
                        getListaPazienti().get(indexPatient).getTerapie().
                        get(therapy).getListScenariGioco().
                        get(position).setDataInizioScenarioGioco(localDate);

                mLogopedistaViewsModels.aggiornaLogopedistaRemoto();

                break;
            }
        }
    }

}
