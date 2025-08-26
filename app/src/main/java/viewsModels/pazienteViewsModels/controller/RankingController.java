package viewsModels.pazienteViewsModels.controller;

import java.util.ArrayList;

import java.util.List;

import java.util.Map;

import java.util.stream.Collectors;

import models.domain.profili.Paziente;

import models.domain.profili.personaggio.Personaggio;

import views.fragment.userPaziente.ranking.Ranking;

public class RankingController {

    public static List<Ranking> creazioneClassifica(List<Paziente> pazienti, List<Personaggio> personaggi) {

        List<Ranking> classifica = new ArrayList<>();

        for (Paziente paziente : pazienti) {

            Map<String, Integer> personaggiSbloccati = paziente.getPersonaggiSbloccati().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> Integer.parseInt(String.valueOf(e.getValue()))));

            for (Map.Entry<String, Integer> entry : personaggiSbloccati.entrySet()) {

                if (entry.getValue() == 2) {

                    String idPersonaggio = entry.getKey();

                    for (Personaggio personaggio : personaggi) {
                        if (personaggio.getIdPersonaggio().equals(idPersonaggio)) {
                            Ranking ranking = new Ranking(paziente.getUsername(), paziente.getPunteggioTot(), personaggio.getTexturePersonaggio());
                            classifica.add(ranking);
                            break;
                        }
                    }

                    break;
                }
            }
        }

        classifica.sort((p1, p2) -> p2.getScore() - p1.getScore());
        return classifica;

    }

}

