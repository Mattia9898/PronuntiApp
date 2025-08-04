package models.domain.esercizi;


import models.domain.esercizi.risultati.RisultatoEsercizio;

import java.util.Map;


public interface EsercizioRealizzabile {

    String getIdEsercizio();

    RisultatoEsercizio getRisultatoEsercizio();

    Map<String, Object> toMap();

}
