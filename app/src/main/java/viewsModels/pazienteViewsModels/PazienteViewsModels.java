package viewsModels.pazienteViewsModels;

import android.util.Log;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;

import java.time.LocalDate;

import java.util.ArrayList;

import java.util.Comparator;

import java.util.List;

import models.database.profili.LogopedistaDAO;

import models.database.profili.PazienteDAO;

import models.domain.profili.Paziente;

import models.domain.profili.personaggio.Personaggio;

import models.domain.scenariGioco.ScenarioGioco;

import models.domain.terapie.Terapia;

import viewsModels.pazienteViewsModels.controller.RankingController;

import viewsModels.pazienteViewsModels.controller.EsercizioCoppiaImmaginiController;

import viewsModels.pazienteViewsModels.controller.EsercizioDenominazioneImmaginiController;

import viewsModels.pazienteViewsModels.controller.EsercizioSequenzaParoleController;

import viewsModels.pazienteViewsModels.controller.CharactersController;

import views.fragment.userPaziente.ranking.Ranking;

import models.domain.esercizi.EsercizioCoppiaImmagini;

import models.domain.esercizi.EsercizioDenominazioneImmagini;

import models.domain.esercizi.EsercizioSequenzaParole;

import models.domain.esercizi.risultati.RisultatoEsercizioCoppiaImmagini;

import models.domain.esercizi.risultati.RisultatoEsercizioDenominazioneImmagini;

import models.domain.esercizi.risultati.RisultatoEsercizioSequenzaParole;

public class PazienteViewsModels extends ViewModel {

    private MutableLiveData<Paziente> mPaziente = new MutableLiveData<>();
    private MutableLiveData<List<Personaggio>> mListaPersonaggi = new MutableLiveData<>();
    private MutableLiveData<List<Ranking>> mClassifica = new MutableLiveData<>();
    private MutableLiveData<String> mTexturePersonaggioSelezionato = new MutableLiveData<>();
    private EsercizioDenominazioneImmaginiController mEsercizioDenominazioneImmaginiController;
    private EsercizioSequenzaParoleController mEsercizioSequenzaParoleController;
    private EsercizioCoppiaImmaginiController mEsercizioCoppiaImmaginiController;
    private CharactersController mCharactersController;
    private RankingController mRankingController;


    public LiveData<Paziente> getPazienteLiveData() {
        return mPaziente;
    }
    public void setPaziente(Paziente paziente) {
        mPaziente.setValue(paziente);
    }

    public LiveData<List<Personaggio>> getListaPersonaggiLiveData() {
        return mListaPersonaggi;
    }
    public void setPersonaggi(List<Personaggio> listaPersonaggi) {
        this.mListaPersonaggi.setValue(listaPersonaggi);
    }

    public Integer getTerapiaPazienteIndex(){
        LocalDate dataCorrente = LocalDate.now();
        List<Terapia> terapie = mPaziente.getValue().getTerapie();
        if(terapie!=null) {
            List<IndexDate> indexDateTerapie = filterTerapie(terapie, dataCorrente);
            indexDateTerapie.sort(Comparator.comparing(IndexDate::getDate));
            List<Integer> listaIndiciTerapie = getIndexList(indexDateTerapie);
            int sizeListaIndiciTerapie = listaIndiciTerapie.size();
            return listaIndiciTerapie.get(sizeListaIndiciTerapie - 1);
        }
        return null;
    }

    public List<Integer> getScenariPaziente(){
        LocalDate dataCorrente = LocalDate.now();
        List<Terapia> terapie = mPaziente.getValue().getTerapie();
        if(terapie!=null){
            Terapia terapia = terapie.get(getTerapiaPazienteIndex());
            List<ScenarioGioco> scenariGiocoTerapia = terapia.getListScenariGioco();
            List<IndexDate> indexDateScenariGioco = filterScenariGioco(scenariGiocoTerapia, dataCorrente);
            indexDateScenariGioco.sort(Comparator.comparing(IndexDate::getDate));
            List<Integer> listaIndici = getIndexList(indexDateScenariGioco);
            if (!listaIndici.isEmpty()) {
                return listaIndici;
            } else {
                return null;
            }
        }else {
            return null;
        }
    }

    private static List<Integer> getIndexList(List<IndexDate> indexDate){
        List<Integer> indexList = new ArrayList<>();
        for(IndexDate currentIndexDate : indexDate){
            int index = currentIndexDate.getIndex();
            indexList.add(index);
        }
        return indexList;
    }


    private List<IndexDate> filterScenariGioco(List<ScenarioGioco> scenariGioco,LocalDate dataCorrente){
        List<IndexDate> IndiciScenariFiltrati = new ArrayList<>();
        for (int index=0; index<scenariGioco.size(); index++){
            ScenarioGioco scenarioGioco = scenariGioco.get(index);
            LocalDate dataInizioScenario = scenarioGioco.getDataInizioScenarioGioco();
            if (!dataInizioScenario.isAfter(dataCorrente)){
                IndexDate indexDate = new IndexDate(index,dataInizioScenario);
                IndiciScenariFiltrati.add(indexDate);
            }
        }
        return IndiciScenariFiltrati;
    }

    private List<IndexDate> filterTerapie (List<Terapia> terapie,LocalDate dataCorrente){
        List<IndexDate> indiciTerapieFiltrati = new ArrayList<>();
        for (int index=0; index<terapie.size(); index++){
            Terapia terapia = terapie.get(index);
            LocalDate dataInizioTerapia = terapia.getDataInizioTerapia();
            if (!dataInizioTerapia.isAfter(dataCorrente)){
                IndexDate indexDate = new IndexDate(index,dataInizioTerapia);
                indiciTerapieFiltrati.add(indexDate);
            }
        }
        return indiciTerapieFiltrati;
    }

    public LiveData<List<Ranking>> getClassificaLiveData() {
        return mClassifica;
    }
    public void setClassifica(List<Ranking> classifica) {
        this.mClassifica.setValue(classifica);
    }

    public LiveData<String> getTexturePersonaggioSelezionatoLiveData() {
        return mTexturePersonaggioSelezionato;
    }
    public void setTexturePersonaggioSelezionato(String texturePersonaggioSelezionato) {
        this.mTexturePersonaggioSelezionato.setValue(texturePersonaggioSelezionato);
    }


    public void aggiornaPazienteRemoto() {
        Paziente paziente = mPaziente.getValue();

        PazienteDAO pazienteDAO = new PazienteDAO();
        pazienteDAO.update(paziente).thenAccept(pazienteAggiornato -> aggiornaClassificaLiveData());

        Log.d("PazienteViewModel.aggiornaPazienteRemoto()", "Paziente aggiornato: " + paziente.toString());
    }

    public void aggiornaClassificaLiveData() {
        PazienteDAO pazienteDAO = new PazienteDAO();
        pazienteDAO.getLogopedistaByIdPaziente(getPazienteLiveData().getValue().getIdProfilo()).thenAccept(logopedista -> {
            logopedista.aggiornaClassificaPazienti();
            LogopedistaDAO logopedistaDAO = new LogopedistaDAO();
            logopedistaDAO.update(logopedista);

            List<Ranking> classifica = RankingController.creationRanking(logopedista.getListaPazienti(), getListaPersonaggiLiveData().getValue());
            setClassifica(classifica);
        });
    }

    public void aggiornaTexturePersonaggioSelezionatoLiveData() {
        String newTexture = CharactersController.getTextureSelectedCharacter(mListaPersonaggi.getValue(), mPaziente.getValue().getPersonaggiSbloccati());
        setTexturePersonaggioSelezionato(newTexture);
    }

    public void setRisultatoEsercizioCoppiaImmaginePaziente(int indiceScenarioCorrente, int indiceEsercizio,int indiceTerapia,RisultatoEsercizioCoppiaImmagini risultato){
        EsercizioCoppiaImmagini esercizio = (EsercizioCoppiaImmagini) mPaziente.getValue().getTerapie().get(indiceTerapia).getListScenariGioco().get(indiceScenarioCorrente).getlistEsercizioRealizzabile().get(indiceEsercizio);
        esercizio.setRisultatoEsercizio(risultato);
    }
    public void setRisultatoEsercizioDenominazioneImmaginiPaziente(int indiceScenarioCorrente, int indiceEsercizio,int indiceTerapia, RisultatoEsercizioDenominazioneImmagini risultato){
        EsercizioDenominazioneImmagini esercizio = (EsercizioDenominazioneImmagini) mPaziente.getValue().getTerapie().get(indiceTerapia).getListScenariGioco().get(indiceScenarioCorrente).getlistEsercizioRealizzabile().get(indiceEsercizio);
        esercizio.setRisultatoEsercizio(risultato);
    }
    public void setRisultatoEsercizioSequenzaParolePaziente(int indiceScenarioCorrente, int indiceEsercizio,int indiceTerapia, RisultatoEsercizioSequenzaParole risultato){
        EsercizioSequenzaParole esercizio = (EsercizioSequenzaParole) mPaziente.getValue().getTerapie().get(indiceTerapia).getListScenariGioco().get(indiceScenarioCorrente).getlistEsercizioRealizzabile().get(indiceEsercizio);
        esercizio.setRisultatoEsercizio(risultato);
    }

    public EsercizioDenominazioneImmaginiController getSceltaImmaginiController() {
        if (this.mEsercizioDenominazioneImmaginiController == null) {
            this.mEsercizioDenominazioneImmaginiController = new EsercizioDenominazioneImmaginiController();
        }
        return this.mEsercizioDenominazioneImmaginiController;
    }

    public EsercizioSequenzaParoleController getSequenzaParoleController() {
        if (this.mEsercizioSequenzaParoleController == null) {
            this.mEsercizioSequenzaParoleController = new EsercizioSequenzaParoleController();
        }
        return this.mEsercizioSequenzaParoleController;
    }

    public EsercizioCoppiaImmaginiController getCoppiaImmaginiController() {
        if (this.mEsercizioCoppiaImmaginiController == null) {
            this.mEsercizioCoppiaImmaginiController = new EsercizioCoppiaImmaginiController();
        }
        return this.mEsercizioCoppiaImmaginiController;
    }

    public CharactersController getPersonaggiController() {
        if(this.mCharactersController == null){
            this.mCharactersController = new CharactersController(this);
        }
        return this.mCharactersController;
    }

    public RankingController getClassificaController() {
        if(this.mRankingController == null){
            this.mRankingController = new RankingController();
        }
        return this.mRankingController;
    }

    class IndexDate{
        private int index;
        private LocalDate date;

        public IndexDate(int index, LocalDate date){
            this.date = date;
            this.index = index;
        }
        public LocalDate getDate() {
            return date;
        }
        public int getIndex() {
            return index;
        }
    }

}
