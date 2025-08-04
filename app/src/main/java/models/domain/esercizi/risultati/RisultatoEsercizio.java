package models.domain.esercizi.risultati;

import models.domain.DataPersistenza;

public interface RisultatoEsercizio extends DataPersistenza<RisultatoEsercizio> {

    boolean isEsitoCorretto();

}
