package viewsModels.pazienteViewsModels.controller;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;

import java.util.Map;

import java.util.stream.Collectors;

import models.domain.profili.Paziente;

import models.domain.profili.personaggio.Personaggio;

import viewsModels.pazienteViewsModels.PazienteViewsModels;

public class PersonaggiController {

    private PazienteViewsModels mPazienteViewsModels;

    public PersonaggiController(PazienteViewsModels mPazienteViewsModels) {
        this.mPazienteViewsModels = mPazienteViewsModels;
    }

    public static String getTexturePersonaggioSelezionato(List<Personaggio> listaPersonaggi, Map<String, Integer> mappaPersonaggiSbloccati) {

        Map<String, Integer> mappaConvertita = mappaPersonaggiSbloccati.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> Integer.parseInt(String.valueOf(e.getValue()))));

        for (Map.Entry<String, Integer> entry : mappaConvertita.entrySet()) {

            if (entry.getValue() == 2) {
                for (Personaggio personaggio : listaPersonaggi) {
                    if (personaggio.getIdPersonaggio().equals(entry.getKey())) {
                        return personaggio.getTexturePersonaggio();
                    }
                }
            }
        }

        return null;
    }

    public List<Personaggio> getSortedListPersonaggi(List<Personaggio> listaPersonaggi, List<String> listaId) {

        Map<String, Personaggio> mappaIdPersonaggio = new HashMap<>();

        for (Personaggio personaggio : listaPersonaggi) {
            mappaIdPersonaggio.put(personaggio.getIdPersonaggio(), personaggio);
        }

        List<Personaggio> listaOrdinata = new ArrayList<>();
        for (String id : listaId) {
            if (mappaIdPersonaggio.containsKey(id)) {
                listaOrdinata.add(mappaIdPersonaggio.get(id));
            }
        }

        return listaOrdinata;
    }

    public boolean isValutaSufficiente(int costoSbloccoPersonaggio) {
        int valutaPaziente = mPazienteViewsModels.getPazienteLiveData().getValue().getValuta();
        return (valutaPaziente >= costoSbloccoPersonaggio);
    }

    public void updateSelezionePersonaggio(String idPersonaggio) {
        Map<String, Integer> personaggi = mPazienteViewsModels.getPazienteLiveData().getValue().getPersonaggiSbloccati();

        Map<String, Integer> personaggiModificati = deselezionaPersonaggio(personaggi);
        personaggiModificati.put(idPersonaggio, 2);

        updateListaPersonaggiSbloccatiRemota(personaggiModificati);
    }

    public void updateListaPersonaggiSbloccatiRemota(Map<String, Integer> personaggiModificati) {
        mPazienteViewsModels.getPazienteLiveData().getValue().setPersonaggiSbloccati(personaggiModificati);
        mPazienteViewsModels.aggiornaTexturePersonaggioSelezionatoLiveData();
        mPazienteViewsModels.aggiornaPazienteRemoto();
    }

    public void updateValutaPaziente(int costoSbloccoPersonaggio) {
        Paziente paziente = mPazienteViewsModels.getPazienteLiveData().getValue();
        paziente.decrementaValuta(costoSbloccoPersonaggio);
        mPazienteViewsModels.setPaziente(paziente);
        mPazienteViewsModels.aggiornaPazienteRemoto();
    }

    private Map<String, Integer> deselezionaPersonaggio(Map<String, Integer> mappa) {

        Map<String, Integer> nuovaMappa = new HashMap<>();

        for (Map.Entry<String, Integer> entry : mappa.entrySet()) {
            String chiave = entry.getKey();
            int valore = Integer.parseInt(String.valueOf(entry.getValue()));
            int nuovoValore = (valore == 2) ? 1 : valore;
            nuovaMappa.put(chiave, nuovoValore);
        }

        return nuovaMappa;
    }

}

