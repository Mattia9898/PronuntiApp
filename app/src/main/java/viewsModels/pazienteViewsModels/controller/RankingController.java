package viewsModels.pazienteViewsModels.controller;


import models.domain.profili.Paziente;
import models.domain.profili.personaggio.Personaggio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import views.fragment.userPaziente.ranking.Ranking;


public class RankingController {

    public static List<Ranking> creationRanking(List<Paziente> listPatients,
                                                List<Personaggio> listCharacters) {

        List<Ranking> listRanking = new ArrayList<>();

        for (Paziente paziente : listPatients) {

            Map<String, Integer> unlockedCharacters = paziente.getPersonaggiSbloccati().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> Integer.parseInt(String.valueOf(e.getValue()))));

            for (Map.Entry<String, Integer> entryMap : unlockedCharacters.entrySet()) {

                if (entryMap.getValue() == 2) {

                    String idCharacter = entryMap.getKey();

                    for (Personaggio personaggio : listCharacters) {
                        if (personaggio.getIdPersonaggio().equals(idCharacter)) {
                            Ranking ranking = new Ranking(paziente.getUsername(),
                                                            paziente.getPunteggioTot(),
                                                            personaggio.getTexturePersonaggio());
                            listRanking.add(ranking);

                            break;
                        }
                    }

                    break;
                }
            }
        }

        listRanking.sort((p1, p2) ->
                p2.getScore() - p1.getScore());

        return listRanking;

    }

}

