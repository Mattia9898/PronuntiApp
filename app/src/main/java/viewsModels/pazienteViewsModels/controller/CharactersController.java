package viewsModels.pazienteViewsModels.controller;


import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import viewsModels.pazienteViewsModels.PazienteViewsModels;

import models.domain.profili.Paziente;
import models.domain.profili.personaggio.Personaggio;


public class CharactersController {

    private PazienteViewsModels pazienteViewsModels;


    public CharactersController(PazienteViewsModels pazienteViewsModels) {
        this.pazienteViewsModels = pazienteViewsModels;
    }

    public static String getTextureSelectedCharacter(List<Personaggio> listCharacters,
                                                     Map<String, Integer> mapUnlockedCharacters) {

        Map<String, Integer> convertedMap = mapUnlockedCharacters.entrySet().stream().
                                                    collect(Collectors.toMap(Map.Entry::getKey, e ->
                                                            Integer.parseInt(String.valueOf(e.getValue()))));

        for (Map.Entry<String, Integer> entryMap : convertedMap.entrySet()) {

            if (entryMap.getValue() == 2) {
                for (Personaggio personaggio : listCharacters) { // cicla su tutta la lista di personaggi
                    if (personaggio.getIdPersonaggio().equals(entryMap.getKey())) { // se corrisponde al personaggio selezionato ->
                        return personaggio.getTexturePersonaggio();
                    }
                }
            }
        }

        return null;
    }

    public List<Personaggio> getSortedListCharacters(List<Personaggio> listCharacters,
                                                     List<String> listStringCharacters) {

        Map<String, Personaggio> mapIdCharacter = new HashMap<>();
        List<Personaggio> sortedList = new ArrayList<>();

        for (Personaggio personaggio : listCharacters) {
            mapIdCharacter.put(personaggio.getIdPersonaggio(), personaggio);
        }

        for (String idCharacter : listStringCharacters) {
            if (mapIdCharacter.containsKey(idCharacter)) {
                sortedList.add(mapIdCharacter.get(idCharacter));
            }
        }

        return sortedList;
    }

    private Map<String, Integer> deselectCharacter(Map<String, Integer> mapCharacters) {

        Map<String, Integer> newMap = new HashMap<>();

        for (Map.Entry<String, Integer> entryMap : mapCharacters.entrySet()) {
            String key = entryMap.getKey();
            int value = Integer.parseInt(String.valueOf(entryMap.getValue()));
            int newValue = (value == 2) ? 1 : value;
            newMap.put(key, newValue);
        }

        return newMap;
    }

    public void updateSelectedCharacter(String idCharacter) {

        // map con tutti i personaggi sbloccati attualmente dal paziente
        Map<String, Integer> mapCharacters = pazienteViewsModels.getPazienteLiveData().getValue().getPersonaggiSbloccati();
        Map<String, Integer> mapEditedCharacters = deselectCharacter(mapCharacters);

        mapEditedCharacters.put(idCharacter, 2);
        updateListUnlockedCharactersRemota(mapEditedCharacters);
    }

    public void updateListUnlockedCharactersRemota(Map<String, Integer> mapEditedCharacters) {
        pazienteViewsModels.getPazienteLiveData().getValue().setPersonaggiSbloccati(mapEditedCharacters);
        pazienteViewsModels.aggiornaTexturePersonaggioSelezionatoLiveData();
        pazienteViewsModels.aggiornaPazienteRemoto();
    }

    public void updateCoinsPaziente(int unlockCostCharacter) {
        // per ottenere i coin del paziente
        Paziente paziente = pazienteViewsModels.getPazienteLiveData().getValue();
        paziente.decreaseCoins(unlockCostCharacter);
        pazienteViewsModels.setPaziente(paziente);
        pazienteViewsModels.aggiornaPazienteRemoto();
    }

    public boolean isSufficientCoins(int unlockCostCharacter) {
        // per ottenere i coin attuali del paziente
        int patientCoins = pazienteViewsModels.getPazienteLiveData().getValue().getValuta();
        return (patientCoins >= unlockCostCharacter);
    }

}

