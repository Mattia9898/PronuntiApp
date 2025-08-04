package viewsModels.pazienteViewsModels.controller;

import java.util.ArrayList;

import java.util.List;

import java.util.Map;

import java.util.stream.Collectors;

import models.domain.profili.Paziente;

import models.domain.profili.personaggio.Personaggio;

import views.fragment.userPaziente.classifica.EntryClassifica;

public class ClassificaController {

    public static List<EntryClassifica> creazioneClassifica(List<Paziente> pazienti, List<Personaggio> personaggi) {

        List<EntryClassifica> classifica = new ArrayList<>();

        for (Paziente paziente : pazienti) {

            Map<String, Integer> personaggiSbloccati = paziente.getPersonaggiSbloccati().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> Integer.parseInt(String.valueOf(e.getValue()))));

            for (Map.Entry<String, Integer> entry : personaggiSbloccati.entrySet()) {

                if (entry.getValue() == 2) {

                    String idPersonaggio = entry.getKey();

                    for (Personaggio personaggio : personaggi) {
                        if (personaggio.getIdPersonaggio().equals(idPersonaggio)) {
                            EntryClassifica entryClassifica = new EntryClassifica(paziente.getUsername(), paziente.getPunteggioTot(), personaggio.getTexturePersonaggio());
                            classifica.add(entryClassifica);
                            break;
                        }
                    }

                    break;
                }
            }
        }

        classifica.sort((p1, p2) -> p2.getPunteggio() - p1.getPunteggio());
        return classifica;

    }

}

