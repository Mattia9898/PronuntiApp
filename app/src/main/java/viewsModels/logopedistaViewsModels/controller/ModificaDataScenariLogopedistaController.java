package viewsModels.logopedistaViewsModels.controller;

import java.time.LocalDate;

import models.domain.profili.Paziente;

import viewsModels.genitoreViewsModels.controller.ModificaDataScenariController;

import viewsModels.logopedistaViewsModels.LogopedistaViewsModels;

public class ModificaDataScenariLogopedistaController implements ModificaDataScenariController {

    private LogopedistaViewsModels mLogopedistaViewsModels;

    public ModificaDataScenariLogopedistaController(LogopedistaViewsModels mLogopedistaViewModel) {
        this.mLogopedistaViewsModels = mLogopedistaViewModel;
    }

    @Override
    public void modificaDataScenari(LocalDate date, int indiceTerapia, int position, String idPaziente, int indicePaziente) {

        for (Paziente paziente: mLogopedistaViewsModels.getLogopedistaLiveData().getValue().getPazienti()) {

            if(paziente.getIdProfilo().equals(idPaziente)){

                mLogopedistaViewsModels.getLogopedistaLiveData().getValue().getPazienti().get(indicePaziente).getTerapie().get(indiceTerapia).getScenariGioco().get(position).setDataInizio(date);
                mLogopedistaViewsModels.aggiornaLogopedistaRemoto();

                break;
            }
        }
    }

}
