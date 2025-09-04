package viewsModels.genitoreViewsModels;


import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import models.domain.profili.Appuntamento;
import models.domain.profili.Genitore;
import models.domain.profili.Paziente;
import models.domain.terapie.Terapia;
import models.database.profili.GenitoreDAO;
import models.database.profili.PazienteDAO;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;


import viewsModels.genitoreViewsModels.controller.EditDataSceneryGenitoreController;


// classe ViewModel che utilizza i MutableLiveData per aggiornare e gestire i dati Genitore
public class GenitoreViewsModels extends ViewModel {


    private MutableLiveData<List<Appuntamento>> mutableLiveDataListAppuntamenti = new MutableLiveData<>();

    private MutableLiveData<Paziente> mutableLiveDataPaziente = new MutableLiveData<>();

    private MutableLiveData<Genitore> mutableLiveDataGenitore = new MutableLiveData<>();

    private EditDataSceneryGenitoreController editDataSceneryGenitoreController;


    public LiveData<Paziente> getPazienteLiveData() {
        return mutableLiveDataPaziente;
    }

    public void setPaziente(Paziente paziente) {
        mutableLiveDataPaziente.setValue(paziente);
    }


    public LiveData<Genitore> getGenitoreLiveData() {
        return mutableLiveDataGenitore;
    }

    public void setGenitore(Genitore genitore) {
        mutableLiveDataGenitore.setValue(genitore);
    }


    public LiveData<List<Appuntamento>> getAppuntamentiLiveData() {
        return mutableLiveDataListAppuntamenti;
    }

    public void setAppuntamenti(List<Appuntamento> appuntamenti) {
        this.mutableLiveDataListAppuntamenti.setValue(appuntamenti);
    }


    public void updatePatient() {

        Paziente paziente = mutableLiveDataPaziente.getValue();

        PazienteDAO pazienteDAO = new PazienteDAO();
        pazienteDAO.update(paziente);

        Log.d("GenitoreViewsModels.updatePaziente()", "Patient updated: " + paziente.toString());
    }

    public void updateParent() {

        Genitore genitore = mutableLiveDataGenitore.getValue();

        GenitoreDAO genitoreDAO = new GenitoreDAO();
        genitoreDAO.update(genitore);

        Log.d("GenitoreViewsModels.updateGenitore()", "Parent updated: " + genitore.toString());
    }


    public EditDataSceneryGenitoreController getEditDataSceneryController(){

        if(this.editDataSceneryGenitoreController == null){
            this.editDataSceneryGenitoreController = new EditDataSceneryGenitoreController(this);
        }

        return this.editDataSceneryGenitoreController;
    }


	/* metodo di count terapie con data successiva a quella corrente. Tolgo 1 al numero di elementi della lista di terapie totali.
	   Così ho l'indice dell'ultima terapia (ma non ha data successiva a quella corrente)
	   Con "isBefore()" non contava la terapia con data inizio attuale, così ho preferito "isAfter()" */
    public int getIndexLastTherapy() {
        if (mutableLiveDataPaziente.getValue() != null) {
            if (mutableLiveDataPaziente.getValue().getTerapie() != null) { // sono presenti terapie
                mutableLiveDataPaziente.getValue().getTerapie().sort(Comparator.comparing(Terapia::getDataInizioTerapia));
                return mutableLiveDataPaziente.getValue().getTerapie().size() -
                        ((int) mutableLiveDataPaziente.getValue().getTerapie().stream().filter(terapia ->
                        terapia.getDataInizioTerapia().isAfter(LocalDate.now())).count()) -1;
            }
        }

        return -1; // nessuna terapia trovata
    }


}

