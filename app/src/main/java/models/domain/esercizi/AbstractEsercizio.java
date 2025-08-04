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

    public int getRicompensaErrato() {
        return ricompensaRispostaErrata;
    }


    public String getIdEsercizio() {
        return idEsercizio;
    }

    public void setIdEsercizio(String idEsercizio) {
        this.idEsercizio = idEsercizio;
    }


    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(CostantiDBAbstractEsercizio.RICOMPENSA_RISPOSTA_CORRETTA, this.ricompensaRispostaCorretta);
        map.put(CostantiDBAbstractEsercizio.RICOMPENSA_RISPOSTA_ERRATA, this.ricompensaRispostaErrata);
        return map;
    }

}
