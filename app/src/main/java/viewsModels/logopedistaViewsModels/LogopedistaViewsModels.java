package viewsModels.logopedistaViewsModels;


import models.domain.scenariGioco.TemplateScenarioGioco;
import models.domain.terapie.Terapia;
import models.domain.esercizi.Esercizio;
import models.domain.profili.Appuntamento;
import models.domain.profili.Logopedista;
import models.domain.profili.Paziente;
import models.database.profili.LogopedistaDAO;

import viewsModels.logopedistaViewsModels.controller.RegistrazionePazienteGenitoreController;
import viewsModels.logopedistaViewsModels.controller.EditAppuntamentiController;
import viewsModels.logopedistaViewsModels.controller.EditDataSceneryLogopedistaController;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;


// classe ViewModel che utilizza i MutableLiveData per aggiornare e gestire i dati Logopedista
public class LogopedistaViewsModels extends ViewModel {

    private MutableLiveData<Logopedista> mutableLiveDataLogopedista = new MutableLiveData<>();

    private MutableLiveData<List<Esercizio>> mutableLiveDataListEsercizi = new MutableLiveData<>();

    private MutableLiveData<List<TemplateScenarioGioco>> mutableLiveDataListTemplateScenarioGioco = new MutableLiveData<>();

    private MutableLiveData<List<Appuntamento>> mutableLiveDataListAppuntamenti = new MutableLiveData<>();

    private RegistrazionePazienteGenitoreController registrazionePazienteGenitoreController;

    private EditDataSceneryLogopedistaController editDataSceneryLogopedistaController;

    private EditAppuntamentiController editAppuntamentiController;


    public LiveData<Logopedista> getLogopedistaLiveData() {
        return mutableLiveDataLogopedista;
    }

    public void setLogopedista(Logopedista logopedista) {
        mutableLiveDataLogopedista.setValue(logopedista);
    }


    public LiveData<List<Appuntamento>> getAppuntamentiLiveData() {
        return mutableLiveDataListAppuntamenti;
    }

    public void setAppuntamenti(List<Appuntamento> listAppuntamenti) {
        this.mutableLiveDataListAppuntamenti.setValue(listAppuntamenti);
    }


    public void setTemplateScenariGioco(List<TemplateScenarioGioco> listTemplateScenariGioco) {
        this.mutableLiveDataListTemplateScenarioGioco.setValue(listTemplateScenariGioco);
    }


    public void setTemplateEsercizi(List<Esercizio> listEsercizi) {
        this.mutableLiveDataListEsercizi.setValue(listEsercizi);
    }

    public Paziente getPazienteById(String id){

        List<Paziente> listPazienti = mutableLiveDataLogopedista.getValue().getListaPazienti();
        Paziente patientFound = null;

        for (Paziente paziente: listPazienti){
            if(paziente.getIdProfilo().equals(id)){
                patientFound = paziente;
            }
        }

        return patientFound;
    }

    public EditAppuntamentiController getEditAppuntamentiController(){

        if (this.editAppuntamentiController == null) {
            this.editAppuntamentiController = new EditAppuntamentiController();
        }

        return this.editAppuntamentiController;
    }


    public EditDataSceneryLogopedistaController getEditDataSceneryLogopedistaController(){

        if(this.editDataSceneryLogopedistaController == null){
            this.editDataSceneryLogopedistaController = new EditDataSceneryLogopedistaController(this);
        }

        return this.editDataSceneryLogopedistaController;
    }

    public RegistrazionePazienteGenitoreController getRegistrazionePazienteGenitoreController() {

        if (this.registrazionePazienteGenitoreController == null) {
            this.registrazionePazienteGenitoreController = new RegistrazionePazienteGenitoreController();
        }

        return this.registrazionePazienteGenitoreController;
    }


    public void addTerapiaPaziente(Terapia terapia, String idPaziente){

        List<Paziente> listPazienti = mutableLiveDataLogopedista.getValue().getListaPazienti();

        for (Paziente paziente: listPazienti){
            if(paziente.getIdProfilo().equals(idPaziente)){
                paziente.addTerapia(terapia);
            }
        }

    }

    public void updateLogopedistaRemoto() {

        Logopedista logopedista = mutableLiveDataLogopedista.getValue();
        LogopedistaDAO logopedistaDAO = new LogopedistaDAO();

        logopedistaDAO.update(logopedista);

        Log.d("LogopedistaViewModel.updateLogopedistaRemoto()", "Logopedista aggiornato: " + logopedista.toString());
    }

    public void removeAppuntamentoFromListAppuntamentiLiveData(String idAppuntamento) {
        List<Appuntamento> listAppuntamenti = mutableLiveDataListAppuntamenti.getValue();
        for (Appuntamento appuntamento : listAppuntamenti) {
            if (appuntamento.getIdAppuntamento().equals(idAppuntamento)) {
                listAppuntamenti.remove(appuntamento);
                break;
            }
        }
        mutableLiveDataListAppuntamenti.setValue(listAppuntamenti);

        Log.d("LogopedistaViewModel.removeAppuntamentoFromListAppuntamentiLiveData()",
                "Deleted appuntamento: " + idAppuntamento);
    }


}
