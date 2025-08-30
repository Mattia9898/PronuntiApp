package viewsModels.genitoreViewsModels;

import android.util.Log;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;

import java.time.LocalDate;

import java.util.Comparator;

import java.util.List;

import java.util.stream.Collectors;

import models.database.profili.GenitoreDAO;

import models.database.profili.PazienteDAO;

import models.domain.profili.Appuntamento;

import models.domain.profili.Genitore;

import models.domain.profili.Paziente;

import models.domain.terapie.Terapia;

import viewsModels.genitoreViewsModels.controller.ModificaDataScenariGenitoreController;

public class GenitoreViewsModels extends ViewModel {

    private MutableLiveData<Genitore> mGenitore = new MutableLiveData<>();
    private MutableLiveData<Paziente> mPaziente = new MutableLiveData<>();
    private MutableLiveData<List<Appuntamento>> mListaAppuntamenti = new MutableLiveData<>();

    private ModificaDataScenariGenitoreController modificaDataScenariGenitoreController;

    public LiveData<Genitore> getGenitoreLiveData() {
        return mGenitore;
    }

    public void setGenitore(Genitore genitore) {
        mGenitore.setValue(genitore);
    }

    public LiveData<Paziente> getPazienteLiveData() {
        return mPaziente;
    }

    public void setPaziente(Paziente paziente) {
        mPaziente.setValue(paziente);
    }

    public LiveData<List<Appuntamento>> getAppuntamentiLiveData() {
        return mListaAppuntamenti;
    }

    public void setAppuntamenti(List<Appuntamento> appuntamenti) {
        this.mListaAppuntamenti.setValue(appuntamenti);
    }

    public void aggiornaGenitoreRemoto() {

        Genitore genitore = mGenitore.getValue();

        GenitoreDAO genitoreDAO = new GenitoreDAO();
        genitoreDAO.update(genitore);

        Log.d("GenitoreViewsModels.aggiornaGenitoreRemoto()", "Genitore aggiornato con successo: " + genitore.toString());
    }

    public void aggiornaPazienteRemoto() {

        Paziente paziente = mPaziente.getValue();

        PazienteDAO pazienteDAO = new PazienteDAO();
        pazienteDAO.update(paziente);

        Log.d("GenitoreViewsModels.aggiornaPazienteRemoto()", "Paziente aggiornato con successo: " + paziente.toString());
    }

    public Terapia getTerapiaByIndiceFromPaziente(int indiceTerapia){
        return mPaziente.getValue().getTerapie().get(indiceTerapia);
    }

	/* metodo per il count delle terapie con data successiva a quella attuale. In seguito sottraggo -1 quel numero
	   al numero di elementi nella lista di terapie totali.
	   In questo modo ottengo l'indice dell'ultima terapia che, però, non ha data successiva a quella attuale.
	   Ho utilizzato "isAfter()" in quanto con "isBefore()" non contava la terapia con data inizio oggi */
    public int getIndiceUltimaTerapia() {

        if (mPaziente.getValue() != null) {
            if (mPaziente.getValue().getTerapie() != null) {
                mPaziente.getValue().getTerapie().sort(Comparator.comparing(Terapia::getDataInizioTerapia));
                return mPaziente.getValue().getTerapie().size()
                        - ((int) mPaziente.getValue().getTerapie().stream().filter(terapia -> terapia.getDataInizioTerapia().isAfter(LocalDate.now())).count()) -1;
            }
        }

        return -1;
    }


    public ModificaDataScenariGenitoreController getModificaDataScenariController(){

        if(this.modificaDataScenariGenitoreController == null){
            this.modificaDataScenariGenitoreController = new ModificaDataScenariGenitoreController(this);
        }

        return this.modificaDataScenariGenitoreController;
    }

}

