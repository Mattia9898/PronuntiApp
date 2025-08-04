package models.domain.esercizi;


import models.database.costantiDB.CostantiDBAbstractEsercizio;

import java.util.Map;
import java.util.HashMap;


public abstract class AbstractEsercizio implements Esercizio {


    protected int ricompensaRispostaCorretta;

    protected int ricompensaRispostaErrata;

    protected String idEsercizio;


    public AbstractEsercizio(int ricompensaRispostaCorretta, int ricompensaRispostaErrata) {
        this.ricompensaRispostaCorretta = ricompensaRispostaCorretta;
        this.ricompensaRispostaErrata = ricompensaRispostaErrata;
    }

    public AbstractEsercizio(String idEsercizio, int ricompensaRispostaCorretta, int ricompensaRispostaErrata) {
        this.idEsercizio = idEsercizio;
        this.ricompensaRispostaCorretta = ricompensaRispostaCorretta;
        this.ricompensaRispostaErrata = ricompensaRispostaErrata;
    }

    public AbstractEsercizio() {}


    public int getRicompensaCorretto() {
        return ricompensaRispostaCorretta;
    }

    public void setRicompensaCorretto(int ricompensaRispostaCorretta) {
        this.ricompensaRispostaCorretta = ricompensaRispostaCorretta;
    }


    public int getRicompensaErrato() {
        return ricompensaRispostaErrata;
    }

    public void setRicompensaErrato(int ricompensaRispostaErrata) {
        this.ricompensaRispostaErrata = ricompensaRispostaErrata;
    }


    public String getIdEsercizio() {
        return idEsercizio;
    }

    public void setIdEsercizio(String idEsercizio) {
        this.idEsercizio = idEsercizio;
    }


    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> entityMap = new HashMap<>();
        entityMap.put(CostantiDBAbstractEsercizio.RICOMPENSA_RISPOSTA_CORRETTA, this.ricompensaRispostaCorretta);
        entityMap.put(CostantiDBAbstractEsercizio.RICOMPENSA_RISPOSTA_ERRATA, this.ricompensaRispostaErrata);
        return entityMap;
    }

}
