package viewsModels.logopedistaViewsModels;

import android.util.Log;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;

import java.util.List;

import models.database.profili.LogopedistaDAO;

import models.domain.esercizi.Esercizio;

import models.domain.profili.Appuntamento;

import models.domain.profili.Logopedista;

import models.domain.profili.Paziente;

import models.domain.scenariGioco.TemplateScenarioGioco;

import models.domain.terapie.Terapia;

import viewsModels.logopedistaViewsModels.controller.EditAppuntamentiController;

import viewsModels.logopedistaViewsModels.controller.EditDataSceneryLogopedistaController;

import viewsModels.logopedistaViewsModels.controller.RegistrazionePazienteGenitoreController;


public class LogopedistaViewsModels extends ViewModel {

    private MutableLiveData<Logopedista> mLogopedista = new MutableLiveData<>();
    private MutableLiveData<List<Appuntamento>> mListaAppuntamenti = new MutableLiveData<>();
    private MutableLiveData<List<TemplateScenarioGioco>> mListaTemplateScenarioGioco = new MutableLiveData<>();
    private MutableLiveData<List<Esercizio>> mListaTemplateEsercizi = new MutableLiveData<>();
    private RegistrazionePazienteGenitoreController mRegistrazionePazienteGenitoreController;
    private EditAppuntamentiController mEditAppuntamentiController;
    private EditDataSceneryLogopedistaController mModificaDataScenariLogopedistaController;

    public LiveData<Logopedista> getLogopedistaLiveData() {
        return mLogopedista;
    }

    public void setLogopedista(Logopedista logopedista) {
        mLogopedista.setValue(logopedista);
    }

    public LiveData<List<Appuntamento>> getAppuntamentiLiveData() {
        return mListaAppuntamenti;
    }

    public void setAppuntamenti(List<Appuntamento> appuntamenti) {
        this.mListaAppuntamenti.setValue(appuntamenti);
    }


    public void setTemplateScenariGioco(List<TemplateScenarioGioco> templateScenariGioco) {
        this.mListaTemplateScenarioGioco.setValue(templateScenariGioco);
    }


    public void setTemplateEsercizi(List<Esercizio> templateEsercizi) {
        this.mListaTemplateEsercizi.setValue(templateEsercizi);
    }


    public void aggiornaLogopedistaRemoto() {

        Logopedista logopedista = mLogopedista.getValue();

        LogopedistaDAO logopedistaDAO = new LogopedistaDAO();
        logopedistaDAO.update(logopedista);

        Log.d("LogopedistaViewModel.aggiornaLogopedistaRemoto()", "Logopedista aggiornato: " + logopedista.toString());
    }

    public void rimuoviAppuntamentoFromListaAppuntamentiLiveData(String idAppuntamento) {
        List<Appuntamento> appuntamenti = mListaAppuntamenti.getValue();
        for (Appuntamento appuntamento : appuntamenti) {
            if (appuntamento.getIdAppuntamento().equals(idAppuntamento)) {
                appuntamenti.remove(appuntamento);
                break;
            }
        }
        mListaAppuntamenti.setValue(appuntamenti);

        Log.d("LogopedistaViewModel.rimuoviAppuntamentoFromListaAppuntamentiLiveData()", "Appuntamento rimosso: " + idAppuntamento);
    }

    public Paziente getPazienteById(String id){

        List<Paziente> pazienti = mLogopedista.getValue().getListaPazienti();
        Paziente pazienteTrovato = null;

        for (Paziente paziente: pazienti){
            if(paziente.getIdProfilo().equals(id)){
                pazienteTrovato = paziente;
            }
        }

        return pazienteTrovato;
    }

    public void addTerapiaInPaziente(Terapia terapia, String idPaziente){

        List<Paziente> pazienti = mLogopedista.getValue().getListaPazienti();

        for (Paziente paziente: pazienti){
            if(paziente.getIdProfilo().equals(idPaziente)){
                paziente.addTerapia(terapia);
            }
        }

    }

    public RegistrazionePazienteGenitoreController getRegistrazionePazienteGenitoreController() {

        if (this.mRegistrazionePazienteGenitoreController == null) {
            this.mRegistrazionePazienteGenitoreController = new RegistrazionePazienteGenitoreController();
        }

        return this.mRegistrazionePazienteGenitoreController;
    }

    public EditAppuntamentiController getModificaAppuntamentiController(){

        if (this.mEditAppuntamentiController == null) {
            this.mEditAppuntamentiController = new EditAppuntamentiController();
        }

        return this.mEditAppuntamentiController;
    }


    public EditDataSceneryLogopedistaController getModificaDataScenariLogopedistaController(){

        if(this.mModificaDataScenariLogopedistaController == null){
            this.mModificaDataScenariLogopedistaController = new EditDataSceneryLogopedistaController(this);
        }

        return this.mModificaDataScenariLogopedistaController;
    }

}
